package idv.funnybrain.plurkchat.modules;

/**
 * Created by Freeman on 2014/4/2.
 */
public class Mod_Responses extends AbstractModule {

    // Fetches responses for plurk with plurk_id and some basic info about the users.
    public void get() {

    }

    // Adds a responses to plurk_id. Language is inherited from the plurk.
    public void responseAdd() {

    }

    // Deletes a response. A user can delete own responses or responses that are posted to own plurks.
    public void responseDelete() {

    }

    @Override
    protected String getModulePath() {
        return "/APP/Response";
    }
}
