package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.StoryResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

//    @Override
//    protected Pair<List<Status>, Boolean> sendRequest() {
//        return getFakeData().getPageOfStatus(getLastItem(), getLimit());
//    }

    @Override
    protected void runSendRequest(AuthToken authToken, User targetUser, int limit, Status lastItem, List<Status> items) throws IOException, TweeterRemoteException {
        String URL_PATH = "/getstory";
        String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
        String lastStatusPost = lastItem == null ? null : lastItem.getPost();

        StoryRequest request = new StoryRequest(authToken, targetUserAlias, limit, lastStatusPost);
        StoryResponse response = getServerFacade().getStory(request, URL_PATH);

        if(response.isSuccess()) {
            setItems(response.getStory());
            setHasMorePages(response.getHasMorePages());
            sendSuccessMessage();
        }
        else {
            sendFailedMessage(response.getMessage());
        }
    }
}
