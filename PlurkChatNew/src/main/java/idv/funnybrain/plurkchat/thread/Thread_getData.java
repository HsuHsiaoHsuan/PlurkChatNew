package idv.funnybrain.plurkchat.thread;

import android.os.Handler;
import android.os.Looper;

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

public class Thread_getData extends Thread {
    private static final String TAG = "Thread_getData";
    private static final boolean D = BuildConfig.DEBUG;

    private Handler handler;

    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler();
        Looper.loop();
    }

    public void getMe() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                JSONObject result = null;
                try {
                    result = DataCentral.getInstance().getPlurkOAuth().getModule(Mod_Users.class).me();
                    if(D) { Logger.json(result.toString()); }
                } catch (RequestException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new Event_Error(e.toString()));
                }
                if(result != null) {
                    try {
                        EventBus.getDefault().post(new Event_Users_Me(new Me(result)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    EventBus.getDefault().post(new Event_Error("get Empty Me Error"));
                }
            }
        });
    }

    public void exit() {
        handler.getLooper().quit();
    }
}
