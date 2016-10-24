package idv.funnybrain.plurkchat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;
import org.scribe.model.Token;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.asynctask.Async_GetAccessToken;
import idv.funnybrain.plurkchat.asynctask.Async_Users_Me;
import idv.funnybrain.plurkchat.asynctask.Async_checkToken;
import idv.funnybrain.plurkchat.data.Language;
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.data.Qualifier;
import idv.funnybrain.plurkchat.eventbus.Event_Error;
import idv.funnybrain.plurkchat.eventbus.Event_GetAccessToken;
import idv.funnybrain.plurkchat.eventbus.Event_Users_Me;
import idv.funnybrain.plurkchat.eventbus.Event_checkToken;
import idv.funnybrain.plurkchat.modules.Mod_Timeline;
import idv.funnybrain.plurkchat.ui.MainActivityAdapter;

/**
 * 1. start login
 * 2. after WebVew set token, notify ok to accee ME
 * 3. get Me
 */
public class MainActivity extends AppCompatActivity {
    static final boolean D = true;
    static final String TAG = "MainActivity";

    public static Me me;
    final static int HANDLER_SHOW_AUTH_URL = 0;
    final static int HANDLER_GET_ACCESS_TOKEN_OK = HANDLER_SHOW_AUTH_URL + 1;
    final static int HANDLER_GET_SELF_OK = HANDLER_GET_ACCESS_TOKEN_OK +1;

    private static final int INTENT_LOGIN = 0;

    private MainActivityAdapter adapter;
    private String[] titles;

    @BindView(R.id.pager) ViewPager pager;

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
        titles = new String[] {
                getString(R.string.tab_friends)
        };

        SharedPreferences pref = getSharedPreferences("accessToken", Context.MODE_PRIVATE);
        String key = pref.getString("key", "nothing");
        String secret = pref.getString("secret", "nothing");
        if (D) { Log.d(TAG, "key, secret: " + key + ", " + secret); }
        if( (!key.equals("nothing")) && (!secret.equals("nothing")) ) {
            DataCentral.getInstance().setPlurkOAuth(new PlurkOAuth(new Token(key, secret)));
            new Async_checkToken().execute("");
            if(savedInstanceState != null) {
//                getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
                // FIXME
                // rotation, setRetainInstance?
            }
        } else {
            configLoginView();
        }
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
    protected void onResume() {
        super.onResume();
        if(D) Log.d(TAG, "onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DataCentral.getInstance().clearLruCache();
    }

    private void toast(Object msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    public void doTest() {
        new PlurkTmpAsyncTask().execute("");
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(Event_GetAccessToken event) {
//        if (D) { Log.d(TAG, "Event_GetAccessToken"); };
//        new Async_Users_Me().execute("");
//    }

    // if checkToken goes here, it's OK! we can start to load data.
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event_checkToken event) {
        JSONObject result = event.getData();
        if (D) {
            Log.e(TAG, "checkToken OK! " + result.toString());
        }

        adapter = new MainActivityAdapter(getSupportFragmentManager(), titles);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);

//        new Async_Users_Me().execute("");
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