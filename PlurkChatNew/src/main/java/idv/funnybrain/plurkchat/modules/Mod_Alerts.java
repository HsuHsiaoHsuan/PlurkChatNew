package idv.funnybrain.plurkchat.modules;

/**
 * Created by Freeman on 2014/4/2.
 */
public class Mod_Alerts extends AbstractModule {

    // Return a JSON list of current active alerts.
    public void getActive() {

    }

    // Return a JSON list of past 30 alerts.
    public void getHistory() {

    }

    // Accept user_id as fan.
    public void addAsFan() {

    }

    // Accept all friendship requests as fans.
    public void addAllAsFan() {

    }

    // Accept all friendship requests as friends.
    public void addAllAsFriends() {

    }

    // Accept user_id as friend.
    public void addAsFriend() {

    }

    // Deny friendship to user_id.
    public void denyFriendship() {

    }

    // Remove notification to user with id user_id.
    public void removeNotification() {

    }

    @Override
    protected String getModulePath() {
        return "/APP/Alerts";
    }
}
