package idv.funnybrain.plurkchat.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Freeman on 2014/4/8.
 */
public class Plurks {
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
    private String replurker_id = "";
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
        SimpleDateFormat sdf = new SimpleDateFormat("E,dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
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
}
