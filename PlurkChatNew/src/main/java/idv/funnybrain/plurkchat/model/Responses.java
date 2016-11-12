package idv.funnybrain.plurkchat.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Freeman on 2014/4/11.
 */
public class Responses {
    public Responses() {
    }

    private Qualifier qualifier = Qualifier.NULL;
    private String plurk_id = ""; // which plurk is this response belong to
    private String id = ""; // the id of this message
    private Language lang = Language.EN;
    private String content_raw = "";
    private String user_id = "";
    private String qualifier_translated = ""; // sometimes it will missing // FIXME
    private String content = "";
    private String posted = ""; // post date

    public Responses(JSONObject object) throws JSONException {
        if(!object.isNull("qualifier"))            { qualifier = Qualifier.getQualifier(object.getString("qualifier")); }
        if(!object.isNull("plurk_id"))             { plurk_id = object.getString("plurk_id"); }
        if(!object.isNull("id"))             { plurk_id = object.getString("id"); }
        if(!object.isNull("lang"))                 { lang = Language.getLang(object.getString("lang")); }
        if(!object.isNull("content_raw"))          { content_raw = object.getString("content_raw"); }
        if(!object.isNull("user_id"))              { user_id = object.getString("user_id"); }
        if(!object.isNull("qualifier_translated")) { qualifier_translated = object.getString("qualifier_translated"); }
        if(!object.isNull("content"))              { content = object.getString("content"); }
        if(!object.isNull("posted"))               { posted = object.getString("posted"); }
    }

    public String getUser_id() {
        return user_id;
    }

    public String getId() {
        return id;
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

            return sdf_new.format(calendar.getTime()).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return posted;
        }
    }
}
/*
"lang": "tr_ch",
"content_raw": "啊...而且又是要等一～兩周...沒關係我無聲卡收多了 orz",
"user_id": 5225678,
"qualifier": "says",
"plurk_id": 1205360988,
"content": "啊...而且又是要等一～兩周...沒關係我無聲卡收多了 orz",
"qualifier_translated": "說",
"id": 5932319759,
"posted": "Thu, 17 Apr 2014 09:09:47 GMT"
 */
