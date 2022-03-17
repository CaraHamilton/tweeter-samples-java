package edu.byu.cs.tweeter.client.presenter;

import android.widget.EditText;
import android.widget.ImageView;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends Presenter<RegisterPresenter.View> {

    public interface View extends ViewFragment {}

    private UserService userService;

    public RegisterPresenter(View view) {
        super(view);
        userService = new UserService();
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        // Send register request.
        userService.register(firstName, lastName, alias, password, imageBytesBase64, new RegisterObserver());
    }

    public void validateRegistration(EditText firstName, EditText lastName, EditText alias, EditText password, ImageView imageToUpload) {
        if (firstName.getText().length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.getText().length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.getText().length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    public class RegisterObserver implements UserService.RegisterObserver {

        @Override
        public void handleSuccess(User registeredUser, AuthToken authToken) {
            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            view.startNewActivity(registeredUser);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to register: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to register because of exception: " + exception.getMessage());
        }
    }
}
