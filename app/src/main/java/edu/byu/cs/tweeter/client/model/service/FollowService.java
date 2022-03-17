package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends Service {

    static final String URL_PATH = "/getfollowing";

    private ServerFacade serverFacade;

    /**
     * An observer interface to be implemented by observers who want to be notified when
     * asynchronous operations complete.
     */
    public interface GetFollowingObserver {
        void handleSuccess(List<User> followees, boolean hasMorePages);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    /**
     * Creates an instance.
     */
    public FollowService() {}

    /**
     * Requests the users that the user specified in the request is following.
     * Limits the number of followees returned and returns the next set of
     * followees after any that were returned in a previous request.
     * This is an asynchronous operation.
     *
     * @param authToken the session auth token.
     * @param targetUser the user for whom followees are being retrieved.
     * @param limit the maximum number of followees to return.
     * @param lastFollowee the last followee returned in the previous request (can be null).
     */
    public void getFollowees(AuthToken authToken, User targetUser, int limit, User lastFollowee, GetFollowingObserver observer) {
        GetFollowingTask followingTask = getGetFollowingTask(authToken, targetUser, limit, lastFollowee, observer);
        BackgroundTaskUtils.runTask(followingTask);
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public ServerFacade getServerFacade() {
        if(serverFacade == null) {
            serverFacade = new ServerFacade();
        }

        return new ServerFacade();
    }

    /**
     * Returns an instance of {@link GetFollowingTask}. Allows mocking of the
     * GetFollowingTask class for testing purposes. All usages of GetFollowingTask
     * should get their instance from this method to allow for proper mocking.
     *
     * @return the instance.
     */
    // This method is public so it can be accessed by test cases
    public GetFollowingTask getGetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee, GetFollowingObserver observer) {
        return new GetFollowingTask(authToken, targetUser, limit, lastFollowee, new MessageHandler(observer));
    }

    /**
     * Handles messages from the background task indicating that the task is done, by invoking
     * methods on the observer.
     */
    public static class MessageHandler extends Handler {

        private final GetFollowingObserver observer;

        public MessageHandler(GetFollowingObserver observer) {
            super(Looper.getMainLooper());
            this.observer = observer;
        }

        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            boolean success = bundle.getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) bundle.getSerializable(GetFollowingTask.FOLLOWEES_KEY);
                boolean hasMorePages = bundle.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
                observer.handleSuccess(followees, hasMorePages);
            } else if (bundle.containsKey(GetFollowingTask.MESSAGE_KEY)) {
                String errorMessage = bundle.getString(GetFollowingTask.MESSAGE_KEY);
                observer.handleFailure(errorMessage);
            } else if (bundle.containsKey(GetFollowingTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) bundle.getSerializable(GetFollowingTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    /**
     * Background task that retrieves a page of other users being followed by a specified user.
     */
    public class GetFollowingTask extends BackgroundTask {

        private static final String LOG_TAG = "GetFollowingTask";

        public static final String FOLLOWEES_KEY = "followees";
        public static final String MORE_PAGES_KEY = "more-pages";

        /**
         * Auth token for logged-in user.
         */
        protected AuthToken authToken;
        /**
         * The user whose following is being retrieved.
         * (This can be any user, not just the currently logged-in user.)
         */
        protected User targetUser;
        /**
         * Maximum number of followed users to return (i.e., page size).
         */
        protected int limit;
        /**
         * The last person being followed returned in the previous page of results (can be null).
         * This allows the new page to begin where the previous page ended.
         */
        protected User lastFollowee;
        /**
         * The followee users returned by the server.
         */
        private List<User> followees;
        /**
         * If there are more pages, returned by the server.
         */
        private boolean hasMorePages;
        public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                                Handler messageHandler) {
            super(messageHandler);
            this.authToken = authToken;
            this.targetUser = targetUser;
            this.limit = limit;
            this.lastFollowee = lastFollowee;
        }

        @Override
        protected void runTask() {
            try {
                String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
                String lastFolloweeAlias = lastFollowee == null ? null : lastFollowee.getAlias();

                FollowingRequest request = new FollowingRequest(authToken, targetUserAlias, limit, lastFolloweeAlias);
                FollowingResponse response = getServerFacade().getFollowees(request, URL_PATH);

                if(response.isSuccess()) {
                    this.followees = response.getFollowees();
                    this.hasMorePages = response.getHasMorePages();
                    sendSuccessMessage();
                }
                else {
                    sendFailedMessage(response.getMessage());
                }
            } catch (IOException | TweeterRemoteException ex) {
                Log.e(LOG_TAG, "Failed to get followees", ex);
                sendExceptionMessage(ex);
            }
        }

        protected void loadSuccessBundle(Bundle msgBundle) {
            msgBundle.putSerializable(FOLLOWEES_KEY, (Serializable) this.followees);
            msgBundle.putBoolean(MORE_PAGES_KEY, this.hasMorePages);
        }
    }

    public interface GetFollowersObserver extends ListObserver<User> {}

    public interface GetFollowersCountObserver extends Observer {
        void handleSuccess(int count);
    }

    public interface GetFollowingCountObserver extends Observer {
        void handleSuccess(int count);
    }

    public interface IsFollowerObserver extends Observer {
        void handleSuccess(boolean isFollower);
    }

    public interface FollowObserver extends Observer {
        void handleSuccess();
    }

    public interface UnfollowObserver extends Observer {
        void handleSuccess();
    }

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower, GetFollowersObserver getFollowerObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new GetFollowersHandler(getFollowerObserver));
        executeSingleThread(getFollowersTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends HandleList {
        private  GetFollowersObserver observer;

        public GetFollowersHandler(GetFollowersObserver observer) {
            this.observer = observer;
        }

        @Override
        public String getTaskItemsKey() {
            return GetFollowersTask.ITEMS_KEY;
        }

        @Override
        public String getTaskPagesKey() {
            return GetFollowersTask.MORE_PAGES_KEY;
        }

        @Override
        public GetFollowersObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return GetFollowersTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return GetFollowersTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return GetFollowersTask.SUCCESS_KEY;
        }
    }

    public void getFollowersCount(AuthToken currUserAuthToken, User selectedUser, GetFollowersCountObserver getFollowersCountObserver) {
        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new GetFollowersCountHandler(getFollowersCountObserver));
        executeFixedThread(followersCountTask);
    }

    // GetFollowersCountHandler

    private class GetFollowersCountHandler extends Handle {
        private GetFollowersCountObserver observer;

        public GetFollowersCountHandler(GetFollowersCountObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessageSuccess(Message msg) {
            int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
            observer.handleSuccess(count);
        }

        @Override
        public GetFollowersCountObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return GetFollowersCountTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return GetFollowersCountTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return GetFollowersCountTask.SUCCESS_KEY;
        }
    }

    public void getFollowingCount(AuthToken currUserAuthToken, User selectedUser, MainPresenter.GetFollowingCountObserver getFollowingCountObserver) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(currUserAuthToken,
                selectedUser, new GetFollowingCountHandler(getFollowingCountObserver));
        executeFixedThread(followingCountTask);
    }

    // GetFollowingCountHandler

    private class GetFollowingCountHandler extends Handle {
        private GetFollowingCountObserver observer;

        public GetFollowingCountHandler(GetFollowingCountObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessageSuccess(Message msg) {
            int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
            observer.handleSuccess(count);
        }

        @Override
        public GetFollowingCountObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return GetFollowingCountTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return GetFollowingCountTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return GetFollowingCountTask.SUCCESS_KEY;
        }
    }

    public void isFollower(AuthToken currUserAuthToken, User currUser, User selectedUser, MainPresenter.IsFollowerObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken,
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(isFollowerObserver));
        executeSingleThread(isFollowerTask);
    }

    // IsFollowerHandler

    private class IsFollowerHandler extends Handle {
        private IsFollowerObserver observer;

        public IsFollowerHandler(IsFollowerObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessageSuccess(Message msg) {
            boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
            observer.handleSuccess(isFollower);
        }

        @Override
        public IsFollowerObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return IsFollowerTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return IsFollowerTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return IsFollowerTask.SUCCESS_KEY;
        }
    }

    public void follow(AuthToken currUserAuthToken, User selectedUser, FollowObserver followObserver) {
        FollowTask followTask = new FollowTask(currUserAuthToken,
                selectedUser, new FollowHandler(followObserver));
        executeSingleThread(followTask);
    }

    // FollowHandler

    private class FollowHandler extends Handle {
        private FollowObserver observer;

        public FollowHandler(FollowObserver obsesrver) {
            this.observer = obsesrver;
        }

        @Override
        public void handleMessageSuccess(Message msg) {
            observer.handleSuccess();
        }

        @Override
        public FollowObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return FollowTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return FollowTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return FollowTask.SUCCESS_KEY;
        }
    }

    public void unfollow(AuthToken currUserAuthToken, User selectedUser, MainPresenter.UnfollowObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(currUserAuthToken,
                selectedUser, new UnfollowHandler(unfollowObserver));
        executeSingleThread(unfollowTask);
    }

    // UnfollowHandler

    private class UnfollowHandler extends Handle {
        private UnfollowObserver observer;

        public UnfollowHandler(UnfollowObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessageSuccess(Message msg) {
            observer.handleSuccess();
        }

        @Override
        public UnfollowObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return UnfollowTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return UnfollowTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return UnfollowTask.SUCCESS_KEY;
        }
    }

}
