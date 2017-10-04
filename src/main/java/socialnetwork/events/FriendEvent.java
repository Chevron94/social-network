package socialnetwork.events;


import socialnetwork.entities.FriendRequest;

/**
 * Created by Роман on 27.09.2016.
 */
public class FriendEvent {
    FriendRequest friendRequest;

    public FriendEvent(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }
}
