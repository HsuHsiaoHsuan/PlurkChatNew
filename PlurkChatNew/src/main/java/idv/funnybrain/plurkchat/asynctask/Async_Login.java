package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.PlurkOAuth;
import idv.funnybrain.plurkchat.eventbus.Event_Login;

/**
 * Created by freeman on 2014/7/16.
 */
public class Async_Login extends AsyncTask<String, Void, String> {
    private Context mContext;

    public Async_Login(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... params) {

        PlurkOAuth plurkOAuth = new PlurkOAuth("http://localhost/auth");

//        Event_Login event = new Event_Login(new PlurkOAuth("http://localhost/auth"));
        Event_Login event = new Event_Login(plurkOAuth);

        EventBus.getDefault().post(event);

        DataCentral.getInstance(mContext).setPlurkOAuth(plurkOAuth);

        return "";
    }
}
