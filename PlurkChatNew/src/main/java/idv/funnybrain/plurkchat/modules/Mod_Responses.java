package idv.funnybrain.plurkchat.modules;

import idv.funnybrain.plurkchat.RequestException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freeman on 2014/4/2.
 */
public class Mod_Responses extends AbstractModule {

    /**
     * Fetches responses for plurk with plurk_id and some basic info about the users.
     * @param plurk_id The plurk that the responses belong to.
     * @param from_response Only fetch responses from an offset - could be 5, 10 or 15 (default: 0)
     * @return Successful return:
     *         Returns a JSON object of responses, friends (users that has posted responses) and responses_seen (the last response that the logged in user has seen) e.g. {"friends": {"3": ...}, "responses_seen": 2, "responses": [{"lang": "en", "content_raw": "Reforms...}}
     *         Error returns:
     *         HTTP 400 BAD REQUEST with {"error_text": "Invalid data"} as body
     *         HTTP 400 BAD REQUEST with {"error_text": "Plurk not found"} as body
     *         HTTP 400 BAD REQUEST with {"error_text": "No permissions"} as body
     */
    public JSONObject get(String plurk_id, int from_response) throws RequestException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("plurk_id", plurk_id));

        if(from_response > 0) {
            params.add(new BasicNameValuePair("from_response", String.valueOf(from_response)));
        }

        JSONObject result = requestAPI("get").args(params).getJSONObjectResult();
        return result;
    }

    // Adds a responses to plurk_id. Language is inherited from the plurk.
    public void responseAdd() {

    }

    // Deletes a response. A user can delete own responses or responses that are posted to own plurks.
    public void responseDelete() {

    }

    @Override
    protected String getModulePath() {
        return "/APP/Responses";
    }
}
