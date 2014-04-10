package idv.funnybrain.plurkchat.modules;

/**
 * Created by Freeman on 2014/4/2.
 */
public class Mod_Emoticons extends AbstractModule {

    // Emoticons are a big part of Plurk since they make it easy to express feelings.
    // Check out current Plurk emoticons. http://www.plurk.com/Help/extraSmilies
    // This call returns a JSON object that looks like:
    public void get() {

    }

    @Override
    protected String getModulePath() {
        return "/APP/Emoticons";
    }
}
