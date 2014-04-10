package idv.funnybrain.plurkchat.modules;

/**
 * Created by Freeman on 2014/4/2.
 */
public class Mod_Profile extends AbstractModule {

    // Returns data that's private for the current user.
    // This can be used to construct a profile and render a timeline of the latest plurks.
    public void getOwnProfile() {

    }

    // Fetches public information such as a user's public plurks and basic information.
    // Fetches also if the current user is following the user, are friends with or is a fan.
    public void getPublicProfile() {

    }

    @Override
    protected String getModulePath() {
        return "/APP/Profile";
    }
}
