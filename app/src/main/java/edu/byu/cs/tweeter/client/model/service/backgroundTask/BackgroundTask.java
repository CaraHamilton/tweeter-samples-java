package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.util.FakeData;

public abstract class BackgroundTask implements Runnable {

    private static final String LOG_TAG = "Task";

    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";

    protected final Handler messageHandler;

    private ServerFacade serverFacade;

    protected BackgroundTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    ServerFacade getServerFacade() {
        if(serverFacade == null) {
            serverFacade = new ServerFacade();
        }

        return serverFacade;
    }

    @Override
    public void run() {
        try {
            runTask();
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    //take this out after refactoring!!
    protected FakeData getFakeData() {
        return new FakeData();
    }

//    protected abstract void runTask() throws IOException;



    // This method is public instead of protected to make it accessible to test cases
    public void sendSuccessMessage() {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        loadSuccessBundle(msgBundle);
        sendMessage(msgBundle);
    }

    // To be overridden by each task to add information to the bundle
    protected void loadSuccessBundle(Bundle msgBundle) {
        // By default, do nothing
    }

    // This method is public instead of protected to make it accessible to test cases
    public void sendFailedMessage(String message) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putString(MESSAGE_KEY, message);
        sendMessage(msgBundle);
    }

    // This method is public instead of protected to make it accessible to test cases
    public void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);
        sendMessage(msgBundle);
    }

    private void sendMessage(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

    protected abstract void runTask() throws IOException, TweeterRemoteException;
}