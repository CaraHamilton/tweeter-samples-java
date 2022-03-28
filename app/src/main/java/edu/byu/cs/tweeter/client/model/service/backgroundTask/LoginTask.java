package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() {

        User loggedInUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(loggedInUser, authToken);
    }

    @Override
    protected void runSendRequest(AuthToken authToken, String username, String password) throws IOException, TweeterRemoteException {
        String URL_PATH = "/login";
        LoginRequest request = new LoginRequest(username, password);
        LoginResponse response = getServerFacade().login(request, URL_PATH);

        if(response.isSuccess()) {
            setAuthenticatedUser(response.getUser());
            setAuthToken(response.getAuthToken());
            sendSuccessMessage();
        }
        else {
            sendFailedMessage(response.getMessage());
        }
    }
}
