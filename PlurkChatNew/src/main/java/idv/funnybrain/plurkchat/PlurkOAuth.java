package idv.funnybrain.plurkchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.PlurkApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import idv.funnybrain.plurkchat.modules.AbstractModule;

/**
 * Created by Freeman on 2014/4/2.
 */
public class PlurkOAuth {
    private static final boolean D = false;
    private static final String TAG = "PlurkOAuth";

    private final static String API_KEY = "eOrMDFD8f5V2";
    private final static String API_SECRET = "aiVo5OOdWJMl0vEEblPVNRmbG3EeIQh5";

    private OAuthService service;
    private Token requestToken;
    private Token accessToken;

    @SuppressWarnings("rawtypes")
    private static Map<Class, Object> cachedModule = new HashMap<Class, Object>();

    public PlurkOAuth() {
        service = new ServiceBuilder().provider(PlurkApiNew.Mobile.class)
                                      .apiKey(API_KEY)
                                      .apiSecret(API_SECRET)
                                      .build();
        requestToken = service.getRequestToken();
    }

    public PlurkOAuth(String callbackURL) {
        service = new ServiceBuilder().provider(PlurkApiNew.Mobile.class)
                                      .apiKey(API_KEY)
                                      .apiSecret(API_SECRET)
                                      .callback(callbackURL)
                                      .build();

        requestToken = service.getRequestToken();
    }

    public PlurkOAuth(Token accessToken) {
        if(D) {
            Log.d(TAG, "acc_key: " + accessToken.getToken());
            Log.d(TAG, "acc_sec: " + accessToken.getSecret());
        }
        this.accessToken = accessToken;
        service = new ServiceBuilder().provider(PlurkApiNew.Mobile.class)
                .apiKey(API_KEY)
                .apiSecret(API_SECRET)
                .build();
    }

    public String getAuthURL() {
        if(D) { Log.d(TAG, "getAuthURL"); }
        return service.getAuthorizationUrl(requestToken);
    }

    public Token getRequestToken() {
        if(D) { Log.d(TAG, "getRequestToken"); }
        return requestToken;
    }

    public boolean setAccessToken(Context context, String validation_code) {
        accessToken = service.getAccessToken(requestToken, new Verifier(validation_code));
//        System.out.println("request token : " + requestToken.toString() + " ,validation: " + validation_code);
        SharedPreferences pref = context.getSharedPreferences("accessToken", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("key", accessToken.getToken());
        editor.putString("secret", accessToken.getSecret());
        return editor.commit();
    }

    public Token getAccessToken() {
        return accessToken;
    }

    public void reInit(Token access) {
        accessToken = access;
    }

    public String sendRequest(String url, List<NameValuePair> args) throws RequestException {
        if(D) { Log.d(TAG, "sendRequest url: " + url); }
        OAuthRequest request = new OAuthRequest(Verb.POST, url);
//        int size = args.size();
        if(args.size() > 0) {
//            for(int x=0; x< size; x++) {
//                request.addQuerystringParameter(args.get(x).getName(), args.get(x).getValue());
//            }
            for(NameValuePair pair : args) {
                request.addQuerystringParameter(pair.getName(), pair.getValue());
            }
        }
        service.signRequest(accessToken, request);
        Response response = request.send();
        if(response.getCode() != 200) {
            throw new RequestException("request error: " + response.getCode() + " " + response.getBody());
        }
        return response.getBody();
    }

    public String sendRequest(String url) throws RequestException {
        if(D) { Log.d(TAG, "sendRequest url: " + url); }
//        System.out.println(url);
        OAuthRequest request = new OAuthRequest(Verb.POST, url);
        service.signRequest(accessToken, request);
        Response response = request.send();
        if(response.getCode() != 200) {
            // TODO 解決 invalid access token
            throw new RequestException("request error: " + response.getCode() + " " + response.getBody());
        }
        return response.getBody();
    }

    @SuppressWarnings("unchecked")
    public <T> T getModule(Class<T> clazz) {
        try {
            if (cachedModule.containsKey(clazz) && cachedModule.get(clazz) != null) {
                return (T) cachedModule.get(clazz);
            }
            T instance = clazz.newInstance();
            if (instance instanceof AbstractModule) {
                AbstractModule module = (AbstractModule) instance;
                module.setPlurkOAuth(this);
                cachedModule.put(clazz, instance);
                return instance;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void test() {

        OAuthRequest req = new OAuthRequest(Verb.POST, "http://www.plurk.com/APP/Timeline/plurkAdd?content=myAppTests&qualifier=says&lang=tr_ch");
        service.signRequest(accessToken, req);
        Response resp = req.send();
        String result = resp.getBody();
//        System.out.println("final result: " + result);
    }
}
