package idv.funnybrain.plurkchat.modules;

import idv.funnybrain.plurkchat.PlurkOAuth;
import idv.funnybrain.plurkchat.RequestMaker;

/**
 * Created by Freeman on 2014/4/2.
 */
public abstract class AbstractModule {
    public static final String PLURK_PREFIX = "http://www.plurk.com";

    private PlurkOAuth plurkOAuth;

    public void setPlurkOAuth(PlurkOAuth oauth) { plurkOAuth = oauth; }

    protected abstract String getModulePath();

    protected RequestMaker requestAPI(String api) {
        if ( !api.equals("") ) {
            return new RequestMaker(plurkOAuth, PLURK_PREFIX + getModulePath() + "/" + api);
        } else {
            return new RequestMaker(plurkOAuth, PLURK_PREFIX + getModulePath());
        }
    }
}
