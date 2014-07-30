package idv.funnybrain.plurkchat.data;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Freeman on 2014/4/8.
 */
public class Plurks implements Parcelable {
    private int responses_seen = 0;
    private Qualifier qualifier = Qualifier.NULL;
    private List<String> replurkers = new ArrayList<String>();
    private String plurk_id = "";
    private int response_count = 0;
    private int replurkers_count = 0;
    private boolean replurkable = false;
    private String limited_to = ""; // will be seperated by |, ex |uid|uid|uid|uid|uid|
    private boolean my_anonymous = false; // Qualifier: whispers
    private int no_comments = 0; // 2: only friends, 1: close response
    private int favorite_count = 0;
    private int is_unread = 0; // 1: true, 0: false
    private Language lang = Language.EN;
    private List<String> favorers = new ArrayList<String>();
    private String content_raw = "";
    private String user_id = "";
    private int plurk_type = 0; // 0: public, 1: specific some body
    private String qualifier_translated = ""; // sometimes it will missing // FIXME
    private boolean replurked = false;
    private boolean favorite = false;
    private String content = "";
    private String replurker_id = "null";
    private String posted = "";
    private String owner_id = "";

    public Plurks(JSONObject object) throws JSONException {
        if(!object.isNull("responses_seen"))       { responses_seen = object.getInt("responses_seen"); }
        if(!object.isNull("qualifier"))            { qualifier = Qualifier.getQualifier(object.getString("qualifier")); }
        if(!object.isNull("replurkers")) {
            JSONArray tmp_replurkers = object.getJSONArray("replurkers");
            replurkers = new ArrayList<String>();
            for (int x = 0; x < tmp_replurkers.length(); x++) {
                replurkers.add(tmp_replurkers.getString(x));
            }
        }
        if(!object.isNull("plurk_id"))             { plurk_id = object.getString("plurk_id"); }
        if(!object.isNull("response_count"))       { response_count = object.getInt("response_count"); }
        if(!object.isNull("replurkers_count"))     { replurkers_count = object.getInt("replurkers_count"); }
        if(!object.isNull("replurkable"))          { replurkable = object.getBoolean("replurkable"); }
        if(!object.isNull("limited_to"))           { limited_to = object.getString("limited_to"); }
        if(!object.isNull("no_comments"))          { no_comments = object.getInt("no_comments"); }
        if(!object.isNull("favorite_count"))       { favorite_count = object.getInt("favorite_count"); }
        if(!object.isNull("is_unread"))            { is_unread = object.getInt("is_unread"); }
        if(!object.isNull("lang"))                 { lang = Language.getLang(object.getString("lang")); }
        if(!object.isNull("favorers")) {
            JSONArray tmp_favorers = object.getJSONArray("favorers");
//            favorers = new ArrayList<String>();
            for (int x = 0; x < tmp_favorers.length(); x++) {
                favorers.add(tmp_favorers.getString(x));
            }
        }
        if(!object.isNull("content_raw"))          { content_raw = object.getString("content_raw"); }
        if(!object.isNull("user_id"))              { user_id = object.getString("user_id"); }
        if(!object.isNull("plurk_type"))           { plurk_type = object.getInt("plurk_type"); }
        if(!object.isNull("qualifier_translated")) { qualifier_translated = object.getString("qualifier_translated"); }
        if(!object.isNull("replurked"))            { replurked = object.getBoolean("replurked"); }
        if(!object.isNull("favorite"))             { favorite = object.getBoolean("favorite"); }
        if(!object.isNull("content"))              { content = object.getString("content"); }
        if(!object.isNull("replurker_id"))         { replurker_id = object.getString("replurker_id"); }
        if(!object.isNull("posted"))               { posted = object.getString("posted"); }
        if(!object.isNull("owner_id"))             { owner_id = object.getString("owner_id");}
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getPlurk_id() {
        return plurk_id;
    }

    public String getContent_raw() {
        return content_raw;
    }

    public String getContent() {
        return content;
    }

    public String getPosted() {
        return posted;
    }

    public String getReadablePostedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        try {
            Date last_posted_date = sdf.parse(posted);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(last_posted_date);
            SimpleDateFormat sdf_new = new SimpleDateFormat("yyyy MMMM d, a hh:mm:ss ");
//            System.out.println(sdf_new.toLocalizedPattern());

            return sdf_new.format(calendar.getTime()).toString();
            //return "return: " +calendar.getTime().toString();
            //System.out.println("convert to calendar: " + sdf_new.format(calendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
            return posted;
        }
    }

