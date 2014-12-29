package idv.funnybrain.plurkchat.eventbus;

import idv.funnybrain.plurkchat.data.IHuman;

import java.util.List;

/**
 * Created by freeman on 2014/7/28.
 */
public class Event_FriendsFans_GetFriendsByOffset {
    private List<IHuman> data;

    public Event_FriendsFans_GetFriendsByOffset(List<IHuman> data) {
        this.data = data;
    }

    public List<IHuman> getData() {
        return this.data;
    }
}
