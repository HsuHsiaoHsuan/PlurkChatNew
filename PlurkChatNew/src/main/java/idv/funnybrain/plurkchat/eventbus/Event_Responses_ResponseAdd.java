package idv.funnybrain.plurkchat.eventbus;

import org.json.JSONObject;

/**
 * Created by freeman on 2014/11/16.
 */
public class Event_Responses_ResponseAdd {
    private final JSONObject data;

    public Event_Responses_ResponseAdd(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return this.data;
    }
}
