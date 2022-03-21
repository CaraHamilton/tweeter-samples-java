package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FeedRequest {

    private AuthToken authToken;
    private String userAlias;
    private int limit;
    private String lastStatusPost;

    public FeedRequest() { }

    public FeedRequest(AuthToken authToken, String userAlias, int limit, String lastStatusPost) {
        this.authToken = authToken;
        this.userAlias = userAlias;
        this.limit = limit;
        this.lastStatusPost = lastStatusPost;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLastStatusPost() {
        return lastStatusPost;
    }

    public void setLastStatusPost(String lastStatusPost) {
        this.lastStatusPost = lastStatusPost;
    }
}
