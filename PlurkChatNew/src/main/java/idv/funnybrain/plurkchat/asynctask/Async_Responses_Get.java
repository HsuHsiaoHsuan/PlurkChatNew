package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.eventbus.Event_Responses_Get;
import idv.funnybrain.plurkchat.modules.Mod_Responses;
import org.json.JSONObject;

/**
 * Created by freeman on 2014/11/15.
 */
public class Async_Responses_Get extends AsyncTaskLoader<Void> {
    String original_post_id;

    public Async_Responses_Get(Context context, String input) {
        super(context);
        original_post_id = input;
    }

    @Override
    public Void loadInBackground() {
        JSONObject result = null;
        try {
            result = DataCentral.getInstance(getContext()).getPlurkOAuth().getModule(Mod_Responses.class)
                     .get(original_post_id, 0);
            EventBus.getDefault().post(new Event_Responses_Get(result));
        } catch (RequestException e) {
            e.printStackTrace();
        }
        //return result;
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
