package edu.byu.cs.tweeter.client.model.service;

public interface Observer {

    void handleFailure(String message);
    void handleException(Exception exception);

}
