package idv.funnybrain.plurkchat.asynctask;

import android.os.AsyncTask;
import android.util.Log;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.eventbus.Event_Users_Me;
import idv.funnybrain.plurkchat.modules.Mod_Users;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by freeman on 2014/7/17.
 */
public class Async_Users_Me extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "Async_Users_Me";
    private static final boolean D = false;

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject result = null;

        try {
            // result = plurkOAuth.getModule(Mod_Users.class).me();
            result = DataCentral.getInstance().getPlurkOAuth().getModule(Mod_Users.class).me();
            if(D) { Log.d(TAG, "Mod_Users_me_AsyncTask: " + result.toString()); }
        } catch (RequestException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject object) {
        super.onPostExecute(object);
        if(object != null) {
            try {
                DataCentral.getInstance().setMe(new Me(object));
                // me = new Me(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new Event_Users_Me());
//            Message msg = new Message();
//            msg.what = HANDLER_GET_SELF_OK;
//            handler.sendMessage(msg);
        }
    }
}
