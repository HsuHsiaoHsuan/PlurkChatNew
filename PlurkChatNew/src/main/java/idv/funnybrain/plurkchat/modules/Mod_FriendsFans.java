package idv.funnybrain.plurkchat.modules;

import idv.funnybrain.plurkchat.RequestException;
import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class Mod_FriendsFans extends AbstractModule {

    /**
     * Returns user_id's friend list in chucks of 10 friends at a time.
     * @param userID
     * @param offset The offset, can be 10, 20, 30 etc.
     * @param limit The max number of friends to be returned (default 10).
     */
    public JSONArray getFriendsByOffset(String userID, int offset, int limit) throws RequestException {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userID);
        params.put("offset", String.valueOf(offset));
        params.put("limit", String.valueOf(limit));
        JSONArray result = requestAPI("getFriendsByOffset").args(params).getJSONArrayResult();

        return result;
    }

    /**
     * Returns user_id's fans list in chucks of 10 fans at a time.
     */
    public void getFansByOffset() {

    }

    /**
     * Returns users that the current logged in user follows as fan - in chucks of 10 fans at a time.
     */
    public JSONArray getFollowingByOffset(int offset, int limit) throws RequestException {
        Map<String, String> params = new HashMap<>();
        params.put("offset", String.valueOf(offset));
        params.put("limit", String.valueOf(limit));
        JSONArray result = requestAPI("getFollowingByOffset").args(params).getJSONArrayResult();

        return result;
    }

    /**
     * Create a friend request to friend_id. User with friend_id has to accept a friendship.
     */
    public void becomeFriend() {

    }

    /**
     * Remove friend with ID friend_id. friend_id won't be notified.
     */
    public void removeAsFriend() {

    }

    /**
     * Become fan of fan_id. To stop being a fan of someone, user /APP/FriendsFans/setFollowing?fan_id=FAN_ID&follow=false.
     */
    public void becomeFan() {

    }

    /**
     * Update following of user_id. A user can befriend someone,
     * but can unfollow them. This request is also used to stop following someone as a fan.
     */
    public void setFollowing() {

    }

    /**
     * Returns a JSON object of the logged in users friends (nick name and full name).
     * This information can be used to construct auto-completion for private plurking.
     * Notice that a friend list can be big, depending on how many friends a user has,
     * so this list should be lazy-loaded in your application.
     */
    public void getCompletion() {

    }

    @Override
    protected String getModulePath() {
        return "/APP/FriendsFans";
    }
}