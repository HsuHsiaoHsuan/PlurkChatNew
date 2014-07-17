package idv.funnybrain.plurkchat.eventbus;

import idv.funnybrain.plurkchat.PlurkOAuth;

/**
 * Created by freeman on 2014/7/16.
 */
public class Event_Login {
    private final PlurkOAuth mPlurkOAuth;

    public Event_Login(PlurkOAuth plurkOAuth) {
        this.mPlurkOAuth = plurkOAuth;
    }

    public PlurkOAuth getPlurkOAuth() {
        return mPlurkOAuth;
    }
}
