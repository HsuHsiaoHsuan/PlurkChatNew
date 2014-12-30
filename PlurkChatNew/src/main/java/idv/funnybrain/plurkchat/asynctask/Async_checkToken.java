package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.util.Log;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.eventbus.Event_Error;
import idv.funnybrain.plurkchat.eventbus.Event_checkToken;
import idv.funnybrain.plurkchat.modules.Mod_checkToken;
import idv.funnybrain.plurkchat.utils.AsyncTask;
import org.json.JSONObject;

/**
 * Created by freeman on 2014/12/31.
 */
public class Async_checkToken extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "Async_checkToken";
    private static final boolean D = false;

    private Context mContext;

    public Async_checkToken(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject result = null;

        try {
            result = DataCentral.getInstance(mContext).getPlurkOAuth().getModule(Mod_checkToken.class).checkToken();
            if(D) { Log.d(TAG, "Mod_checkToken: " + result.toString()); }
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
//            try {
//                DataCentral.getInstance(mContext)
//            }
            EventBus.getDefault().post(new Event_checkToken(object));
        }
    }
}
