package idv.funnybrain.plurkchat.modules;

import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Language;
import idv.funnybrain.plurkchat.data.Qualifier;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freeman on 2014/4/2.
 */
public class Mod_Timeline extends AbstractModule {

    //
    public void getPlurk() {
        // call Mod_Polling/getPlurks
    }

    /**
     *
     * @param offset Return plurks older than offset, formatted as 2009-6-20T21:55:34.
     * @param limit How many plurks should be returned? Default is 20. Max 30
     * @param filter Can be only_user, only_responded, only_private or only_favorite
     * @param favorers_detail If true, detailed users information about all favorers of all plurks will be included into "plurk_users"
     * @param limited_detail If true, detailed users information about all private plurks' recepients will be included into "plurk_users"
     * @param replurkers_detail If true, detailed users information about all replurkers of all plurks will be included into "plurk_users"
     * @return JSON data
     */
    public JSONObject getPlurks(String offset, int limit, String filter, boolean favorers_detail, boolean limited_detail, boolean replurkers_detail) throws RequestException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if(offset != null) {
            params.add(new BasicNameValuePair("offset", offset));
        }
        params.add(new BasicNameValuePair("limit", "30"));
        if(filter != null) {
            params.add(new BasicNameValuePair("filter", filter));
        }
        if(favorers_detail) {
            params.add(new BasicNameValuePair("favorers_detail", "true"));
        }
        if(limited_detail) {
            params.add(new BasicNameValuePair("limited_detail", "true"));
        }
        if(replurkers_detail) {
            params.add(new BasicNameValuePair("replurkers_detail", "true"));
        }

        JSONObject result = requestAPI("getPlurks").args(params).getJSONObjectResult();
        return result;
    }

    //
    public void getUnreadPlurks() {

    }

    //
    public void getPublicPlurks() {

    }

    /**
     *
     * @param content The Plurk's text.
     * @param qualifier The Plurk's qualifier, must be in English.
     * @param limited_to Limit the plurk only to some users (also known as private plurking).
     *                   limited_to should be a JSON list of friend ids, e.g. limited_to of [3,4,66,34]
     *                   will only be plurked to these user ids. If limited_to is [0] then the Plurk is
     *                   privatley posted to the poster's friends.
     * @param no_comments If set to 1, then responses are disabled for this plurk.
     *                    If set to 2, then only friends can respond to this plurk.
     * @param lang The plurk's language.
     * @return JSONObject
     */
    public JSONObject plurkAdd(String content, Qualifier qualifier, String[] limited_to, int no_comments, Language lang) throws RequestException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("content", content));
        params.add(new BasicNameValuePair("qualifier", qualifier.toString()));
        if(limited_to != null) {
            params.add(new BasicNameValuePair("limited_to", limited_to.toString()));
        }
        if(no_comments != 0) {
            params.add(new BasicNameValuePair("no_comments", String.valueOf(no_comments)));
        }
        if(lang != null) {
            params.add(new BasicNameValuePair("lang", lang.toString()));
        }


        JSONObject result = requestAPI("plurkAdd").args(params).getJSONObjectResult();
        return result;
    }

    //
    public void plurkDelete() {

    }

    //
    public void plurkEdit() {

    }

    //
    public void toggleComments() {

    }

    //
    public void mutePlurks() {

    }

    //
    public void unmutePlurks() {

    }

    //
    public void favoritePlurks() {

    }

    //
    public void unfavoritePlurks() {

    }

    //
    public void replurk() {

    }

    //
    public void unreplurk() {

    }

    //
    public void markAsRead() {

    }

    // To upload a picture to Plurk, you should do a multipart/form-data POST
    // request to /APP/Timeline/uploadPicture. This will add the picture to Plurk's CDN network and
    // return a image link that you can add to /APP/Timeline/plurkAdd
    // Plurk will automatically scale down the image and create a thumbnail.
    public void uploadPicture() {

    }

    // porn:       Nudity and Pornography (祼露和色情)
    // spam:       Spam and Phishing (垃圾訊息和網路釣魚)
    // privacy:    Privacy and Identity (隱私和身分)
    // violence:   Violence and Threats (暴力和威脅)
    // others:     Others (其他)
    public void reportAbuse() {

    }

    @Override
    protected String getModulePath() {
        return "/APP/Timeline";
    }
}
