package idv.funnybrain.plurkchat.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Freeman on 2014/4/5.
 "email_confirmed":true,
 "uid":1367985,
 "following_tl":1,
 "location":"Taipei, Taiwan",
 "verified_account":false,
 "settings":true,
 "following_im":1,
 "recruited":17,
 "date_of_birth":"Thu, 06 Oct 1983 00:01:00 GMT",
 "avatar":0,
 "nick_name":"brianhsu",
 "relationship":"single",
 "id":1367985,
 "karma":128.46,
 "display_name":"墳墓（Brian Hsu）"
 ,"name_color":null,
 "following":true,
 "timezone":"Asia\/Taipei",
 "dateformat":0,
 "bday_privacy":2,
 "gender":1,
 "has_profile_image":1,
 "default_lang":"tr_ch",
 "full_name":"BrianHsu"
 *
 */
public class Friend {
    private boolean email_confirmed = false;
    private String uid = null;
    private int following_tl = 0;
    private String location = null;
    private boolean verified_account = false;
    private boolean settings = false; // FIXME
    private int following_im = 0;
    private int recruited = 0;
    private String date_of_birth = "null";
    private String avatar = "null";
    private String nick_name = "";
    private String relationship = "";
    private String id = null;
    private double karma = 0;
    private String display_name = "";
    private String name_color = "null"; // FIXME
    private boolean following = false;
    private String timezone = "null";
    private int dateformat = 0;
    private int bday_privacy = 0;
    private int gender = 2; // 1 is male, 0 is female, 2 is not stating/other.
    private int has_profile_image = 0;
    private String default_lang = "";
    private String full_name = "null";

    public Friend(JSONObject friend) throws JSONException {
        if(!friend.isNull("email_confirmed"))   { email_confirmed = friend.getBoolean("email_confirmed"); }
        if(!friend.isNull("uid"))               { uid = friend.getString("uid"); }
        if(!friend.isNull("following_tl"))      { following_tl = friend.getInt("following_tl"); }
        if(!friend.isNull("location"))          { location = friend.getString("location"); }
        if(!friend.isNull("verified_account"))  { verified_account = friend.getBoolean("verified_account"); }
        if(!friend.isNull("settings"))          { settings = friend.getBoolean("settings"); }
        if(!friend.isNull("following_im"))      { following_im = friend.getInt("following_im"); }
        if(!friend.isNull("recruited"))         { recruited = friend.getInt("recruited"); }
        if(!friend.isNull("date_of_birth"))     { date_of_birth = friend.getString("date_of_birth"); }
        if(!friend.isNull("avatar"))            { avatar = friend.getString("avatar"); }
        if(!friend.isNull("nick_name"))         { nick_name = friend.getString("nick_name"); }
        if(!friend.isNull("relationship"))      { relationship = friend.getString("relationship"); }
        if(!friend.isNull("id"))                { id = friend.getString("id"); }
        if(!friend.isNull("karma"))             { karma = friend.getDouble("karma"); }
        if(!friend.isNull("display_name"))      { display_name = friend.getString("display_name"); }
        if(!friend.isNull("name_color"))        { name_color = friend.getString("name_color"); }
        if(!friend.isNull("following"))         { following = friend.getBoolean("following"); }
        if(!friend.isNull("timezone"))          { timezone = friend.getString("timezone"); }
        if(!friend.isNull("dateformat"))        { dateformat = friend.getInt("dateformat"); }
        if(!friend.isNull("bday_privacy"))      { bday_privacy = friend.getInt("bday_privacy"); }
        if(!friend.isNull("gender"))            { gender = friend.getInt("gender"); }
        if(!friend.isNull("has_profile_image")) { has_profile_image = friend.getInt("has_profile_image"); }
        if(!friend.isNull("default_lang"))      { default_lang = friend.getString("default_lang"); }
        if(!friend.isNull("full_name"))         { full_name = friend.getString("full_name"); }
    }

    public String getId() { return id; }

    public String getDisplay_name() { return display_name; }

    public String getNick_name() { return nick_name; }

    public String getFull_name() { return full_name; }

    public int getHas_profile_image() { return has_profile_image; }

    public String getAvatar() { return avatar; }

    public String getIconAddress() {
        String imgURL = "http://www.plurk.com/static/default_big.gif";
        if(getHas_profile_image()>0) {
            String avatar = getAvatar();
            if(avatar.equals("null")) {
                imgURL = "http://avatars.plurk.com/" + getId() + "-big.jpg";
            } else {
                if(avatar.equals("0")) { avatar = ""; }
                imgURL = "http://avatars.plurk.com/" + getId() + "-big" + avatar + ".jpg";
            }
        }
        return imgURL;
    }

    public String getShowName() {
        if(!display_name.equals("") && !display_name.equals("null")) {
            return display_name;
        } else if(!full_name.equals("") && !full_name.equals("null")) {
            return full_name;
        } else {
            return nick_name;
        }
    }
}
