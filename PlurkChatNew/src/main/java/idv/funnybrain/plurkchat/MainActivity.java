package idv.funnybrain.plurkchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

    private final int MSG_GET_OAUTH = 0;
    private final int MSG_INIT_TOKEN = MSG_GET_OAUTH + 1;

    private Token accessToken;
    private Thread goLoginThread = new Thread(new Runnable() {
        @Override
        public void run() {
            PlurkOAuth plurkOAuth = new PlurkOAuth("http://localhost/auth");
            DataCentral.getInstance().setPlurkOAuth(plurkOAuth);
            Message msg = new Message();
            msg.what = MSG_GET_OAUTH;
            msg.obj = plurkOAuth;
            handler.sendMessage(msg);
        }
    });
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_OAUTH:
                    PlurkOAuth oauth = (PlurkOAuth) msg.obj;
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new PlurkAuthWebViewClient());
                    webView.loadUrl(oauth.getAuthURL());
                    viewLogin.setVisibility(View.VISIBLE);
                    DataCentral.getInstance().setPlurkOAuth(oauth);
                    break;
                case MSG_INIT_TOKEN:
                    new Async_Users_Me().execute("");
                    break;
            }
        }
    };


    @BindView(R.id.login_control) View viewLogin;
    @BindView(R.id.bt_getAuth) Button bt_getAuth;
    @BindView(R.id.wv_auth_url) WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SharedPreferences pref = getSharedPreferences("accessToken", Context.MODE_PRIVATE);
        String key = pref.getString("key", "nothing");
        String secret = pref.getString("secret", "nothing");
        if (D) { Log.d(TAG, "key, secret: " + key + ", " + secret); }
        if( (!key.equals("nothing")) && (!secret.equals("nothing")) ) {
            accessToken = new Token(key, secret);
            DataCentral.getInstance().setPlurkOAuth(new PlurkOAuth(accessToken));

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
        bt_getAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginThread.start();
            }
        });
        goLoginThread.start();
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
//        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event_checkToken event) {
        JSONObject result = event.getData();
        new Async_Users_Me().execute("");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event_Error event) {
        if (event.getData().contains("invalid access token")) {
            SharedPreferences pref = getSharedPreferences("accessToken", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();
            configLoginView();
            viewLogin.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event_Users_Me event) {
//        if (D) { Log.d(TAG, "Event_Users_Me"); };
//        ActionBar actionBar = getActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        // actionBar.setDisplayShowTitleEnabled(true);
//        findViewById(R.id.fragment_content).setVisibility(View.VISIBLE);
//        findViewById(R.id.login_control).setVisibility(View.GONE);
//
//        actionBar.addTab(
//                actionBar.newTab()
//                        // .setText(R.string.tab_friends)
//                        .setTabListener(new TabListener<MeFriendsFollowingFragment>(
//                                this, "me_friend_following", MeFriendsFollowingFragment.class
//                        ))
//                        .setIcon(R.drawable.ic_launcher_v1)
//        );
//
//        actionBar.addTab(
//                actionBar.newTab()
//                        // .setText(R.string.tab_chatrooms)
//                        .setTabListener(new TabListener<ChatRoomsFragment_v2>(
//                                this, "rooms", ChatRoomsFragment_v2.class
//                        ))
//                        .setIcon(R.drawable.ic_launcher_v1)
//        );
    }

    // ---- inner class START ----
    private class PlurkAuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (D) { Log.d(TAG, "====> WebViewClient.shouldOverrideURL.url=" + url); }

        if (url.startsWith("http://localhost/auth")) {
            Uri uri = Uri.parse(url);
            final String code = uri.getQueryParameter("oauth_verifier");

            // to get access token
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (DataCentral.getInstance().getPlurkOAuth().setAccessToken(MainActivity.this, code)) {
                        Message msg = new Message();
                        msg.what = MSG_INIT_TOKEN;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
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

//    private static class TabListener<T extends Fragment> implements ActionBar.TabListener {
//        private Fragment fragment;
//        private final Activity activity;
//        private final String tag;
//        private final Class<T> clz;
//        private final Bundle args;
//
//        private TabListener(Activity activity, String tag, Class<T> clz) {
//            this(activity, tag, clz, null);
//        }
//
//        private TabListener(Activity activity, String tag, Class<T> clz, Bundle args) {
//            this.activity = activity;
//            this.tag = tag;
//            this.clz = clz;
//            this.args = args;
//        }
//
//        @Override
//        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
//            if(fragment == null) {
//                fragment = Fragment.instantiate(activity, clz.getName(), args);
//                //ft.add(android.R.id.content, fragment, tag);
//                ft.add(R.id.fragment_content, fragment, tag);
//            } else {
//                ft.attach(fragment);
//            }
//            tab.setIcon(R.drawable.ic_launcher);
//        }
//
//        @Override
//        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
//            if(fragment != null) {
//                ft.detach(fragment);
//                tab.setIcon(R.drawable.ic_launcher_v1);
//            }
//        }
//
//        @Override
//        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
//
//        }
//    }
    // ---- inner class END----
}