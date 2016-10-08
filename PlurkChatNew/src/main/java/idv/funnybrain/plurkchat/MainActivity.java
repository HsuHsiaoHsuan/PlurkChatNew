package idv.funnybrain.plurkchat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONObject;
import org.scribe.model.Token;

import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.asynctask.Async_GetAccessToken;
import idv.funnybrain.plurkchat.asynctask.Async_Login;
import idv.funnybrain.plurkchat.asynctask.Async_Users_Me;
import idv.funnybrain.plurkchat.asynctask.Async_checkToken;
import idv.funnybrain.plurkchat.data.Language;
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.data.Qualifier;
import idv.funnybrain.plurkchat.eventbus.Event_Error;
import idv.funnybrain.plurkchat.eventbus.Event_GetAccessToken;
import idv.funnybrain.plurkchat.eventbus.Event_Login;
import idv.funnybrain.plurkchat.eventbus.Event_Users_Me;
import idv.funnybrain.plurkchat.eventbus.Event_checkToken;
import idv.funnybrain.plurkchat.modules.Mod_Timeline;
import idv.funnybrain.plurkchat.ui.ChatRoomsFragment_v2;
import idv.funnybrain.plurkchat.ui.MeFriendsFollowingFragment;

public class MainActivity extends AppCompatActivity {
    // ---- constant START ----
    static final boolean D = false;
    static final String TAG = "MainActivity";

    final static int HANDLER_SHOW_AUTH_URL = 0;
    final static int HANDLER_GET_ACCESS_TOKEN_OK = HANDLER_SHOW_AUTH_URL + 1;
    final static int HANDLER_GET_SELF_OK = HANDLER_GET_ACCESS_TOKEN_OK +1;
    // ---- constant END ----

    // ---- local variable START ----
    private Token accessToken;

    public static Me me;
    // ---- local variable END ----
    //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (D) { Log.d(TAG, "onCreate"); }
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        EventBus.getDefault().register(this);

        SharedPreferences pref = getSharedPreferences("accessToken", Context.MODE_PRIVATE);
        String key = pref.getString("key", "nothing");
        String secret = pref.getString("secret", "nothing");
        if (D) { Log.d(TAG, "key, secret: " + key + ", " + secret); }
        if( (!key.equals("nothing")) && (!secret.equals("nothing")) ) {

            accessToken = new Token(key, secret);
            DataCentral.getInstance(this).setPlurkOAuth(new PlurkOAuth(accessToken));

            new Async_checkToken(MainActivity.this).execute("");

            if(savedInstanceState != null) {
                getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
                // FIXME
                // rotation, setRetainInstance?
            }
        } else {
            configLoginView();
        }
    }

    private void configLoginView() {
        Button bt_getAuth = (Button) findViewById(R.id.bt_getAuth);
        bt_getAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async_Login(MainActivity.this).execute("");
            }
        });
        new Async_Login(MainActivity.this).execute("");

        final WebView webView = (WebView) findViewById(R.id.wv_auth_url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new PlurkAuthWebViewClient());

        findViewById(R.id.login_control).setVisibility(View.VISIBLE);
    }

    public void onEventMainThread(Event_checkToken event) {
        JSONObject result = event.getData();
        new Async_Users_Me(this).execute("");
//        System.out.println("funnyactiviyt checkToken: " + result);
    }

    public void onEventMainThread(Event_Error event) {
        if (event.getData().contains("invalid access token")) {
            SharedPreferences pref = getSharedPreferences("accessToken", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            configLoginView();
            findViewById(R.id.login_control).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(D) Log.d(TAG, "onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        DataCentral.getInstance(this).clearLruCache();
    }

    private void toast(Object msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    public void doTest() {
        new PlurkTmpAsyncTask().execute("");
    }

    // ---- EventBus onEvent START ----
    public void onEventMainThread(Event_Login event) {
        if (D) { Log.d(TAG, "Event_Login"); };
        WebView webView = (WebView) findViewById(R.id.wv_auth_url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new PlurkAuthWebViewClient());
        webView.loadUrl(event.getPlurkOAuth().getAuthURL());
        DataCentral.getInstance(this).setPlurkOAuth(event.getPlurkOAuth());
    }

    public void onEventMainThread(Event_GetAccessToken event) {
        if (D) { Log.d(TAG, "Event_GetAccessToken"); };
        new Async_Users_Me(this).execute("");
    }

    public void onEventMainThread(Event_Users_Me event) {
        if (D) { Log.d(TAG, "Event_Users_Me"); };
//        if(D) { Log.d(TAG, "HANDLER_GET_SELF_OK: " + me.getDisplay_name()); }
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // actionBar.setDisplayShowTitleEnabled(true);
        findViewById(R.id.fragment_content).setVisibility(View.VISIBLE);
        findViewById(R.id.login_control).setVisibility(View.GONE);

        actionBar.addTab(
                actionBar.newTab()
                        // .setText(R.string.tab_friends)
                        .setTabListener(new TabListener<MeFriendsFollowingFragment>(
                                this, "me_friend_following", MeFriendsFollowingFragment.class
                        ))
                        .setIcon(R.drawable.ic_launcher_v1)
        );

        actionBar.addTab(
                actionBar.newTab()
                        // .setText(R.string.tab_chatrooms)
                        .setTabListener(new TabListener<ChatRoomsFragment_v2>(
                                this, "rooms", ChatRoomsFragment_v2.class
                        ))
                        .setIcon(R.drawable.ic_launcher_v1)
        );
    }
    // ---- EventBus onEvent END ----

    // ---- inner class START ----
    private class PlurkAuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (D) { Log.d(TAG, "====> WebViewClient.shouldOverrideURL.url=" + url); }

        if (url.startsWith("http://localhost/auth")) {
            Uri uri = Uri.parse(url);
            String code = uri.getQueryParameter("oauth_verifier");
            Async_GetAccessToken async = new Async_GetAccessToken(MainActivity.this);
            async.execute(code);
            return true;
        }
        return false;
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

    private static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment fragment;
        private final Activity activity;
        private final String tag;
        private final Class<T> clz;
        private final Bundle args;

        private TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        private TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
            this.activity = activity;
            this.tag = tag;
            this.clz = clz;
            this.args = args;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if(fragment == null) {
                fragment = Fragment.instantiate(activity, clz.getName(), args);
                //ft.add(android.R.id.content, fragment, tag);
                ft.add(R.id.fragment_content, fragment, tag);
            } else {
                ft.attach(fragment);
            }
            tab.setIcon(R.drawable.ic_launcher);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if(fragment != null) {
                ft.detach(fragment);
                tab.setIcon(R.drawable.ic_launcher_v1);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }
    // ---- inner class END----
}