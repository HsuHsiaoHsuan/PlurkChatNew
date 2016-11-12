package idv.funnybrain.plurkchat.asynctask;

import android.os.AsyncTask;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.BuildConfig;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.eventbus.Event_Error;
import idv.funnybrain.plurkchat.eventbus.Event_Users_Me;
import idv.funnybrain.plurkchat.model.Me;
import idv.funnybrain.plurkchat.modules.Mod_Users;

public class Async_Users_Me extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "Async_Users_Me";
    private static final boolean D = BuildConfig.DEBUG;

    public Async_Users_Me() {
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject result = null;

        try {
            result = DataCentral.getInstance().getPlurkOAuth().getModule(Mod_Users.class).me();
            if(D) { Logger.json(result.toString()); }
        } catch (RequestException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new Event_Error(e.toString()));
        }
        return result;
    }

    @Override
    protected void onPostExecute(JSONObject object) {
        super.onPostExecute(object);
        if(object != null) {
            try {
                DataCentral.getInstance().setMe(new Me(object));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                EventBus.getDefault().post(new Event_Users_Me(new Me(object)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
