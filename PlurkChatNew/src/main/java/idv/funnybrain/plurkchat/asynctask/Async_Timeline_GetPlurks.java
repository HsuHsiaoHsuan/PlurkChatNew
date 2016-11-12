package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.HashMap;

import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.eventbus.Event_Timeline_GetPlurks;
import idv.funnybrain.plurkchat.modules.Mod_Timeline;
import idv.funnybrain.plurkchat.view.ChatRoomsFragment_v2;

/**
 * Created by freeman on 2014/7/28.
 */
public class Async_Timeline_GetPlurks extends AsyncTaskLoader<Void> {
    // private static final boolean D = false;
    private static final String TAG = "Async_Timeline_GetPlurks";

    HashMap<String, String> params;

    public Async_Timeline_GetPlurks(Context context, HashMap<String, String> params) {
        super(context);
        this.params = params;
    }

    @Override
    public Void loadInBackground() {
        JSONObject result = null;

        String offset = null;
        int limit = 30;
        String filter = null;
        boolean favorers_detail = false;
        boolean limited_detail = false;
        boolean replurkers_detail = false;

        if(params.containsKey(ChatRoomsFragment_v2.OFFSET)) {
            offset = params.get(ChatRoomsFragment_v2.OFFSET);
        }
        if(params.containsKey(ChatRoomsFragment_v2.LIMIT)) {
            String tmp = params.get(ChatRoomsFragment_v2.LIMIT);
            limit = Integer.valueOf(tmp);
        }
        if(params.containsKey(ChatRoomsFragment_v2.FILTER)) {
            filter = params.get(ChatRoomsFragment_v2.FILTER);
        }
        if(params.containsKey(ChatRoomsFragment_v2.FAVORERS_DETAIL)) {
            String tmp = params.get(ChatRoomsFragment_v2.FAVORERS_DETAIL);
            if(tmp.equals("true")) { favorers_detail = true; }
        }
        if(params.containsKey(ChatRoomsFragment_v2.LIMITED_DETAIL)) {
            String tmp = params.get(ChatRoomsFragment_v2.LIMITED_DETAIL);
            if(tmp.equals("true")) { limited_detail = true; }
        }
        if(params.containsKey(ChatRoomsFragment_v2.REPLURKERS_DETAIL)) {
            String tmp = params.get(ChatRoomsFragment_v2.REPLURKERS_DETAIL);
            if(tmp.equals("true")) { replurkers_detail = true; }
        }

        try {
            result = DataCentral.getInstance(getContext()).getPlurkOAuth().getModule(Mod_Timeline.class)
                    .getPlurks(offset, limit, filter, favorers_detail, limited_detail, replurkers_detail);

            EventBus.getDefault().post(new Event_Timeline_GetPlurks(result));
        } catch (RequestException e) {
            Logger.e(e.getMessage());
            // e.printStackTrace();
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
