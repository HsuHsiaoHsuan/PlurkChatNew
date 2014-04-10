package idv.funnybrain.plurkchat.ui;

import android.app.Activity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.data.Plurk_Users;
import idv.funnybrain.plurkchat.data.Plurks;
import idv.funnybrain.plurkchat.logger.Log;
import idv.funnybrain.plurkchat.utils.ImageFetcher;

import java.util.List;

/**
 * Created by Freeman on 2014/4/8.
 */
public class ChatRoomExpandableListAdapter extends BaseExpandableListAdapter {
    // ---- constants START ----
    private static final boolean D = true;
    private static final String TAG = "ChatRoomExpandableListAdapter";
    // ---- constants END ----

    // --- local variable START ----
    private Activity activity;
    private List<Plurk_Users> plurk_users;
    private List<List<Plurks>> plurks;
    private ImageFetcher mImageFetcher;
    // --- local variable END ----

    public ChatRoomExpandableListAdapter(Activity activity, List<Plurk_Users> users, List<List<Plurks>> plurks, ImageFetcher imageFetcher) {
        this.activity = activity;
        this.plurk_users = users;
        this.plurks = plurks;
        this.mImageFetcher = imageFetcher;
    }

    public void addMore() {
        if(D) { Log.d(TAG, "add More"); }
    }

    @Override
    public int getGroupCount() {
        if(D) { Log.d(TAG, "getGroupCount" + plurk_users.size()); }
        return plurk_users.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(D) { Log.d(TAG, "getChildrenCount" + plurks.size()); }
        return plurks.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return plurk_users.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return plurks.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    static class ViewHolderGroup {
        public TextView tv_id;
        public ImageView iv_image;
        public TextView tv_title;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            rowView = activity.getLayoutInflater().inflate(R.layout.chatrooms_group_cell, null);
            ViewHolderGroup holder = new ViewHolderGroup();
            holder.tv_id = (TextView) rowView.findViewById(R.id.uid);
            holder.iv_image = (ImageView) rowView.findViewById(R.id.image);
            holder.tv_title = (TextView) rowView.findViewById(R.id.title);
            rowView.setTag(holder);
        }

        final ViewHolderGroup holder = (ViewHolderGroup) rowView.getTag();
        Plurk_Users user = plurk_users.get(groupPosition);
        holder.tv_id.setText(user.getId());


        String showTitle = (user.getDisplay_name().equals("")) ? user.getFull_name() : user.getDisplay_name();
        holder.tv_title.setText(showTitle);

        String imgURL = user.getIconAddress();

        mImageFetcher.loadImage(imgURL, holder.iv_image);

        return rowView;
    }

    static class ViewHolderChild {
        public TextView tv_id;
        public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_posted;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            rowView = activity.getLayoutInflater().inflate(R.layout.chatrooms_child_cell, null);
            ViewHolderChild holder = new ViewHolderChild();
            holder.tv_id = (TextView) rowView.findViewById(R.id.uid);
            holder.iv_image = (ImageView) rowView.findViewById(R.id.image);
            holder.tv_title = (TextView) rowView.findViewById(R.id.title);
            holder.tv_posted = (TextView) rowView.findViewById(R.id.posted);
            rowView.setTag(holder);
        }

        final ViewHolderChild holder = (ViewHolderChild) rowView.getTag();
        Plurks plurk = plurks.get(groupPosition).get(childPosition);
        holder.tv_id.setText(plurk.getPlurk_id());
        holder.tv_title.setText(Html.fromHtml(plurk.getContent()));
        holder.tv_posted.setText(plurk.getReadablePostedDate());

        return rowView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
