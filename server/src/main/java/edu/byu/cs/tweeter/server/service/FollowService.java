package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public FollowingResponse getFollowees(FollowingRequest request) {
        if(request.getFollowerAlias() == null) { //do you need to check for auth token
            throw new RuntimeException("[BadRequest] Request needs to have a follower alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request needs to have a positive limit");
        }
        return getFollowingDAO().getFollowees(request);
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }


    public FollowersResponse getFollowers(FollowersRequest request) {
        if(request.getFolloweeAlias() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a followee alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request needs to have a positive limit");
        }
        return getFollowersDAO().getFollowers(request);
    }

    FollowDAO getFollowersDAO() { return new FollowDAO(); }

    public FollowResponse follow(FollowRequest request) {
        if(request.getFollowee() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a followee");
        }
        return new FollowResponse();
    }

    public UnfollowResponse unfollow(UnfollowRequest request) {
        if(request.getFollowee() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a followee");
        }
        return new UnfollowResponse();
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if(request.getFolloweeAlias() == null) { //do you need to check for auth token
            throw new RuntimeException("[BadRequest] Request needs to have a followee alias");
        } else if(request.getFollowerAlias() == null) {
            throw new RuntimeException("[BadRequest] Request needs to have a follower alias");
        }
        return getFollowingDAO().isFollower(request);
    }

    public FollowersCountResponse getFollowersCount(FollowersCountRequest request) {
        if(request.getUser() == null) { //do you need to check for auth token
            throw new RuntimeException("[BadRequest] Request needs to have a user alias");
        }
        int count = getFollowingDAO().getFollowersCount(request.getUser());
        FollowersCountResponse response = new FollowersCountResponse(count);
        return response;
    }

    public FollowingCountResponse getFollowingCount(FollowingCountRequest request) {
        if(request.getUser() == null) { //do you need to check for auth token
            throw new RuntimeException("[BadRequest] Request needs to have a user alias");
        }
        int count = getFollowingDAO().getFolloweeCount(request.getUser());
        FollowingCountResponse response = new FollowingCountResponse(count);
        return response;
    }
}
