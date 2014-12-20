package idv.funnybrain.plurkchat.ui;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import idv.funnybrain.plurkchat.DataCentral;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.data.IHuman;
import idv.funnybrain.plurkchat.data.Me;
import idv.funnybrain.plurkchat.utils.ImageFetcher;

import java.util.List;

/**
 * Created by Freeman on 2014/4/12.
 */
public class MeFriendsFollowingExpandableListAdapter extends BaseExpandableListAdapter {
    // ---- constants START ----
    private static final boolean D = true;
    private static final String TAG = "MeFriendsFollowingExpandableListAdapter";
    // ---- constants END ----

    // --- local variable START ----
    private LayoutInflater inflater;
    private List<String> group;
    private List<List<IHuman>> child;
//    private ImageFetcher mImageFetcher;
    private DataCentral mData;
    private ImageLoader mImageLoader;
    // --- local variable END ----

    public MeFriendsFollowingExpandableListAdapter(LayoutInflater inflater, List<String> group, List<List<IHuman>> child/*, ImageFetcher mImageFetcher*/) {
        this.inflater = inflater;
        this.group = group;
        this.child = child;
//        this.mImageFetcher = mImageFetcher;
        mData = DataCentral.getInstance(inflater.getContext());
        mImageLoader = mData.getImageLoader();
    }

    @Override
    public int getGroupCount() {
        //return group.size();
        return child.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return Long.valueOf(child.get(groupPosition).get(childPosition).getHumanId());
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class ViewHolderGroup {
        public TextView tv_title;
        public TextView tv_count;
    }
    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, final ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            rowView = this.inflater.inflate(R.layout.me_friend_following_group_cell, null);
            ViewHolderGroup holder = new ViewHolderGroup();
            holder.tv_title = (TextView) rowView.findViewById(R.id.tv_title);
            holder.tv_count = (TextView) rowView.findViewById(R.id.tv_count);
            rowView.setTag(holder);
        }

        final ViewHolderGroup holder = (ViewHolderGroup) rowView.getTag();
        holder.tv_title.setText(group.get(groupPosition));

        if(groupPosition != 0) {
            holder.tv_count.setText("(" + child.get(groupPosition).size() + ")");
            holder.tv_count.setVisibility(View.VISIBLE);
        }

        //((ExpandableListView) parent).expandGroup(groupPosition);

        return rowView;
    }

    static class ViewHolderChild {
        public TextView tv_id;
//        public ImageView iv_image;
        public NetworkImageView iv_image;
//        public RoundedImageView riv_image;
        public TextView tv_title;
        public TextView tv_about;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            rowView = this.inflater.inflate(R.layout.me_friend_following_child_cell, null);
            ViewHolderChild holder = new ViewHolderChild();
            holder.tv_id = (TextView) rowView.findViewById(R.id.uid);
            holder.iv_image = (NetworkImageView) rowView.findViewById(R.id.image);
//            holder.iv_image = (ImageView) rowView.findViewById(R.id.image);
//            holder.riv_image = (RoundedImageView) rowView.findViewById(R.id.image_v2);
            holder.tv_title = (TextView) rowView.findViewById(R.id.title);
            holder.tv_about = (TextView) rowView.findViewById(R.id.about);
            rowView.setTag(holder);
        }

        final ViewHolderChild holder = (ViewHolderChild) rowView.getTag();
        IHuman data = child.get(groupPosition).get(childPosition);
        holder.tv_id.setText(data.getHumanId());
        holder.iv_image.setImageUrl(data.getHumanImage(), mImageLoader);
//        mImageLoader.get(data.getHumanImage(), ImageLoader.getImageListener(holder.iv_image,
//                R.drawable.default_plurk_avatar,
//                R.drawable.default_plurk_avatar));
//        mImageFetcher.loadImage(data.getHumanImage(), holder.iv_image);
//        mImageFetcher.loadImage(data.getHumanImage(), holder.riv_image);

        holder.tv_title.setText(data.getHumanName());
        if((groupPosition == 0) && (data instanceof Me) ){
            holder.tv_about.setText(((Me) data).getAbout());
            holder.tv_about.setVisibility(View.VISIBLE);
        } else {
            holder.tv_about.setVisibility(View.GONE);
        }

        // rowView.setOnClickListener(childOnClickListener);
        return rowView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

//    private View.OnClickListener childOnClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            ViewHolderChild holder = (ViewHolderChild) view.getTag();
//            System.out.println("you chick: " + holder.tv_id.getText() + holder.tv_title.getText());
//        }
//    };

    public void addNewData(String groupName, List<IHuman> child_data) {
//        if(group.contains(groupName)) {
//            int idx = group.indexOf(groupName);
//            group.remove(idx);
//            group.add(idx, groupName);
//            child.remove(idx);
//            child.add(idx, child_data);
//        } else {
//            group.add(groupName);
//            child.add(child_data);
//        }
        notifyDataSetChanged();
    }
}
