package idv.funnybrain.plurkchat.eventbus;

import android.util.JsonReader;
import org.json.JSONObject;

/**
 * Created by freeman on 2014/11/16.
 */
public class Event_Error {
    String data;

    public Event_Error(String input) {
        data = input;
    }

    public String getData() {
        return data;
    }
}
