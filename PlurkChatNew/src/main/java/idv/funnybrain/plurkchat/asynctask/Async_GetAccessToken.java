package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.eventbus.Event_GetAccessToken;
import idv.funnybrain.plurkchat.logger.Log;

public class Async_GetAccessToken extends AsyncTask<String, Void, Boolean> {
    private Context mContext;

    public Async_GetAccessToken(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (DataCentral.getInstance().getPlurkOAuth() == null) {
            Log.e("FREEMAN", "NULL1");
        }
        Log.e("FREEMAN", "params: " + params[0]);

        return DataCentral.getInstance(mContext).getPlurkOAuth().setAccessToken(mContext, params[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(aBoolean) {
            EventBus.getDefault().post(new Event_GetAccessToken());
        }
    }
}