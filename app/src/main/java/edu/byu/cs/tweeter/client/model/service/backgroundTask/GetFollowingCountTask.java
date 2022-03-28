package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected void sendRequest() throws IOException, TweeterRemoteException {
        String URL_PATH = "/getfollowingcount";
        User targetUser = getTargetUser() == null ? null : getTargetUser();

        FollowingCountRequest request = new FollowingCountRequest(getAuthToken(), targetUser);
        FollowingCountResponse response = getServerFacade().getFollowingCount(request, URL_PATH);

        if (response.isSuccess()) {
            setCount(response.getCount());
            sendSuccessMessage();
        }
        else {
            sendFailedMessage(response.getMessage());
        }
    }

//    @Override
//    protected int runCountTask() {
//        return 20;
//    }
}
