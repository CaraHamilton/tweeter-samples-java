package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

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

//    @Override
//    public void run() {
//        try {
//
//            sendSuccessMessage();
//
//        } catch (Exception ex) {
//            Log.e(LOG_TAG, ex.getMessage(), ex);
//            sendExceptionMessage(ex);
//        }
//    }

    @Override
    protected void runTask() {
        // We could do this from the presenter, without a task and handler, but we will
        // eventually access the database from here when we aren't using dummy data.

        // Call sendSuccessMessage if successful
        sendSuccessMessage();
        // or call sendFailedMessage if not successful
        // sendFailedMessage()
    }

//    private void sendSuccessMessage() {
//        Bundle msgBundle = new Bundle();
//        msgBundle.putBoolean(SUCCESS_KEY, true);
//
//        Message msg = Message.obtain();
//        msg.setData(msgBundle);
//
//        messageHandler.sendMessage(msg);
//    }
//
//    @Override
//    protected void loadMessageHandle(Bundle msgBundle) {
//
//    }
//
//    private void sendFailedMessage(String message) {
//        Bundle msgBundle = new Bundle();
//        msgBundle.putBoolean(SUCCESS_KEY, false);
//        msgBundle.putString(MESSAGE_KEY, message);
//
//        Message msg = Message.obtain();
//        msg.setData(msgBundle);
//
//        messageHandler.sendMessage(msg);
//    }
//
//    private void sendExceptionMessage(Exception exception) {
//        Bundle msgBundle = new Bundle();
//        msgBundle.putBoolean(SUCCESS_KEY, false);
//        msgBundle.putSerializable(EXCEPTION_KEY, exception);
//
//        Message msg = Message.obtain();
//        msg.setData(msgBundle);
//
//        messageHandler.sendMessage(msg);
//    }
}
