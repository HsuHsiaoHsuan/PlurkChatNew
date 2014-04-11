package idv.funnybrain.plurkchat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import idv.funnybrain.plurkchat.data.Language;
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.data.Qualifier;
import idv.funnybrain.plurkchat.modules.Mod_Timeline;
import idv.funnybrain.plurkchat.modules.Mod_Users;
import idv.funnybrain.plurkchat.ui.ChatRoomsFragment;
import idv.funnybrain.plurkchat.ui.FriendsFragment;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class FunnyActivity extends SherlockFragmentActivity {
    // ---- constant START ----
    private static final boolean D = true;
    private static final String TAG = "FunnyActivity";

    private final static int HANDLER_SHOW_AUTH_URL = 0;
    private final static int HANDLER_GET_ACCESS_TOKEN_OK = HANDLER_SHOW_AUTH_URL + 1;
    private final static int HANDLER_GET_SELF_OK = HANDLER_GET_ACCESS_TOKEN_OK +1;
    // ---- constant END ----

    // ---- local variable START ----
    private static PlurkOAuth plurkOAuth;
    private OAuthService service;
    private Token requestToken;
    private Token accessToken;
    private Handler handler;
    private Verifier verifier;

    private Me me;
    // ---- local variable END ----

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funny);

        final EditText et_auth = (EditText) findViewById(R.id.et_auth);
        final Button bt_submit_auth = (Button) findViewById(R.id.bt_submit_auth);
        bt_submit_auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_auth.getWindowToken(), 0);

                String verification_code = et_auth.getText().toString();
                if(D) { Log.d(TAG, "the verification code is: " + verification_code); }
                new PlurkGetAccessTokenAsyncTask().execute(verification_code);
            }
        });

        Button bt_getAuth = (Button) findViewById(R.id.bt_getAuth);
        bt_getAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PlurkLoginAsyncTask().execute("");
            }
        });

        final WebView webView = (WebView) findViewById(R.id.wv_auth_url);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case HANDLER_SHOW_AUTH_URL:
                        if(D) { Log.d(TAG, "HANDLER_SHOW_AUTH_URL"); }
                        String AuthURL = msg.getData().getString("URL");
                        webView.loadUrl(AuthURL);
                        et_auth.setVisibility(View.VISIBLE);
                        bt_submit_auth.setVisibility(View.VISIBLE);
                        break;
                    case HANDLER_GET_ACCESS_TOKEN_OK:
                        if(D) { Log.d(TAG, "HANDLER_GET_ACCESS_TOKEN"); }
                        setContentView(R.layout.empty);
                        //new PlurkTmpAsyncTask().execute("");
                        // should get user self
                        new Mod_Users_me_AsyncTask().execute("");

                        //doTest();
                        break;
                    case HANDLER_GET_SELF_OK:
                        if(D) { Log.d(TAG, "HANDLER_GET_SELF_OK: " + me.getDisplay_name()); }
                        ActionBar actionBar = getSupportActionBar();
                        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
                        actionBar.setDisplayShowTitleEnabled(true);

                        ActionBar.Tab tab = actionBar.newTab()
                                .setText("Friends")
                                .setTabListener(new TabListener<FriendsFragment>(
                                        FunnyActivity.this, "friends", FriendsFragment.class
                                ));
                        actionBar.addTab(tab);

                        actionBar.addTab(
                                actionBar.newTab()
                                        .setText("Chat Roooms")
                                        .setTabListener(new TabListener<ChatRoomsFragment>(
                                                FunnyActivity.this, "rooms", ChatRoomsFragment.class
                                        ))
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
            }
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
    private class PlurkLoginAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            plurkOAuth = new PlurkOAuth();
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
                ft.add(android.R.id.content, fragment, tag);
            } else {
                ft.attach(fragment);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
            if(fragment != null) {
                ft.detach(fragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

        }
    }
    // ---- inner class END----
}
