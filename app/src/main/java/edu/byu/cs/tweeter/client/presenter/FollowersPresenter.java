package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {

    public interface View extends ViewPages<User> {}

    private FollowService followService;

    public FollowersPresenter(View view) {
        super(view);
        followService = new FollowService();
    }

    @Override
    public void getItems(User user) {
        followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetFollowerObserver());

    }

    public class GetFollowerObserver implements FollowService.GetFollowersObserver {

        @Override
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastItem = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(followers);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get followers: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get followers because of exception: " + exception.getMessage());
        }
    }
}
