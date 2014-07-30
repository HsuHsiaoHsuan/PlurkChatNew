package idv.funnybrain.plurkchat.asynctask;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.greenrobot.event.EventBus;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.RequestException;
import idv.funnybrain.plurkchat.data.Friend;
import idv.funnybrain.plurkchat.data.IHuman;
import idv.funnybrain.plurkchat.eventbus.Event_GetFriendsByOffset;
import idv.funnybrain.plurkchat.modules.Mod_FriendsFans;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by freeman on 2014/7/22.
 */
// public class Async_FriendFans_getFriendsByOffset extends AsyncTaskLoader<List<IHuman>> {
public class Async_FriendFans_getFriendsByOffset extends AsyncTaskLoader<Void> {
    private static final boolean D = false;
    private static final String TAG = "Async_FriendFans_getFriendsByOffset";

    private Context mContext;

    public Async_FriendFans_getFriendsByOffset(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    // public List<IHuman> loadInBackground() {
    public Void loadInBackground() {
        JSONArray result = null;
        int result_size = 0;
        List<IHuman> friends = new ArrayList<IHuman>();
        int round = 0;

        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            do {
                if(D) { Log.d(TAG, "Mod_FriendsFans_getFriendsByOffset_AsyncTask, while: " + round); }
                // result = plurkOAuth.getModule(Mod_FriendsFans.class).getFriendsByOffset(me.getHumanId(), 0 + 100 * round, 100);
                result = DataCentral.getInstance(getContext()).getPlurkOAuth().getModule(Mod_FriendsFans.class).
                        getFriendsByOffset(DataCentral.getInstance(mContext).getMe().getHumanId(), 0 + 100 * round, 100);
                // CWT   7014485
                // kero  4373060
                // 6880391

//                result_size = result.length();
//                for (int x = 0; x < result_size; x++) {
//                    friends.add(new Friend(result.getJSONObject(x)));
//                }
                JsonParser parser = factory.createParser(result.toString());
                Friend[] tmp = mapper.readValue(parser, Friend[].class);
                friends.addAll(Arrays.asList(tmp));

                round++;
            } while (result_size > 0);
            if (D) {
                Log.d(TAG, result.toString());
            }
        // } catch (JSONException je) {
        //    Log.e(TAG, je.getMessage());
        } catch (RequestException e) {
            Log.e(TAG, e.getMessage());
        } catch (JsonMappingException jme) {
            Log.e(TAG, jme.getMessage());
        } catch (JsonParseException jpe) {
            Log.e(TAG, jpe.getMessage());
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        }

        if(D) {
            for (int x = 0; x < friends.size(); x++) {
                String tmp = friends.get(x).getHumanName();
                if (tmp.equals("")) {
                    tmp = "!!!!!!!!!!!!";
                }
                System.out.println(x + " " + tmp + " " +
                        friends.get(x).getHumanId() + " " +
                        ((Friend) friends.get(x)).getNick_name() + " " +
                        ((Friend) friends.get(x)).getFull_name());
            }
        }

        // return friends;
        EventBus.getDefault().post(new Event_GetFriendsByOffset(friends));

        return null;
    }
}
