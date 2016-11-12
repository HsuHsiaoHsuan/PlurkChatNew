package idv.funnybrain.plurkchat.eventbus;

import java.util.HashMap;
import java.util.List;

import idv.funnybrain.plurkchat.model.Friend;
import idv.funnybrain.plurkchat.model.Responses;

/**
 * Created by freeman on 2014/11/15.
 */
public class Event_Responses_Get {
    //private final JSONObject data;
    private final List<Responses> responses;
    private final HashMap<String, Friend> friends;

    public Event_Responses_Get(List<Responses> responses, HashMap<String, Friend> friends) {
        //this.data = data;
        this.responses = responses;
        this.friends = friends;
    }

    public List<Responses> getResponsesData() {
        return this.responses;
    }

    public HashMap<String, Friend> getFriendsData() {
        return this.friends;
    }
}
