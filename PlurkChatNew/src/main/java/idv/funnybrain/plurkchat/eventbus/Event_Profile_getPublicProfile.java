package idv.funnybrain.plurkchat.eventbus;

/**
 * Created by freeman on 2014/12/18.
 */
public class Event_Profile_getPublicProfile {
    private Object data;

    public Event_Profile_getPublicProfile(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }
}
