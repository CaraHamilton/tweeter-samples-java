package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {

    //does it need a target user? can only ever get your feed
    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

//    @Override
//    protected Pair<List<Status>, Boolean> sendRequest() {
//        return getFakeData().getPageOfStatus(getLastItem(), getLimit());
//    }

    @Override
    protected void runSendRequest(AuthToken authToken, User targetUser, int limit, Status lastItem, List<Status> items) throws IOException, TweeterRemoteException {
        String URL_PATH = "/getfeed";

        String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
        String lastStatusPost = lastItem == null ? null : lastItem.getPost();

        FeedRequest request = new FeedRequest(authToken, targetUserAlias, limit, lastStatusPost);
        FeedResponse response = getServerFacade().getFeed(request, URL_PATH);

        if(response.isSuccess()) {
            setItems(response.getFeed());
            setHasMorePages(response.getHasMorePages());
            sendSuccessMessage();
        }
        else {
            sendFailedMessage(response.getMessage());
        }
    }
}
