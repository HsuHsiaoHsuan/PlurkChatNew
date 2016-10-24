package idv.funnybrain.plurkchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final boolean D = true;

    @BindView(R.id.bt_getAuth) Button bt_getAuth;
    @BindView(R.id.wv_auth_url) WebView webView;

    private final int MSG_GET_OAUTH = 0;
    private final int MSG_INIT_TOKEN = MSG_GET_OAUTH + 1;

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
                    DataCentral.getInstance().setPlurkOAuth(oauth);
                    break;
                case MSG_INIT_TOKEN:
                    setResult(RESULT_OK);
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        bt_getAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoginThread.start();
            }
        });
        goLoginThread.start();
    }

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
                        if (DataCentral.getInstance().getPlurkOAuth().setAccessToken(LoginActivity.this, code)) {
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
}