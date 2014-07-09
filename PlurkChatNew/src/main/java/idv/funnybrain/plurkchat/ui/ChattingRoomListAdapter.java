package idv.funnybrain.plurkchat.ui;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import idv.funnybrain.plurkchat.FunnyActivity;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.data.Friend;
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.data.Responses;
import idv.funnybrain.plurkchat.utils.ImageFetcher;

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
    private ImageFetcher mImageFetcher;
//    private String my_Id;
    private Me me;
    // ---- local variable END ----

//    public ChattingRoomListAdapter(LayoutInflater inflater, HashMap<String, Friend> fri, List<Responses> res, ImageFetcher imageFetcher, String my_id) {
    public ChattingRoomListAdapter(LayoutInflater inflater, HashMap<String, Friend> fri, List<Responses> res, ImageFetcher imageFetcher) {
        this.inflater = inflater;
        this.friends = fri;
        this.responses = res;
        this.mImageFetcher = imageFetcher;
//        this.my_Id = my_id;
        //this.me = (new FunnyActivity()).getMe();
        this.me = FunnyActivity.me;

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
        public ImageView iv_leftImage;
        public TextView tv_poster_name;
        public ImageView iv_rightImage;
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
            holder.iv_leftImage = (ImageView) rowView.findViewById(R.id.iv_leftImage);
            holder.tv_poster_name = (TextView) rowView.findViewById(R.id.tv_poster_name);
            holder.iv_rightImage = (ImageView) rowView.findViewById(R.id.iv_rightImage);
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
        if(msg_user_id.equals(me.getHumanId())) {
            // if the poster is myself
            mImageFetcher.loadImage(me.getHumanImage(), holder.iv_rightImage);
            holder.iv_rightImage.setVisibility(View.VISIBLE);
            holder.iv_leftImage.setVisibility(View.GONE);
            holder.tv_poster_name.setText(me.getDisplay_name());
            holder.tv_poster_name.setGravity(Gravity.RIGHT);
        } else {
            // if the poster is others
            Friend f = friends.get(msg_user_id);
            mImageFetcher.loadImage(f.getHumanImage(), holder.iv_leftImage);
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
