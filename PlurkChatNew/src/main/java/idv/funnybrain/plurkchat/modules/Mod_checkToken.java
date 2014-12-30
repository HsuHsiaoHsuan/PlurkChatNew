package idv.funnybrain.plurkchat.modules;

import android.content.Context;
import idv.funnybrain.plurkchat.RequestException;
import org.json.JSONObject;

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
