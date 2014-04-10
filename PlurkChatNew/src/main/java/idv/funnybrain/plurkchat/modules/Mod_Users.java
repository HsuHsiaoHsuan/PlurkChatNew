package idv.funnybrain.plurkchat.modules;

import idv.funnybrain.plurkchat.RequestException;
import org.json.JSONObject;

/**
 * Created by Freeman on 2014/4/2.
 */
public class Mod_Users extends AbstractModule {

    // Returns information about current user, including page-title and user-about.
    public JSONObject me() throws RequestException {
        JSONObject result = requestAPI("me").args(null).getJSONObjectResult();
        return result;
    }

    // Update a user's information (such as display name, email or privacy).
    public void update() {

    }

    // Update a user's profile picture. You can read more about how to render
    // an avatar via user data. You should do a multipart/form-data POST request to
    // /API/Users/updatePicture. The picture will be scaled down to 3 versions:
    // big, medium and small. The optimal size of profile_image should be 195x195 pixels.
    public void updatePicture() {

    }

    // Returns info about current user's karma, including
    // current karma, karma growth, karma graph and the latest reason why the karma has dropped.
    public void getKarmaStats() {

    }

    @Override
    protected String getModulePath() {
        return "/APP/Users";
    }
}
