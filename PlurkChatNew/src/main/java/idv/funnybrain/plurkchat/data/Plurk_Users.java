package idv.funnybrain.plurkchat.data;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Freeman on 2014/4/8.
 */
public class Plurk_Users implements IHuman{
    private static final boolean D = false;
    private static final String TAG = "Plurk_Users";

    private boolean verified_account = false;
    private Language default_lang = null;
    private String display_name = ""; // FIXME sometimes it will missing
    private int dateformat = 0;
    private String nick_name = "";
    private int has_profile_image = 0;
    private String location = null;
    private int bday_privacy = 0; // 0: hide birthday, 1: show birth date but not birth year, 2: show all
    private String date_of_birth = null;
    private double karma = 0;
    private String full_name = "null";
    private int gender = 2; // 1 is male, 0 is female, 2 is not stating/other.
    private String name_color = null;
    private String timezone = null;
    private String id = null;
    private String avatar = "NaN";

    public Plurk_Users(JSONObject object) throws JSONException {
        if(!object.isNull("verified_account"))  { verified_account = object.getBoolean("verified_account"); }
        if(D) { Log.d(TAG, "verified_account: " + verified_account); }
        if(!object.isNull("default_lang"))      { default_lang = Language.getLang(object.getString("default_lang")); }
        if(D) { Log.d(TAG, "default_lang: " + default_lang); }
        if(!object.isNull("display_name"))      { display_name = object.getString("display_name"); }
        if(D) { Log.d(TAG, "display_name: " + display_name); }
        if(!object.isNull("dateformat"))        { dateformat = object.getInt("dateformat"); }
        if(D) { Log.d(TAG, "dateformat: " + dateformat); }
        if(!object.isNull("nick_name"))         { nick_name = object.getString("nick_name"); }
        if(D) { Log.d(TAG, "nick_name: " + nick_name); }
        if(!object.isNull("has_profile_image")) { has_profile_image = object.getInt("has_profile_image"); }
        if(D) { Log.d(TAG, "has_profile_image: " + has_profile_image); }
        if(!object.isNull("location"))          { location = object.getString("location"); }
        if(D) { Log.d(TAG, "location: " + location); }
        if(!object.isNull("bday_privacy"))      { bday_privacy = object.getInt("bday_privacy"); }
        if(D) { Log.d(TAG, "bday_privacy: " + bday_privacy); }
        if(!object.isNull("date_of_birth"))     { date_of_birth = object.getString("date_of_birth"); }
        if(D) { Log.d(TAG, "date_of_birth: " + date_of_birth); }
        if(!object.isNull("karma"))             { karma = object.getDouble("karma"); }
        if(D) { Log.d(TAG, "karma: " + karma); }
        if(!object.isNull("full_name"))         { full_name = object.getString("full_name"); }
        if(D) { Log.d(TAG, "full_name: " + full_name); }
        if(!object.isNull("gender"))            { gender = object.getInt("gender"); }
        if(D) { Log.d(TAG, "gender: " + gender); }
        if(!object.isNull("name_color"))        { name_color = object.getString("name_color"); }
        if(D) { Log.d(TAG, "name_color: " + name_color); }
        if(!object.isNull("timezone"))          { timezone = object.getString("timezone"); }
        if(D) { Log.d(TAG, "timezone: " + timezone); }
        if(!object.isNull("id"))                { id = object.getString("id"); }
        if(D) { Log.d(TAG, "id: " + id); }
        if(!object.isNull("avatar"))            { avatar = object.getString("avatar"); }
        if(D) { Log.d(TAG, "avatar: " + avatar); }
    }

//    public String getIconAddress() {
//        String imgURL = "http://www.plurk.com/static/default_big.gif";
//        if(has_profile_image>0) {
//            if(avatar.equals("null")) {
//                imgURL = "http://avatars.plurk.com/" + id + "-big.jpg";
//            } else {
//                if(avatar.equals("0")) { avatar = ""; }
//                imgURL = "http://avatars.plurk.com/" + id + "-big" + avatar + ".jpg";
//            }
//        }
//        return imgURL;
//    }

//    public String getId() {
//        return id;
//    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getFull_name() {
        return full_name;
    }

//    public String getShowName() {
//        if(!display_name.equals("") && !display_name.equals("null")) {
//            return display_name;
//        } else if(!full_name.equals("") && !full_name.equals("null")) {
//            return full_name;
//        } else {
//            return nick_name;
//        }
//    }

    @Override
    public String getHumanId() {
        return id;
    }

    @Override
    public String getHumanName() {
        if(!display_name.equals("") && !display_name.equals("null")) {
            return display_name;
        } else if(!full_name.equals("") && !full_name.equals("null")) {
            return full_name;
        } else {
            return nick_name;
        }
    }

    @Override
    public String getHumanImage() {
        String imgURL = "http://www.plurk.com/static/default_big.gif";
        if(has_profile_image>0) {
            if(avatar.equals("null")) {
                imgURL = "http://avatars.plurk.com/" + id + "-big.jpg";
            } else {
                if(avatar.equals("0")) { avatar = ""; }
                imgURL = "http://avatars.plurk.com/" + id + "-big" + avatar + ".jpg";
            }
        }
        return imgURL;
    }
}
