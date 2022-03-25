package edu.byu.cs.tweeter.client.model.service;

import junit.framework.TestCase;

import org.mockito.Mockito;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;

public class StatusServiceTest extends TestCase {

    private GetStoryTask getStoryTaskSpy;
    private StatusService statusServiceSpy;

    @Override
    public void setUp() throws Exception {
        super.setUp();
//        getStoryTaskSpy = Mockito.spy(GetStoryTask.class);
        statusServiceSpy = Mockito.spy(StatusService.class);
    }

    public void testGetStory() {
        getStoryTaskSpy.run();
        //create background task
        //notify server observer of outcome
        //verify observer was notified in the case of successful story retrieval
    }
}