package idv.funnybrain.plurkchat.ui;

import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import idv.funnybrain.plurkchat.R;
import idv.funnybrain.plurkchat.data.Plurk_Users;
import idv.funnybrain.plurkchat.data.Plurks;
import idv.funnybrain.plurkchat.utils.ImageFetcher;

import java.util.*;

/**
 * Created by Freeman on 2014/4/16.
 */
public class ChatRoomExpandableListAdapter_v2 extends BaseExpandableListAdapter {
    // ---- constants START ----
    private static final boolean D = true;
    private static final String TAG = "ChatRoomExpandableListAdapter_v2";
    // ---- constants END ----

    // --- local variable START ----
    private LayoutInflater inflater;
    private List<Plurk_Users> group;
    private HashMap<String, List<Plurks>> plurks;
    private ImageFetcher mImageFetcher;

//    final Html.ImageGetter imageGetter = new Html.ImageGetter() {
//
//        @Override
//        public Drawable getDrawable(String source) {
//
//            return drawable;
//        }
//    };
    // --- local variable END ----

    public ChatRoomExpandableListAdapter_v2(LayoutInflater inflater, HashMap<String, Plurk_Users> users, HashMap<String, List<Plurks>> plurks, ImageFetcher imageFetcher) {
        this.inflater = inflater;
        this.mImageFetcher = imageFetcher;
        this.plurks = plurks;

        group = new ArrayList<Plurk_Users>();
        Iterator<String> plurks_iter = plurks.keySet().iterator();
        while(plurks_iter.hasNext()) {
            String id = plurks_iter.next();
            group.add(users.get(id));
        }
        Collections.sort(group, new Comparator<Plurk_Users>() {
            @Override
            public int compare(Plurk_Users lhs, Plurk_Users rhs) {
                return (Integer.valueOf(lhs.getHumanId()) - Integer.valueOf(rhs.getHumanId()));
            }
        });
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return plurks.get(group.get(groupPosition).getHumanId()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return plurks.get(group.get(groupPosition).getHumanId()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return Long.valueOf(group.get(groupPosition).getHumanId());
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return Long.valueOf(plurks.get(group.get(groupPosition).getHumanId()).get(childPosition).getPlurk_id());
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class ViewHolderGroup {
        public TextView tv_id;
        public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_count;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            rowView = this.inflater.inflate(R.layout.chatrooms_group_cell, null);
            ViewHolderGroup holder = new ViewHolderGroup();
            holder.tv_id = (TextView) rowView.findViewById(R.id.uid);
            holder.iv_image = (ImageView) rowView.findViewById(R.id.image);
            holder.tv_title = (TextView) rowView.findViewById(R.id.title);
            holder.tv_count = (TextView) rowView.findViewById(R.id.msg_count);
            rowView.setTag(holder);
        }

        final ViewHolderGroup holder = (ViewHolderGroup) rowView.getTag();
        Plurk_Users user = group.get(groupPosition);
        holder.tv_id.setText(user.getHumanId());
        holder.tv_title.setText(user.getHumanName());
        holder.tv_count.setText("(" + plurks.get(user.getHumanId()).size() + ")");

        String imgURL = user.getHumanImage();

        mImageFetcher.loadImage(imgURL, holder.iv_image);

        if(isExpanded) {
            rowView.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.blue_light));
            holder.tv_title.setTextColor(Color.WHITE);
            holder.tv_count.setTextColor(Color.WHITE);
        } else {
            rowView.setBackgroundColor(inflater.getContext().getResources().getColor(R.color.abs__background_holo_light));
            holder.tv_title.setTextColor(Color.BLACK);
            holder.tv_count.setTextColor(Color.BLACK);
        }

        return rowView;
    }

    static class ViewHolderChild {
        public TextView tv_id;
        public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_posted;
        public TextView tv_count;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            rowView = this.inflater.inflate(R.layout.chatrooms_child_cell, null);
            ViewHolderChild holder = new ViewHolderChild();
            holder.tv_id = (TextView) rowView.findViewById(R.id.uid);
            holder.iv_image = (ImageView) rowView.findViewById(R.id.image);
            holder.tv_title = (TextView) rowView.findViewById(R.id.title);
//            holder.tv_title.setMovementMethod(LinkMovementMethod.getInstance());
            holder.tv_posted = (TextView) rowView.findViewById(R.id.posted);
            holder.tv_count = (TextView) rowView.findViewById(R.id.count);
            rowView.setTag(holder);
        }

        final ViewHolderChild holder = (ViewHolderChild) rowView.getTag();
        Plurks plurk = plurks.get(group.get(groupPosition).getHumanId()).get(childPosition);
        holder.tv_id.setText(plurk.getPlurk_id());
        holder.tv_title.setText(Html.fromHtml(plurk.getContent()));
        holder.tv_posted.setText(plurk.getReadablePostedDate());
        holder.tv_count.setText(inflater.getContext().getString(R.string.response) + plurk.getResponse_count());
        return rowView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

//    public void addNewData(HashMap<String, Plurk_Users> users, HashMap<String, List<Plurks>> plurks) {
    public void addNewData() {
//        List<String> tmp_idx = new ArrayList<String>();
//        for(Plurk_Users pu: group) {
//            tmp_idx.add(pu.getHumanId());
//        }
//
//        Iterator<String> iterator = plurks.keySet().iterator();
//        while(iterator.hasNext()) {
//            String idx = iterator.next();
//            if(tmp_idx.contains(idx)) {
//                plurks.get(idx).addAll(plurks.get(idx));
//            } else {
//                group.add(users.get(idx));
//                plurks.put(idx, plurks.get(idx));
//            }
//        }
        notifyDataSetChanged();
    }
}