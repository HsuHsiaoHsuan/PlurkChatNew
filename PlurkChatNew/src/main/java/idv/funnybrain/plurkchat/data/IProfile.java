package idv.funnybrain.plurkchat.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by freeman on 2014/12/18.
 */
public interface IProfile {
    public int getFriendsCount();

    public int getFansCount();

    public User_Info getUserInfo();

//    public String getPrivacy();

    public List<Plurks> getFormattedPlurks();
}
