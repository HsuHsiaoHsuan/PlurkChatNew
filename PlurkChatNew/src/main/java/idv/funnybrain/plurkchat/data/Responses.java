package idv.funnybrain.plurkchat.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freeman on 2014/4/11.
 */
public class Responses {
    private Qualifier qualifier = Qualifier.NULL;
    private String plurk_id = "";
    private String id = "";
    private Language lang = Language.EN;
    private String content_raw = "";
    private String user_id = "";
    private String qualifier_translated = ""; // sometimes it will missing // FIXME
    private String content = "";
    private String posted = "";

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
}
