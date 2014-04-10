package idv.funnybrain.plurkchat.modules;

/**
 * Created by Freeman on 2014/4/2.
 */
public class Mod_Polling extends AbstractModule {

    // You should use this call to find out if there any new plurks posted to the user's timeline.
    // It's much more efficient than doing it with /APP/Timeline/getPlurks, so please use it :)
    public void getPlurks() {

    }

    // Use this call to find out if there are unread plurks on a user's timeline.
    public void getUnreadCount() {

    }

    @Override
    protected String getModulePath() {
        return "/APP/Polling";
    }
}
