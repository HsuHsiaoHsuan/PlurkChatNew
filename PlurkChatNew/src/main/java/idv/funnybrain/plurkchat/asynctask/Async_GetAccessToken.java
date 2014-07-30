package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.eventbus.Event_GetAccessToken;

/**
 * Created by freeman on 2014/7/16.
 */
public class Async_GetAccessToken extends AsyncTask<String, Void, Boolean> {
    private Context mContext;

    public Async_GetAccessToken(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... params) {
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