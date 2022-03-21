package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

//    @Override
//    protected Pair<List<User>, Boolean> sendRequest() {
//        return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
//    }

    @Override
    protected void runSendRequest(AuthToken authToken, User targetUser, int limit, User lastItem, List<User> items) throws IOException, TweeterRemoteException {

        String URL_PATH = "/getfollowers";

        String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
        String lastFollowerAlias = lastItem == null ? null : lastItem.getAlias();

        FollowersRequest request = new FollowersRequest(authToken, targetUserAlias, limit, lastFollowerAlias);
        FollowersResponse response = getServerFacade().getFollowers(request, URL_PATH);

        if(response.isSuccess()) {
            setItems(response.getFollowers());
            setHasMorePages(response.getHasMorePages());
            sendSuccessMessage();
        }
        else {
            sendFailedMessage(response.getMessage());
        }
    }
}
