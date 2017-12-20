package idv.funnybrain.plurkchat.modules;

import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Language;
import idv.funnybrain.plurkchat.data.Qualifier;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
     * @return Successful return:
     *         Returns a JSON object of plurks and their users, e.g. {"plurks": [{"plurk_id": 3, "content": "Test", "qualifier_translated": "says", "qualifier": "says", "lang": "en" ...}, ...], "plurk_users": {"3": {"id": 3, "nick_name": "alvin", ...}}
     */
    public JSONObject getPlurks(String offset, int limit, String filter, boolean favorers_detail, boolean limited_detail, boolean replurkers_detail) throws RequestException {
        Map<String, String> params = new HashMap<>();
        if(offset != null) {
            params.put("offset", offset);
        }
        params.put("limit", "30");
        if(filter != null) {
            params.put("filter", filter);
        }
        if(favorers_detail) {
            params.put("favorers_detail", "true");
        }
        if(limited_detail) {
            params.put("limited_detail", "true");
        }
        if(replurkers_detail) {
            params.put("replurkers_detail", "true");
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
     * @return Successful return:
     *         Returns a JSON object of the new plurk, e.g. {"plurk_id": 3, "content": "Test", "qualifier_translated": "says", "qualifier": "says", "lang": "en" ...}
     *         Error returns:
     *             HTTP 400 BAD REQUEST with {"error_text": "Invalid data"} as body
     *             HTTP 400 BAD REQUEST with {"error_text": "Must be friends"} as body
     *             HTTP 400 BAD REQUEST with {"error_text": "Content is empty"} as body
     *             HTTP 400 BAD REQUEST with {"error_text": "anti-flood-same-content"} as body
     *             HTTP 400 BAD REQUEST with {"error_text": "anti-flood-spam-domain"} as body
     *             HTTP 400 BAD REQUEST with {"error_text": "anti-flood-too-many-new"} as body
     */
    public JSONObject plurkAdd(String content, Qualifier qualifier, String[] limited_to, int no_comments, Language lang) throws RequestException {
        Map<String, String> params = new HashMap<>();
        params.put("content", content);
        params.put("qualifier", qualifier.toString());
        if(limited_to != null) {
            params.put("limited_to", limited_to.toString());
        }
        if(no_comments != 0) {
            params.put("no_comments", String.valueOf(no_comments));
        }
        if(lang != null) {
            params.put("lang", lang.toString());
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
