package idv.funnybrain.plurkchat.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Freeman on 2014/4/8.
 * return from /APP/Timeline/getPlurks

 "plurk_users": {
 "1367985": {
 "verified_account": false,
 "default_lang": "tr_ch",
 "display_name": "墳墓（Brian Hsu）",
 "dateformat": 0,
 "nick_name": "brianhsu",
 "has_profile_image": 1,
 "location": "Taipei, Taiwan",
 "bday_privacy": 2,
 "date_of_birth": "Thu, 06 Oct 1983 00:01:00 GMT",
 "karma": 134.59,
 "full_name": "BrianHsu",
 "gender": 1,
 "name_color": null,
 "timezone": "Asia/Taipei",
 "id": 1367985,
 "avatar": 4
 }
 */
public class Plurk_Users implements IHuman,Parcelable {
    private static final boolean D = false;
    private static final String TAG = "Plurk_Users";

    private boolean verified_account = false;
    private Language default_lang = null;
    private String display_name = ""; // FIXME sometimes it will missing
    private int dateformat = 0;
    private String nick_name = "";
    private int has_profile_image = 0;
    private String location = "";
    private int bday_privacy = 0; // 0: hide birthday, 1: show birth date but not birth year, 2: show all
    private String date_of_birth = "";
    private double karma = 0;
    private String full_name = "null";
    private int gender = 2; // 1 is male, 0 is female, 2 is not stating/other.
    private String name_color = "";
    private String timezone = "";
    private String id = "";
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

    public static final Parcelable.Creator<Plurk_Users> CREATOR = new Parcelable.Creator<Plurk_Users>() {
        public Plurk_Users createFromParcel(Parcel in) {
            return new Plurk_Users(in);
        }

        @Override
        public Plurk_Users[] newArray(int size) {
            return new Plurk_Users[size];
        }
    };

    private Plurk_Users(Parcel in) {
        verified_account = in.readByte() != 0;
        default_lang = Language.valueOf(in.readString());
        display_name = in.readString();
        nick_name = in.readString();
        has_profile_image = in.readInt();
        location = in.readString();
        bday_privacy = in.readInt();
        date_of_birth = in.readString();
        karma = in.readDouble();
        full_name = in.readString();
        gender = in.readInt();
        name_color = in.readString();
        timezone = in.readString();
        id = in.readString();
        avatar = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte)(verified_account ? 1 : 0));
        dest.writeString(default_lang.toString());
        dest.writeString(display_name);
        dest.writeString(nick_name);
        dest.writeInt(has_profile_image);
        dest.writeString(location);
        dest.writeInt(bday_privacy);
        dest.writeString(date_of_birth);
        dest.writeDouble(karma);
        dest.writeString(full_name);
        dest.writeInt(gender);
        dest.writeString(name_color);
        dest.writeString(timezone);
        dest.writeString(id);
        dest.writeString(avatar);
    }
}
