package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {

    /**
     * The user that is being followed.
     */
    private final User followee;

//    /**
//     * Message handler that will receive task results.
//     */
//    private Handler messageHandler;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
//        this.authToken = authToken;
        this.followee = followee;
//        this.messageHandler = messageHandler;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        // We could do this from the presenter, without a task and handler, but we will
        // eventually access the database from here when we aren't using dummy data.

        String URL_PATH = "/follow";
        FollowRequest request = new FollowRequest(getAuthToken(), followee);
        FollowResponse response = getServerFacade().follow(request, URL_PATH);

        if(response.isSuccess()) {
            sendSuccessMessage();
        }
        else {
            sendFailedMessage(response.getMessage());
        }
    }
}
