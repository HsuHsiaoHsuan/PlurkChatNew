package idv.funnybrain.plurkchat.ui;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.data.Friend;
import idv.funnybrain.plurkchat.data.Responses;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Freeman on 2014/4/11.
 */
public class ChattingRoomListAdapter extends BaseAdapter {
    // ---- constants START ----
    private static final boolean D = true;
    private static final String TAG = "ChattingRoomListAdapter";
    // ---- constants END ----

    // ---- local variable START ----
    private final LayoutInflater inflater;
    private HashMap<String, Friend> friends;
    private List<Responses> responses;
    private DataCentral mData;
    private ImageLoader mImageLoader;
    // ---- local variable END ----

    public ChattingRoomListAdapter(LayoutInflater inflater, HashMap<String, Friend> fri, List<Responses> res) {
        this.inflater = inflater;
        this.friends = fri;
        this.responses = res;
        mData = DataCentral.getInstance(inflater.getContext());
        mImageLoader = mData.getImageLoader();

        if(D) {
            Iterator<Friend> tmp_friend = friends.values().iterator();
            while (tmp_friend.hasNext()) {
                Friend tmp = tmp_friend.next();
                Log.d(TAG, "constructor, friends: " + tmp.getHumanName());
            }

            for(Responses r : responses) {
                Log.d(TAG, "construcor, responses: " + r.getContent());
            }
        }
    }

    @Override
    public int getCount() {
        return responses.size();
    }

    @Override
    public Object getItem(int position) {
        return responses.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return Long.valueOf(responses.get(position).getId());
        return position;
    }

    static class ViewHolder {
        public TextView tv_id;
        public NetworkImageView iv_leftImage;
        public TextView tv_poster_name;
        public NetworkImageView iv_rightImage;
        public TextView tv_msg;
        public TextView tv_msg_date;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            rowView = inflater.inflate(R.layout.chatting_cell, null);
            if(rowView == null) {
                Log.e(TAG, "why??");
            }
            ViewHolder holder = new ViewHolder();
            holder.tv_id = (TextView) rowView.findViewById(R.id.tv_id);
            holder.iv_leftImage = (NetworkImageView) rowView.findViewById(R.id.iv_leftImage);
            holder.tv_poster_name = (TextView) rowView.findViewById(R.id.tv_poster_name);
            holder.iv_rightImage = (NetworkImageView) rowView.findViewById(R.id.iv_rightImage);
            holder.tv_msg = (TextView) rowView.findViewById(R.id.tv_msg);
            holder.tv_msg.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tv_msg_date = (TextView) rowView.findViewById(R.id.tv_msg_date);
            rowView.setTag(holder);
        }
        final ViewHolder holder = (ViewHolder) rowView.getTag();
        Responses resp = responses.get(position);
        if(resp == null) Log.d(TAG, "resp null");

        holder.tv_id.setText(resp.getId());
        String msg_user_id = resp.getUser_id();

        if(D) { Log.d(TAG, "~~~~~~~>" + DataCentral.getInstance(parent.getContext()).getMe().getDisplay_name()); }
        if(msg_user_id.equals(DataCentral.getInstance(parent.getContext()).getMe().getHumanId())) {
            // if the poster is myself
            holder.iv_rightImage.setImageUrl(DataCentral.getInstance(parent.getContext()).getMe().getHumanImage(), mImageLoader);
            holder.iv_rightImage.setVisibility(View.VISIBLE);
            holder.iv_leftImage.setVisibility(View.GONE);
            holder.tv_poster_name.setText(DataCentral.getInstance(parent.getContext()).getMe().getDisplay_name());
            holder.tv_poster_name.setGravity(Gravity.RIGHT);
        } else {
            // if the poster is others
            Friend f = friends.get(msg_user_id);
            holder.iv_leftImage.setImageUrl(f.getHumanImage(), mImageLoader);
            holder.iv_leftImage.setVisibility(View.VISIBLE);
            holder.iv_rightImage.setVisibility(View.GONE);
            holder.tv_poster_name.setText(f.getDisplay_name());
            holder.tv_poster_name.setGravity(Gravity.LEFT);
        }

        holder.tv_msg.setText(Html.fromHtml(resp.getContent()));
        holder.tv_msg_date.setText(resp.getReadablePostedDate());

        return rowView;
    }

    public void addNewData() {
        notifyDataSetChanged();
    }
}
