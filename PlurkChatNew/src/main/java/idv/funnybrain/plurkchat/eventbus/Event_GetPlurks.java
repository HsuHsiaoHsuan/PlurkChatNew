package idv.funnybrain.plurkchat.eventbus;

import org.json.JSONObject;

/**
 * Created by freeman on 2014/7/28.
 */
public class Event_GetPlurks {
    private final JSONObject data;

    public Event_GetPlurks(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return this.data;
    }
}
