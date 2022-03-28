package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

//    @Override
//    protected int runCountTask() {
//        return 20;
//    }

    @Override
    protected void sendRequest() throws IOException, TweeterRemoteException {
        String URL_PATH = "/getfollowerscount";
        User targetUser = getTargetUser() == null ? null : getTargetUser();

        FollowersCountRequest request = new FollowersCountRequest(getAuthToken(), targetUser);
        FollowersCountResponse response = getServerFacade().getFollowersCount(request, URL_PATH);

        if (response.isSuccess()) {
            setCount(response.getCount());
            sendSuccessMessage();
        }
        else {
            sendFailedMessage(response.getMessage());
        }
    }
}
