package idv.funnybrain.plurkchat.modules;

/**
 * Created by Freeman on 2014/4/2.
 */
public class Mod_UserSearch extends AbstractModule {

    // Returns 10 users that match query, users are sorted by karma.
    public void search() {

    }

    @Override
    protected String getModulePath() {
        return "/APP/UserSearch";
    }
}
