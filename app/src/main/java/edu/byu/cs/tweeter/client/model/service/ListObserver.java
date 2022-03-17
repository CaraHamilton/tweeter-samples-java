package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

public interface ListObserver<L> extends Observer {
    void handleSuccess(List<L> items, boolean hasMorePages);
}
