package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    public  interface View extends ViewPages<Status> {}

    private StatusService statusService;

    public StatusService getStatusService() {
        return statusService;
    }

    public StoryPresenter(View view) {
        super(view);
        statusService = new StatusService();
    }

    @Override
    public void getItems(User user) {
        statusService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem, new GetStoryObserver());
    }

    public class GetStoryObserver implements StatusService.GetStoryObserver {

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastItem = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(statuses);
        }

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get story: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get story because of exception: " + exception.getMessage());

        }
    }
}
