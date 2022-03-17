package edu.byu.cs.tweeter.client.model.service;

import android.os.Message;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends Service {

    public interface GetStoryObserver extends ListObserver<Status> {}

    public interface GetFeedObserver extends ListObserver<Status> {}

    public interface PostStatusObserver extends Observer {
        void handleSuccess();
    }

    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetStoryObserver getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetStoryHandler(getStoryObserver));
        executeSingleThread(getStoryTask);
    }

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus, GetFeedObserver getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetFeedHandler(getFeedObserver));
        executeSingleThread(getFeedTask);
    }

    public void postStatus(AuthToken currUserAuthToken, Status newStatus, MainPresenter.PostStatusObserver postStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken,
                newStatus, new PostStatusHandler(postStatusObserver));
        executeSingleThread(statusTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends HandleList {
        private GetStoryObserver observer;

        public GetStoryHandler(GetStoryObserver observer) {
            this.observer = observer;
        }

        @Override
        public GetStoryObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return GetStoryTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return GetStoryTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return GetStoryTask.SUCCESS_KEY;
        }

        @Override
        public String getTaskItemsKey() {
            return GetStoryTask.ITEMS_KEY;
        }

        @Override
        public String getTaskPagesKey() {
            return GetStoryTask.MORE_PAGES_KEY;
        }
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends HandleList {
        private GetFeedObserver observer;

        public GetFeedHandler(GetFeedObserver observer) {
            this.observer = observer;
        }

        @Override
        public GetFeedObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return GetFeedTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return GetFeedTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return GetFeedTask.SUCCESS_KEY;
        }

        @Override
        public String getTaskItemsKey() {
            return GetFeedTask.ITEMS_KEY;
        }

        @Override
        public String getTaskPagesKey() {
            return GetFeedTask.MORE_PAGES_KEY;
        }
    }

    // PostStatusHandler

    private class PostStatusHandler extends Handle {
        private PostStatusObserver observer;

        public PostStatusHandler(PostStatusObserver observer){
            this.observer = observer;
        }

        @Override
        public void handleMessageSuccess(Message msg) {
            observer.handleSuccess();
        }

        @Override
        public PostStatusObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return PostStatusTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return PostStatusTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return PostStatusTask.SUCCESS_KEY;
        }
    }
}
