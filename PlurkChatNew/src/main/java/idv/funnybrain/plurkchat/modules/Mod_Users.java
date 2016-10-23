/**
 *
 A user and the data
 Depending on what kind of request it is the data returned varies. For responses and plurks, the data returned is minimal and will look like this:

 {"display_name": "amix3", "gender": 0, "nick_name": "amix", "has_profile_image": 1, "id": 1, "avatar": null}
 This can be used to render minimal info about a user.

 For other type of requests, such as viewing a friend list or a profile, the data returned will be larger:

 {"display_name": "Alexey", "is_channel": 0, "nick_name": "Scoundrel", "has_profile_image": 1, "location": "Canada", "date_of_birth": "Sat, 19 Mar 1983 00:00:00 GMT", "relationship": "not_saying", "avatar": 3, "full_name": "Alexey Kovyrin", "gender": 1, "recruited": 6, "id": 5, "karma": 33.5}

 User attributes:
 id: The unique user id.
 nick_name: The unique nick_name of the user, for example amix.
 display_name: The non-unique display name of the user, for example Amir S. Only set if it's non empty.
 has_profile_image: If 1 then the user has a profile picture, otherwise the user should use the default.
 avatar: Specifies what the latest avatar (profile picture) version is.
 location: The user's location, a text string, for example Aarhus Denmark.
 default_lang: The user's profile language.
 date_of_birth: The user's birthday. Note that the birthday is always stored in UTC timezone. You should not convert it to local time zone, otherwise you may get the wrong date of user's birthday.
 bday_privacy: 0: hide birthday, 1: show birth date but not birth year, 2: show all
 full_name: The user's full name, like Amir Salihefendic.
 gender: 1 is male, 0 is female, 2 is not stating/other.
 karma: User's karma value.
 recruited: How many friends has the user recruited.
 relationship: Can be not_saying, single, married, divorced, engaged, in_relationship, complicated, widowed, unstable_relationship, open_relationship

 About birth date privacy
 If user selects to hide his birthday (bday_privacy=0), the returned date_of_birth will be null. If user selects to hide his age (bday_privacy=1), the returned date_of_birth will be altered by updating the birth year to 1904.
 How to render the avatar
 One needs to construct the avatar URL. user_id specifies user's id while avatar specifies the profile image version.

 If has_profile_image == 1 and avatar == null then the avatar is:

 http://avatars.plurk.com/{user_id}-small.gif
 http://avatars.plurk.com/{user_id}-medium.gif
 http://avatars.plurk.com/{user_id}-big.jpg
 If has_profile_image == 1 and avatar != null:

 http://avatars.plurk.com/{user_id}-small{avatar}.gif
 http://avatars.plurk.com/{user_id}-medium{avatar}.gif
 http://avatars.plurk.com/{user_id}-big{avatar}.jpg
 If has_profile_image == 0:

 http://www.plurk.com/static/default_small.gif
 http://www.plurk.com/static/default_medium.gif
 http://www.plurk.com/static/default_big.gif

 */
package idv.funnybrain.plurkchat.modules;

import org.json.JSONObject;

import idv.funnybrain.plurkchat.RequestException;

/**
 * Created by Freeman on 2014/4/2.
 */
public class Mod_Users extends AbstractModule {

    /**
     * Returns information about current user, including page-title and user-about.
     * @return Successful return: user data as described above
     * @throws RequestException
     */
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
