package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends Presenter<MainPresenter.View> {

    public interface View extends ViewBase {
        void logout();
        void setFollowerCount(int count);
        void setFolloweeCount(int count);
        void setIsFollower(boolean value);
        void follow();
        void unfollow();
        void displayPostMessage();
    }

    private UserService userService;
    private FollowService followService;
    private StatusService statusService;

    public MainPresenter(View view) {
        super(view);
        userService = new UserService();
        followService = new FollowService();
        statusService = new StatusService();
    }

    public void postStatus(Status newStatus) {
        statusService.postStatus(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new PostStatusObserver());
    }

    public void unfollow(User selectedUser) {
        followService.unfollow(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowObserver());
    }

    public void follow(User selectedUser) {
        followService.follow(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowObserver());
    }

    public void isFollower(User currUser, User selectedUser) {
        followService.isFollower(Cache.getInstance().getCurrUserAuthToken(),
                currUser, selectedUser, new IsFollowerObserver());
    }

    public void getFollowingCount(User selectedUser) {
        followService.getFollowingCount(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser,new GetFollowingCountObserver());
    }

    public void getFollowersCount(User selectedUser) {
        followService.getFollowersCount(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new GetFollowersCountObserver());
    }

    public void logout() {
        userService.logout(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
    }

    public class PostStatusObserver implements StatusService.PostStatusObserver {

        @Override
        public void handleSuccess() {
            view.displayPostMessage();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to post status: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to post status because of exception: " + exception.getMessage());
        }

    }

    public class UnfollowObserver implements FollowService.UnfollowObserver {

        @Override
        public void handleSuccess() {
            view.unfollow();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to unfollow: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to unfollow because of exception: " + exception.getMessage());
        }
    }

    public class FollowObserver implements FollowService.FollowObserver {

        @Override
        public void handleSuccess() {
            view.follow();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to follow: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to follow because of exception: " + exception.getMessage());
        }
    }

    public class IsFollowerObserver implements FollowService.IsFollowerObserver {

        @Override
        public void handleSuccess(boolean isFollower) {
            // If logged in user if a follower of the selected user, display the follow button as "following"
            view.setIsFollower(isFollower);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to determine following relationship: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to determine following relationship because of exception: " + exception.getMessage());
        }
    }

    public class GetFollowingCountObserver implements FollowService.GetFollowingCountObserver {

        @Override
        public void handleSuccess(int count) {
            view.setFolloweeCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get following count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get following count because of exception: " + exception.getMessage());
        }
    }

    public class GetFollowersCountObserver implements FollowService.GetFollowersCountObserver {

        @Override
        public void handleSuccess(int count) {
            view.setFollowerCount(count);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get followers count: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get followers count because of exception: " + exception.getMessage());
        }
    }

    public class LogoutObserver implements UserService.LogoutObserver {

        @Override
        public void handleSuccess() {

            view.logout();
            Cache.getInstance().clearCache();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to logout because of exception: " + exception.getMessage());
        }
    }
}
