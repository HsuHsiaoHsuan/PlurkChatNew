package idv.funnybrain.plurkchat.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.data.Friend;
import idv.funnybrain.plurkchat.utils.ImageFetcher;

import java.util.List;

/**
 * Created by Freeman on 2014/4/7.
 */
public class FriendsListAdapter extends BaseAdapter {
    // ---- constants START ----
    private static final boolean D = true;
    private static final String TAG = "FriendsListAdapter";
    // ---- constants END ----

    // ---- local variable START ----
    private final LayoutInflater inflater;
    private List<Friend> friends;
    private ImageFetcher mImageFetcher;
    // ---- local variable END ----

    public FriendsListAdapter(LayoutInflater inflater, List<Friend> friends, ImageFetcher imageFetcher) {
        this.inflater = inflater;
        this.friends = friends;
        mImageFetcher = imageFetcher;
    }

    @Override
    public int getCount() {
        return this.friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(friends.get(position).getId());
    }

    static class ViewHolder {
        public TextView tv_id;
        public ImageView iv_image;
        public TextView tv_title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = this.inflater.inflate(R.layout.friend_cell, null);
            ViewHolder holder = new ViewHolder();
            holder.tv_id = (TextView) rowView.findViewById(R.id.uid);
            holder.iv_image = (ImageView) rowView.findViewById(R.id.image);
            holder.tv_title = (TextView) rowView.findViewById(R.id.title);
            rowView.setTag(holder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();
        Friend f = this.friends.get(position);

        holder.tv_id.setText(f.getId());
        //String showTitle = f.getDisplay_name().equals("") ? f.getFull_name() : f.getDisplay_name();
        holder.tv_title.setText(f.getShowName());

        String imgURL = f.getIconAddress();

        mImageFetcher.loadImage(imgURL, holder.iv_image);

        return rowView;
    }
}