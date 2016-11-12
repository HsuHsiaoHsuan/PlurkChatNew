package idv.funnybrain.plurkchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.scribe.model.Token;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.asynctask.Async_Users_Me;
import idv.funnybrain.plurkchat.asynctask.Async_checkToken;
import idv.funnybrain.plurkchat.eventbus.Event_Error;
import idv.funnybrain.plurkchat.eventbus.Event_Users_Me;
import idv.funnybrain.plurkchat.eventbus.Event_checkToken;
import idv.funnybrain.plurkchat.model.Language;
import idv.funnybrain.plurkchat.model.Qualifier;
import idv.funnybrain.plurkchat.modules.Mod_Timeline;
import idv.funnybrain.plurkchat.view.MainActivityAdapter;

//import idv.funnybrain.plurkchat.thread.Thread_getData;

/**
 * 1. start login
 * 2. after WebVew set token, notify ok to accee ME
 * 3. get Me
 */
public class MainActivity extends AppCompatActivity {
    static final boolean D = BuildConfig.DEBUG;
    static final String TAG = "MainActivity";

    private static final int HANDLER_SHOW_AUTH_URL = 0;
    private static final int HANDLER_GET_ACCESS_TOKEN_OK = HANDLER_SHOW_AUTH_URL + 1;
    private static final int HANDLER_GET_SELF_OK = HANDLER_GET_ACCESS_TOKEN_OK +1;

    private static final int INTENT_LOGIN = 0;

    private MainActivityAdapter adapter;
    private String[] titles;

    @BindView(R.id.pager) ViewPager pager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // set translucent status/toolbar bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        SharedPreferences pref = getSharedPreferences("accessToken", Context.MODE_PRIVATE);
        String key = pref.getString("key", "nothing");
        String secret = pref.getString("secret", "nothing");
        if (D) { Log.d(TAG, "key, secret: " + key + ", " + secret); }
        if( (!key.equals("nothing")) && (!secret.equals("nothing")) ) {
            DataCentral.getInstance().setPlurkOAuth(new PlurkOAuth(new Token(key, secret)));

            new Async_checkToken().execute("");
        } else {
            configLoginView();
        }

        titles = new String[] {
                getString(R.string.tab_friends),
                getString(R.string.tab_friends),
                getString(R.string.tab_friends)
        };
    }

    private void configLoginView() {
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        startActivityForResult(intent, INTENT_LOGIN);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataCentral.getInstance().clearLruCache();
    }

    private void toast(Object msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    public void doTest() {
        new PlurkTmpAsyncTask().execute("");
    }

    public void doGetMe() {
        new Async_Users_Me().execute("");
    }

    // if checkToken goes here, it's OK! we can start to load data.
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event_checkToken event) {
        JSONObject result = event.getData();
        if (D) {
            Logger.json(result.toString());
        }
        doGetMe();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event_Users_Me event) {
        DataCentral.getInstance().setMe(event.getMe());
        adapter = new MainActivityAdapter(getSupportFragmentManager(), titles);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        tabLayout.setupWithViewPager(pager, true);
//        setRetainInstance(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event_Error event) {
        if (event.getData().contains("invalid access token")) {
            SharedPreferences pref = getSharedPreferences("accessToken", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            configLoginView();
        }
    }

    private class PlurkTmpAsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject result = null;
            try {
                result = DataCentral.getInstance(MainActivity.this).getPlurkOAuth().getModule(Mod_Timeline.class)
                        .plurkAdd("(wave)(wave)(wave)"+ Math.random(), Qualifier.SAYS, null, 0, Language.TR_CH);
            } catch (RequestException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}