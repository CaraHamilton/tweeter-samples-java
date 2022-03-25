package edu.byu.cs.tweeter.client.model.net;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;

public class ServerFacadeTest extends TestCase {

    private static final String FEMALE_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png";

    private ServerFacade serverFacadeSpy;
    private FollowersRequest followersRequest;
    private RegisterRequest registerRequest;
    private FollowersCountRequest followersCountRequest;
    private AuthToken fakeAuthToken;
    private User fakeUser;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        fakeAuthToken = new AuthToken("abc-123-xyz-789", "March 20, 2022 3:01 PM");
        fakeUser = new User("Mary", "Jane", "@Mary_Jane", FEMALE_IMAGE_URL);
        serverFacadeSpy = Mockito.spy(ServerFacade.class);
        followersRequest = new FollowersRequest(fakeAuthToken, "@test-alias", 10, "@last-follower");
        registerRequest = new RegisterRequest("Mary", "Jane", FEMALE_IMAGE_URL, "@Mary_Jane", "1234");
        followersCountRequest = new FollowersCountRequest(fakeAuthToken, fakeUser);
    }

    @Test
    public void testGetFollowers() throws IOException, TweeterRemoteException {
        FollowersResponse response = serverFacadeSpy.getFollowers(followersRequest, "getfollowers");
        assertNotNull(response);
        assertEquals(response.getFollowers().size(), 10);
    }

    public void testRegister() throws IOException, TweeterRemoteException {
        RegisterResponse response = serverFacadeSpy.register(registerRequest, "register");
        assertNotNull(response.getAuthToken());
        assertNotNull(response.getUser());
    }

    public void testGetFollowersCount() throws IOException, TweeterRemoteException {
        FollowersCountResponse response = serverFacadeSpy.getFollowersCount(followersCountRequest, "getfollowerscount");
        assertEquals(response.getCount(), 20);
    }
}