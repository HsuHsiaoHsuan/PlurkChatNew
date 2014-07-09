package idv.funnybrain.plurkchat.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Freeman on 2014/4/3.
 */
public class Me implements IHuman{
    private String location = null;
    private boolean verified_account = false;
    private int recruited = 0;
    private String about = "";
    private String avatar_small = null;
    private String nick_name = null;
    private String id = null;
    private double karma = 0;
    private String timezone = null;
    private int bday_privacy = 0; // 0: hide birthday, 1: show birth date but not birth year, 2: show all
    private boolean post_anonymous_plurk = false;
    private boolean setup_weibo_sync = false;
    private String avatar_medium = "";
    private int gender = 2; // 1 is male, 0 is female, 2 is not stating/other.
    private String accept_private_plurk_from = null;
    private boolean setup_twitter_sync = false;
    private int fans_count = 0;
    private String date_of_birth = null;
    private String privacy = null; //User's privacy settings. The option can be world (whole world can view the profile) or only_friends (only friends can view the profile).
    private int profile_views = 0;
    private int plurks_count = 0;
    private String avatar = null;
    private String avatar_big = null;
    private boolean setup_facebook_sync = false;
    private String relationship = null;
    private String display_name = null;
    private String name_color = null;
    private int friends_count = 0;
    private int dateformat = 0;
    private int response_count = 0;
    private int has_profile_image = 0;
    private Language default_lang = null;
    private String full_name = null;
    private String page_title = null;

    public Me(JSONObject me) throws JSONException {
        if(!me.isNull("location"))                  { location = me.getString("location"); }
        if(!me.isNull("verified_account"))          { verified_account = me.getBoolean("verified_account"); }
        if(!me.isNull("recruited"))                 { recruited = me.getInt("recruited"); }
        if(!me.isNull("about"))                     { about = me.getString("about"); }
        if(!me.isNull("avatar_small"))              { avatar_small = me.getString("avatar_small"); }
        if(!me.isNull("nick_name"))                 { nick_name = me.getString("nick_name"); }
        if(!me.isNull("id"))                        { id = me.getString("id");}
        if(!me.isNull("karma"))                     { karma = me.getDouble("karma"); }
        if(!me.isNull("timezone"))                  { timezone = me.getString("timezone"); }
        if(!me.isNull("bday_privacy"))              { bday_privacy = me.getInt("bday_privacy"); }
        if(!me.isNull("post_anonymous_plurk"))      { post_anonymous_plurk = me.getBoolean("post_anonymous_plurk"); }
        if(!me.isNull("setup_weibo_sync"))          { setup_weibo_sync = me.getBoolean("setup_weibo_sync"); }
        if(!me.isNull("avatar_medium"))             { avatar_medium = me.getString("avatar_medium"); }
        if(!me.isNull("gender"))                    { gender = me.getInt("gender"); }
        if(!me.isNull("accept_private_plurk_from")) { accept_private_plurk_from = me.getString("accept_private_plurk_from"); }
        if(!me.isNull("setup_twitter_sync"))        { setup_twitter_sync = me.getBoolean("setup_twitter_sync"); }
        if(!me.isNull("fans_count"))                { fans_count = me.getInt("fans_count"); }
        if(!me.isNull("date_of_birth"))             { date_of_birth = me.getString("date_of_birth"); }
        if(!me.isNull("privacy"))                   { privacy = me.getString("privacy"); }
        if(!me.isNull("profile_views"))             { profile_views = me.getInt("profile_views"); }
        if(!me.isNull("plurks_count"))              { plurks_count = me.getInt("plurks_count"); }
        if(!me.isNull("avatar"))                    { avatar = me.getString("avatar"); }
        if(!me.isNull("avatar_big"))                { avatar_big = me.getString("avatar_big"); }
        if(!me.isNull("setup_facebook_sync"))       { setup_facebook_sync = me.getBoolean("setup_facebook_sync"); }
        if(!me.isNull("relationship"))              { relationship = me.getString("relationship"); }
        if(!me.isNull("display_name"))              { display_name = me.getString("display_name"); }
        if(!me.isNull("name_color"))                { name_color = me.getString("name_color"); }
        if(!me.isNull("friends_count"))             { friends_count = me.getInt("friends_count"); }
        if(!me.isNull("dateformat"))                { dateformat = me.getInt("dateformat"); }
        if(!me.isNull("response_count"))            { response_count = me.getInt("response_count"); }
        if(!me.isNull("has_profile_image"))         { has_profile_image = me.getInt("has_profile_image"); }
        if(!me.isNull("default_lang")) {
            String lang = me.getString("default_lang");
            default_lang = Language.getLang(lang);
        }
        if(!me.isNull("full_name"))                 { full_name = me.getString("full_name"); }
        if(!me.isNull("page_title"))                { page_title = me.getString("page_title"); }
    }

//    public String getId() {
//        return id;
//    }

    public String getDisplay_name() {
        return display_name;
    }

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

    public String getAbout() {
        return about;
    }
}
