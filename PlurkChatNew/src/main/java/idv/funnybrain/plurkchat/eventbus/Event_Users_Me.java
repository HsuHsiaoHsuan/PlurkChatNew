package idv.funnybrain.plurkchat.eventbus;

import idv.funnybrain.plurkchat.model.Me;

public class Event_Users_Me {
    private final Me me;
    public Event_Users_Me(Me me) {
        this.me = me;
    }

    public Me getMe() {
        return me;
    }
}
