package idv.funnybrain.plurkchat.eventbus;

import org.json.JSONObject;

/**
 * Created by freeman on 2014/12/31.
 */
public class Event_checkToken {
    private final JSONObject data;


    public Event_checkToken(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return this.data;
    }
}
