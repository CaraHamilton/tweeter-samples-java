package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

/**
 * Contains the business logic to support the login operation.
 */
public class UserService extends Service {

//    private static final String URL_PATH = "/login";

//    private ServerFacade serverFacade;

    /**
     * An observer interface to be implemented by observers who want to be notified when
     * asynchronous operations complete.
     */
//    public interface LoginObserver {
//        void handleSuccess(User user, AuthToken authToken);
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    /**
     * Creates an instance.
     *
     */
     public UserService() {
     }

    /**
     * Makes an asynchronous login request.
     *
     * @param username the user's name.
     * @param password the user's password.
     */
    public void login(String username, String password, LoginObserver observer) {
        LoginTask loginTask = getLoginTask(username, password, observer);
        BackgroundTaskUtils.runTask(loginTask);
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
//    ServerFacade getServerFacade() {
//        if(serverFacade == null) {
//            serverFacade = new ServerFacade();
//        }
//
//        return serverFacade;
//    }


    /**
     * Returns an instance of {@link LoginTask}. Allows mocking of the LoginTask class for
     * testing purposes. All usages of LoginTask should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    LoginTask getLoginTask(String username, String password, LoginObserver loginObserver) {
        return new LoginTask(username, password, new LoginHandler(loginObserver));
    }


    /**
     * Handles messages from the background task indicating that the task is done, by invoking
     * methods on the observer.
     */
//    private static class MessageHandler extends Handler {
//
//        private final LoginObserver observer;
//
//        MessageHandler(LoginObserver observer) {
//            super(Looper.getMainLooper());
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(Message message) {
//            Bundle bundle = message.getData();
//            boolean success = bundle.getBoolean(LoginTask.SUCCESS_KEY);
//            if (success) {
//                User user = (User) bundle.getSerializable(LoginTask.USER_KEY);
//                AuthToken authToken = (AuthToken) bundle.getSerializable(LoginTask.AUTH_TOKEN_KEY);
//                observer.handleSuccess(user, authToken);
//            } else if (bundle.containsKey(LoginTask.MESSAGE_KEY)) {
//                String errorMessage = bundle.getString(LoginTask.MESSAGE_KEY);
//                observer.handleFailure(errorMessage);
//            } else if (bundle.containsKey(LoginTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) bundle.getSerializable(LoginTask.EXCEPTION_KEY);
//                observer.handleException(ex);
//            }
//        }
//    }
//
//    /**
//     * Background task that logs in a user (i.e., starts a session).
//     */
//    private class LoginTask extends BackgroundTask {
//
//        private static final String LOG_TAG = "LoginTask";
//
//        public static final String USER_KEY = "user";
//        public static final String AUTH_TOKEN_KEY = "auth-token";
//
//        /**
//         * The user's username (or "alias" or "handle"). E.g., "@susan".
//         */
//        private String username;
//        /**
//         * The user's password.
//         */
//        private String password;
//
//        /**
//         * The logged-in user returned by the server.
//         */
//        protected User user;
//
//        /**
//         * The auth token returned by the server.
//         */
//        protected AuthToken authToken;
//
//        public LoginTask(String username, String password, Handler messageHandler) {
//            super(messageHandler);
//
//            this.username = username;
//            this.password = password;
//        }
//
//        @Override
//        protected void runTask() {
//            try {
//                LoginRequest request = new LoginRequest(username, password);
//                LoginResponse response = getServerFacade().login(request, URL_PATH);
//
//                if(response.isSuccess()) {
//                    this.user = response.getUser();
//                    this.authToken = response.getAuthToken();
//                    sendSuccessMessage();
//                }
//                else {
//                    sendFailedMessage(response.getMessage());
//                }
//            } catch (Exception ex) {
//                Log.e(LOG_TAG, ex.getMessage(), ex);
//                sendExceptionMessage(ex);
//            }
//        }
//
//        protected void loadSuccessBundle(Bundle msgBundle) {
//            msgBundle.putSerializable(USER_KEY, this.user);
//            msgBundle.putSerializable(AUTH_TOKEN_KEY, this.authToken);
//        }
//    }

    public interface LoginObserver extends Observer {
        void handleSuccess(User loggedInUser, AuthToken authToken);
    }

    public interface GetUserObserver extends Observer {
        void handleSuccess(User user);
        void sendMessage(String message);
    }

    public interface RegisterObserver extends Observer {
        void handleSuccess(User registeredUser, AuthToken authToken);
    }

    public interface LogoutObserver extends Observer {
        void handleSuccess();
    }

    public void getUser(AuthToken currUserAuthToken, String userAliasString, GetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken,
                userAliasString, new GetUserHandler(getUserObserver));
        executeSingleThread(getUserTask);

        getUserObserver.sendMessage("Getting user's profile...");
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64, RegisterObserver registerObserver) {
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new RegisterHandler(registerObserver));
        executeSingleThread(registerTask);

    }

    public void logout(AuthToken currUserAuthToken, LogoutObserver logoutObserver) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, new LogoutHandler(logoutObserver));
        executeSingleThread(logoutTask);

    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler extends Handle {

        private GetUserObserver observer;

        public GetUserHandler(GetUserObserver observer) {
            this.observer = observer;
        }

        public GetUserObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return GetUserTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return GetUserTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return GetUserTask.SUCCESS_KEY;
        }

        @Override
        public void handleMessageSuccess(Message msg) {
            User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
            observer.handleSuccess(user);
        }
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends Handle {
        private LoginObserver observer;

        public LoginHandler (LoginObserver observer) {
            this.observer = observer;
        }


        @Override
        public LoginObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return LoginTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return LoginTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return LoginTask.SUCCESS_KEY;
        }

        @Override
        public void handleMessageSuccess(Message msg) {
            User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

            observer.handleSuccess(loggedInUser, authToken);
        }
    }

    // RegisterHandler

    private class RegisterHandler extends Handle {
        private RegisterObserver observer;

        public RegisterHandler(RegisterObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessageSuccess(Message msg) {
            User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);
            observer.handleSuccess(registeredUser, authToken);
        }

        @Override
        public RegisterObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return RegisterTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return RegisterTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return RegisterTask.SUCCESS_KEY;
        }
    }

    // LogoutHandler

    private class LogoutHandler extends Handle {
        private LogoutObserver observer;

        public LogoutHandler(LogoutObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessageSuccess(Message msg) {
            observer.handleSuccess();
        }

        @Override
        public LogoutObserver getObserver() {
            return observer;
        }

        @Override
        public String getTaskMessageKey() {
            return LogoutTask.MESSAGE_KEY;
        }

        @Override
        public String getTaskExceptionKey() {
            return LogoutTask.EXCEPTION_KEY;
        }

        @Override
        public String getTaskSuccessKey() {
            return LogoutTask.SUCCESS_KEY;
        }
    }

}
