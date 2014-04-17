package idv.funnybrain.plurkchat.ui;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.data.Friend;
import idv.funnybrain.plurkchat.data.Responses;
import idv.funnybrain.plurkchat.utils.ImageFetcher;

import java.util.HashMap;
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
    private String my_Id;
    // ---- local variable END ----

    public ChattingRoomListAdapter(LayoutInflater inflater, HashMap<String, Friend> fri, List<Responses> res, ImageFetcher imageFetcher, String my_id) {
        this.inflater = inflater;
        this.friends = fri;
        this.responses = res;
        this.mImageFetcher = imageFetcher;
        this.my_Id = my_id;
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

//    static class ViewHolder {
//        public TextView tv_id;
//        public ImageView iv_image;
//        public TextView tv_title;
//    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View rowView = convertView;
//        if (rowView == null) {
//            if (responses.get(position).getUser_id().equals(my_Id)) {
//                rowView = this.inflater.inflate(R.layout.chatting_cell_others, null);
//            } else {
//                rowView = this.inflater.inflate(R.layout.chatting_cell_me, null);
//            }
//            ViewHolder holder = new ViewHolder();
//            holder.tv_id = (TextView) rowView.findViewById(R.id._id);
//            holder.iv_image = (ImageView) rowView.findViewById(R.id.icon);
//            holder.tv_title = (TextView) rowView.findViewById(R.id.message);
//            rowView.setTag(holder);
//        }
//        final ViewHolder holder = (ViewHolder) rowView.getTag();
        View view;
        Responses resp = this.responses.get(position);
        if (resp.getUser_id().equals(my_Id)) {
                view = this.inflater.inflate(R.layout.chatting_cell_others, null);
        } else {
                view = this.inflater.inflate(R.layout.chatting_cell_me, null);
        }
        ((TextView)view.findViewById(R.id._id)).setText(resp.getId());

        ((TextView)view.findViewById(R.id.message)).setText(Html.fromHtml(resp.getContent()));

        if(friends.containsKey(resp.getUser_id())) {
            Friend fri = friends.get(resp.getUser_id());
            mImageFetcher.loadImage(fri.getHumanImage(), (ImageView)view.findViewById(R.id.icon));
        }

        return view;
    }

    public void addNewData() {
        notifyDataSetChanged();
    }
}
