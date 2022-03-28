package edu.byu.cs.tweeter.client.model.service;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.presenter.StoryPresenter;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusServiceTest extends TestCase {

    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private StatusService statusServiceSpy;
    private StatusServiceObserver observer;
    private StoryPresenter presenterMock;

    private AuthToken fakeAuthToken;
    private User fakeUser;

    private CountDownLatch countDownLatch;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        fakeAuthToken = new AuthToken();
        fakeUser = new User("Mary", "Jane", "@Mary_Jane", FEMALE_IMAGE_URL);

        statusServiceSpy = Mockito.spy(StatusService.class);
        presenterMock = Mockito.mock(StoryPresenter.class);

        Mockito.doReturn(statusServiceSpy).when(presenterMock).getStatusService();
        observer = new StatusServiceObserver();

        resetCountDownLatch();
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    @Test
    public void testGetStory() throws InterruptedException {
        statusServiceSpy.getStory(fakeAuthToken, fakeUser, 3, null, observer);
        awaitCountDownLatch();

        List<Status> expectedStatuses = new FakeData().getFakeStatuses().subList(0, 3);
        Assert.assertTrue(observer.isSuccess());
        Assert.assertNull(observer.getMessage());
        Assert.assertEquals(expectedStatuses.get(0).post, observer.getStatuses().get(0).post);
        Assert.assertEquals(expectedStatuses.get(1).post, observer.getStatuses().get(1).post);
        Assert.assertEquals(expectedStatuses.get(2).post, observer.getStatuses().get(2).post);
        Assert.assertTrue(observer.getHasMorePages());
        Assert.assertNull(observer.getException());
    }

    private class StatusServiceObserver implements StatusService.GetStoryObserver {

        private boolean success;
        private String message;
        private List<Status> statuses;
        private boolean hasMorePages;
        private Exception exception;

        @Override
        public void handleSuccess(List<Status> items, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.statuses = items;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.statuses = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.success = false;
            this.message = null;
            this.statuses = null;
            this.hasMorePages = false;
            this.exception = exception;

            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getStatuses() {
            return statuses;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public Exception getException() {
            return exception;
        }
    }
}