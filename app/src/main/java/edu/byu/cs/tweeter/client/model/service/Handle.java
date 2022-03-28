package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

public abstract class Handle<T extends Observer> extends Handler {

    public Handle() {
        super(Looper.getMainLooper());
    }

    public abstract void handleMessageSuccess(Message msg);

    public abstract T getObserver();
    public abstract String getTaskMessageKey();
    public abstract String getTaskExceptionKey();
    public abstract String getTaskSuccessKey();

    @Override
    public void handleMessage(@NonNull Message msg) {

        boolean success = msg.getData().getBoolean(getTaskSuccessKey());
        if (success) {
            handleMessageSuccess(msg);
        } else if (msg.getData().containsKey(getTaskMessageKey())) {
            String message = msg.getData().getString(getTaskMessageKey());
            getObserver().handleFailure(message);
        } else if (msg.getData().containsKey(getTaskExceptionKey())) {
            Exception ex = (Exception) msg.getData().getSerializable(getTaskExceptionKey());
            getObserver().handleException(ex);
        }
    }
}
