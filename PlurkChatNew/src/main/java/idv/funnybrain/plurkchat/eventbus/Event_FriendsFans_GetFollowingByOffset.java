package idv.funnybrain.plurkchat.eventbus;

import java.util.List;

import idv.funnybrain.plurkchat.model.IHuman;

/**
 * Created by freeman on 2014/7/28.
 */
public class Event_FriendsFans_GetFollowingByOffset {
    private List<IHuman> data;

    public Event_FriendsFans_GetFollowingByOffset(List<IHuman> data) {
        this.data = data;
    }

    public List<IHuman> getData() {
        return this.data;
    }
}
