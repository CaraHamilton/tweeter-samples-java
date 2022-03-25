package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * The presenter for the "following" functionality of the application.
 */
//public class FollowingPresenter implements FollowService.GetFollowingObserver {
public class FollowingPresenter extends PagedPresenter<User> {

    private FollowService followService;

    public FollowService getFollowService() {
        return followService;
    }

    @Override
    public void getItems(User user) {
        followService.getFollowees(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetFollowingObserver());
    }

    public interface View extends ViewPages<User> {
        void setLoading(boolean b);
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public FollowingPresenter(View view) {
        super(view);
        followService = new FollowService();
    }

    public class GetFollowingObserver implements FollowService.GetFollowingObserver  {

        @Override
        public void handleSuccess(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastItem = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(followees);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get following because of exception: " + exception.getMessage());

        }
    }
}