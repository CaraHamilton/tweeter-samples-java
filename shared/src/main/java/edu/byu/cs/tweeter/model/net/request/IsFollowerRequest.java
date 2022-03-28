package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class IsFollowerRequest {
    private AuthToken authToken;
    private String followeeAlias;
    private String followerAlias;

    public IsFollowerRequest() {
    }

    public IsFollowerRequest(AuthToken authToken, String userAlias, String allegedFollowerAlias) {
        this.authToken = authToken;
        this.followeeAlias = userAlias;
        this.followerAlias = allegedFollowerAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }
}
