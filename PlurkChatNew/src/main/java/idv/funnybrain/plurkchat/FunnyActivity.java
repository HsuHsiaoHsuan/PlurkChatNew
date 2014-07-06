package idv.funnybrain.plurkchat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import idv.funnybrain.plurkchat.data.Language;
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.data.Qualifier;
import idv.funnybrain.plurkchat.modules.Mod_Timeline;
import idv.funnybrain.plurkchat.modules.Mod_Users;
import idv.funnybrain.plurkchat.ui.ChatRoomsFragment_v2;
import idv.funnybrain.plurkchat.ui.MeFriendsFollowingFragment;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.Token;

public class FunnyActivity extends SherlockFragmentActivity {
    // ---- constant START ----
    static final boolean D = false;
    static final String TAG = "FunnyActivity";

    final static int HANDLER_SHOW_AUTH_URL = 0;
    final static int HANDLER_GET_ACCESS_TOKEN_OK = HANDLER_SHOW_AUTH_URL + 1;
    final static int HANDLER_GET_SELF_OK = HANDLER_GET_ACCESS_TOKEN_OK +1;
    // ---- constant END ----

    // ---- local variable START ----
    static PlurkOAuth plurkOAuth;
//    private OAuthService service;
//    private Token requestToken;
    private Token accessToken;
    private Handler handler;
//    private Verifier verifier;

    public static Me me;
    // ---- local variable END ----
    //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funny);

        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button bt_getAuth = (Button) findViewById(R.id.bt_getAuth);
        bt_getAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PlurkLoginAsyncTask().execute("");
            }
        });

        final WebView webView = (WebView) findViewById(R.id.wv_auth_url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new PlurkAuthWebViewClient());


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HANDLER_SHOW_AUTH_URL:
                        if(D) { Log.d(TAG, "HANDLER_SHOW_AUTH_URL"); }
                        String AuthURL = msg.getData().getString("URL");
                        webView.loadUrl(AuthURL);
                        break;
                    case HANDLER_GET_ACCESS_TOKEN_OK:
                        if(D) { Log.d(TAG, "HANDLER_GET_ACCESS_TOKEN"); }
                        //setContentView(R.layout.empty);

                        //new PlurkTmpAsyncTask().execute("");
                        // should get user self


                        new Mod_Users_me_AsyncTask().execute("");

                        //doTest();
                        break;
                    case HANDLER_GET_SELF_OK:
                        if(D) { Log.d(TAG, "HANDLER_GET_SELF_OK: " + me.getDisplay_name()); }
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                        //actionBar.setDisplayShowTitleEnabled(true);
                        findViewById(R.id.fragment_content).setVisibility(View.VISIBLE);
                        findViewById(R.id.login_control).setVisibility(View.GONE);

//                        ActionBar.Tab tab = actionBar.newTab()
//                                .setText(R.string.tab_friends)
//                                .setTabListener(new TabListener<FriendsFragment>(
//                                        FunnyActivity.this, "friends", FriendsFragment.class
//                                ));
//                        actionBar.addTab(tab);

                        actionBar.addTab(
                                actionBar.newTab()
//                                        .setText(R.string.tab_friends)
                                        .setTabListener(new TabListener<MeFriendsFollowingFragment>(
                                                FunnyActivity.this, "me_friend_following", MeFriendsFollowingFragment.class
                                        ))
                                        .setIcon(R.drawable.ic_launcher_v1)
                        );

                        actionBar.addTab(
                                actionBar.newTab()
//                                        .setText(R.string.tab_chatrooms)
                                        .setTabListener(new TabListener<ChatRoomsFragment_v2>(
                                                FunnyActivity.this, "rooms", ChatRoomsFragment_v2.class
                                        ))
                                        .setIcon(R.drawable.ic_launcher_v1)
                        );

                        break;
                }
            }
        };

        SharedPreferences pref = getSharedPreferences("accessToken", Context.MODE_PRIVATE);
        String key = pref.getString("key", "nothing");
        String secret = pref.getString("secret", "nothing");
        if( (!key.equals("nothing")) && (!secret.equals("nothing")) ) {
            accessToken = new Token(key, secret);
            plurkOAuth = new PlurkOAuth(accessToken);

            Message msg = new Message();
            msg.what = HANDLER_GET_ACCESS_TOKEN_OK;
            handler.sendMessage(msg);

            if(savedInstanceState != null) {
                getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
                // FIXME
                // rotation, setRetainInstance?
            }
        } else {
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
        outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
    }

    private void toast(Object msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }

    public void doTest() {
        new PlurkTmpAsyncTask().execute("");
    }

    public PlurkOAuth getPlurkOAuth() { return plurkOAuth; }

    public Me getMe() { return me; }

    // ---- inner class START ----

    //
    private class PlurkAuthWebViewClient extends WebViewClient {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (D) { Log.d(TAG, "====> WebViewClient.shouldOverrideURL.url=" + url); }

        if (url.startsWith("http://localhost/auth")) {
          Uri uri = Uri.parse(url);
          String code = uri.getQueryParameter("oauth_verifier");
          new PlurkGetAccessTokenAsyncTask().execute(code);
          return true;
        }

        return false;
      }
    }


    private class PlurkLoginAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            plurkOAuth = new PlurkOAuth("http://localhost/auth");
            plurkOAuth.getAuthURL();

            Message msg = new Message();
            msg.what = HANDLER_SHOW_AUTH_URL;
            msg.getData().putString("URL", plurkOAuth.getAuthURL());
            handler.sendMessage(msg);

            return "";
        }
    }


    private class PlurkGetAccessTokenAsyncTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            return plurkOAuth.setAccessToken(FunnyActivity.this , params[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                Message msg = new Message();
                msg.what = HANDLER_GET_ACCESS_TOKEN_OK;
                handler.sendMessage(msg);
            }
        }
    }

    private class PlurkTmpAsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject result = null;
            try {
                //System.out.println();
                result = plurkOAuth.getModule(Mod_Timeline.class).plurkAdd("(wave)(wave)(wave)"+ Math.random(), Qualifier.SAYS, null, 0, Language.TR_CH);
            } catch (RequestException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    // get myself
    private class Mod_Users_me_AsyncTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject result = null;
            try {
                result = plurkOAuth.getModule(Mod_Users.class).me();
                if(D) { Log.d(TAG, "Mod_Users_me_AsyncTask: " + result.toString()); }
            } catch (RequestException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            if(object != null) {
                try {
                    me = new Me(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = HANDLER_GET_SELF_OK;
                handler.sendMessage(msg);
            }
        }
    }

    private static class TabListener<T extends SherlockFragment> implements ActionBar.TabListener {
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
        public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
            if(fragment == null) {
                fragment = SherlockFragment.instantiate(activity, clz.getName(), args);
                //ft.add(android.R.id.content, fragment, tag);
                ft.add(R.id.fragment_content, fragment, tag);
            } else {
                ft.attach(fragment);
            }
            tab.setIcon(R.drawable.ic_launcher);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
            if(fragment != null) {
                ft.detach(fragment);
                tab.setIcon(R.drawable.ic_launcher_v1);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

        }
    }
    // ---- inner class END----
}
