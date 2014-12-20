package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.PublicProfile;
import idv.funnybrain.plurkchat.modules.Mod_Profile;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by freeman on 2014/12/18.
 */
public class Async_Profile_getPublicProfile extends AsyncTaskLoader<Void> {
    private static final boolean D = false;
    private static final String TAG = "Async_Profile_getPublicProfile";

    private Context mContext;
    private String uid;

    public Async_Profile_getPublicProfile(Context context, String user_id) {
        super(context);
        mContext = context;
        uid = user_id;
    }

    @Override
    public Void loadInBackground() {
        JSONObject result = null;

        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            result = DataCentral.getInstance(getContext()).getPlurkOAuth().getModule(Mod_Profile.class).getPublicProfile(uid);
            System.out.print(TAG + "----------->>>" + result);

            JsonParser parser = factory.createParser(result.toString());
            PublicProfile tmp = mapper.readValue(parser, PublicProfile.class);

        } catch (RequestException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
