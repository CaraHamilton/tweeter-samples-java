package edu.byu.cs.tweeter.client.model.service;

import android.os.Looper;
import android.os.Message;

import java.util.List;

public abstract class HandleList<K> extends Handle<ListObserver> {

    public HandleList() {
//        super();
    }

    public abstract String getTaskItemsKey();
    public abstract String getTaskPagesKey();


    @Override
    public void handleMessageSuccess(Message msg) {
        List<K> items = (List<K>) msg.getData().getSerializable(getTaskItemsKey());
        boolean hasMorePages = msg.getData().getBoolean(getTaskPagesKey());
        getObserver().handleSuccess(items, hasMorePages);
    }

}
