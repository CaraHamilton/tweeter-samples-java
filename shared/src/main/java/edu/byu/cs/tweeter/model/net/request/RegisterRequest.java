package edu.byu.cs.tweeter.model.net.request;

public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String image;
    private String username;
    private String password;

    public RegisterRequest(String firstName, String lastName, String image, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.username = username;
        this.password = password;
    }

    public RegisterRequest() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