    public Date getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        try {
            Date date = sdf.parse(posted);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public String getQueryFormatedPostedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("E,dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        try {
            Date posted_date = sdf.parse(posted);
            //Calendar calendar = Calendar.getInstance();
            //calendar.setTime(posted_date);
            SimpleDateFormat sdf_new = new SimpleDateFormat("yyyy-M-d'T'k:m:s");
            sdf_new.setTimeZone(TimeZone.getTimeZone("GMT"));
            return sdf_new.format(posted_date);
            //return sdf_new.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return posted;
        }
    }

    public String getReplurker_id() {
        return replurker_id;
    }

    public int getResponse_count() {
        return response_count;
    }

    public static final Parcelable.Creator<Plurks> CREATOR = new Parcelable.Creator<Plurks>() {
        public Plurks createFromParcel(Parcel in) {
            return new Plurks(in);
        }

        @Override
        public Plurks[] newArray(int size) {
            return new Plurks[size];
        }
    };

    private Plurks(Parcel in) {
        responses_seen = in.readInt();
        qualifier = Qualifier.getQualifier(in.readString());
        in.readStringList(replurkers);
        plurk_id = in.readString();
        response_count = in.readInt();
        replurkers_count = in.readInt();
        replurkable = in.readByte() != 0; // == true if byte != 0
        limited_to = in.readString();
        my_anonymous = in.readByte() != 0; // == true if byte != 0
        no_comments = in.readInt();
        favorite_count = in.readInt();
        is_unread = in.readInt();
        lang = Language.getLang(in.readString());
        in.readStringList(favorers);
        content_raw = in.readString();
        user_id = in.readString();
        plurk_type = in.readInt();
        qualifier_translated = in.readString();
        replurked = in.readByte() != 0; // == true if byte != 0
        favorite = in.readByte() != 0; // == true if byte != 0
        content = in.readString();
        replurker_id = in.readString();
        posted = in.readString();
        owner_id = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(responses_seen);
        dest.writeString(qualifier.toString());
        dest.writeStringList(replurkers);
        dest.writeString(plurk_id);
        dest.writeInt(response_count);
        dest.writeInt(replurkers_count);
        dest.writeByte((byte)(replurkable ? 1: 0));
        dest.writeString(limited_to);
        dest.writeByte((byte)(my_anonymous ? 1 : 0));
        dest.writeInt(no_comments);
        dest.writeInt(favorite_count);
        dest.writeInt(is_unread);
        dest.writeString(lang.toString());
        dest.writeStringList(favorers);
        dest.writeString(content_raw);
        dest.writeString(user_id);
        dest.writeInt(plurk_type);
        dest.writeString(qualifier_translated);
        dest.writeByte((byte)(replurked ? 1 : 0));
        dest.writeByte((byte)(favorite ? 1 : 0));
        dest.writeString(content);
        dest.writeString(replurker_id);
        dest.writeString(posted);
        dest.writeString(owner_id);
    }
}

/*
{
            "replurkers_count": 0,
            "replurkable": true,
            "favorite_count": 0,
            "is_unread": 0,
            "content": "【文】《瓶據》。（盜筆‧瓶邪）",
            "user_id": 5225678,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": false,
            "favorers": [],
            "replurker_id": null,
            "owner_id": 4373060,
            "responses_seen": 0,
            "qualifier": "says",
            "plurk_id": 1205214599,
            "response_count": 10,
            "limited_to": null,
            "no_comments": 0,
            "posted": "Wed, 16 Apr 2014 13:49:33 GMT",
            "lang": "tr_ch",
            "content_raw": "【文】《瓶據》。（盜筆‧瓶邪）",
            "replurkers": [],
            "favorite": false
        }
 */