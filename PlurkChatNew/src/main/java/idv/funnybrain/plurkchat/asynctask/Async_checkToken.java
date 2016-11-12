package idv.funnybrain.plurkchat.asynctask;

import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.eventbus.Event_Error;
import idv.funnybrain.plurkchat.eventbus.Event_checkToken;
import idv.funnybrain.plurkchat.modules.Mod_checkToken;
import idv.funnybrain.plurkchat.utils.AsyncTask;

public class Async_checkToken extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "Async_checkToken";
    private static final boolean D = false;

    public Async_checkToken() {
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject result = null;

        try {
            result = DataCentral.getInstance().getPlurkOAuth().getModule(Mod_checkToken.class).checkToken();
            if(D) { Logger.d("Mod_checkToken: " + result.toString()); }
        } catch (RequestException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new Event_Error(e.toString()));
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject object) {
        super.onPostExecute(object);
        if (object != null) {
            EventBus.getDefault().post(new Event_checkToken(object));
        }
    }
}
