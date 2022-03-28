package edu.byu.cs.tweeter.model.net.response;

public class FollowingCountResponse extends Response{
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

    public FollowingCountResponse(int count) {
        super(true);
        this.count = count;
    }

    public FollowingCountResponse(String message) {
        super(false, message);
    }
}
