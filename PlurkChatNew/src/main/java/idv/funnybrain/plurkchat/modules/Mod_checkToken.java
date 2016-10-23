package idv.funnybrain.plurkchat.modules;

import org.json.JSONObject;

import idv.funnybrain.plurkchat.RequestException;

/**
 * Created by freeman on 2014/12/31.
 */
public class Mod_checkToken extends AbstractModule {

    public JSONObject checkToken() throws RequestException {
        JSONObject result = requestAPI("").args(null).getJSONObjectResult();
        return result;
    }

    @Override
    protected String getModulePath() {
        return "/APP/checkToken";
    }
}
