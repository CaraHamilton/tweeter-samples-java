package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.User;

public interface ViewFragment extends ViewBase {
    void startNewActivity(User user);
}
