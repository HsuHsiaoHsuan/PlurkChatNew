package idv.funnybrain.plurkchat.eventbus;

import org.json.JSONObject;

/**
 * Created by freeman on 2014/11/15.
 */
public class Event_Responses_Get {
    private final JSONObject data;

    public Event_Responses_Get(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return this.data;
    }
}
