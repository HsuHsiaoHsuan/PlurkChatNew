package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Qualifier;
import idv.funnybrain.plurkchat.eventbus.Event_Error;
import idv.funnybrain.plurkchat.eventbus.Event_Responses_ResponseAdd;
import idv.funnybrain.plurkchat.modules.Mod_Responses;

/**
 * Created by freeman on 2014/11/16.
 */
public class Async_Responses_ResponseAdd extends AsyncTaskLoader<Void> {
    String id;
    String content;
    Qualifier qualifier;

    public Async_Responses_ResponseAdd(Context context, String plurk_id,String plurk_content, Qualifier plurk_qualifier) {
        super(context);
        id = plurk_id;
        content = plurk_content;
        qualifier = plurk_qualifier;
    }

    @Override
    public Void loadInBackground() {
        JSONObject result = null;

        try {
            result = DataCentral.getInstance(getContext()).getPlurkOAuth().getModule(Mod_Responses.class)
                     .responseAdd(id, content, qualifier);
            EventBus.getDefault().post(new Event_Responses_ResponseAdd(result));
        } catch (RequestException e) {
            EventBus.getDefault().post(new Event_Error(e.getMessage()));
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deliverResult(Void data) {
        if(isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    public void onCanceled(Void data) {
        super.onCanceled(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
    }
}
