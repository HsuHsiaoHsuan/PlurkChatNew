package idv.funnybrain.plurkchat.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by freeman on 2014/12/18.
 */
public class User_Info implements IHuman, Parcelable {
    public User_Info() {
    }

    private boolean verified_account = false;
    private int fans_count = 0;
    private Language default_lang = null;
    private boolean setup_facebook_sync = false;
    private String page_title = "";
    private String uid = "";
    private String avatar_big = "";
    private int dateformat = 0;
    private int has_profile_image = 0;
    private int response_count = 0;
    private String avatar_small = "";
    private String full_name = "";
    private String name_color = "";
    private String timezone = "";
    private String id = "";
    private boolean setup_weibo_sync = false;
    private int profile_views = 0;
    private String about = "";
    private String display_name = "";
    private Relationship relationship = null;
    private String privacy = "";
    private String nick_name = "";
    private int gender = 2;
    private int plurks_count = 0;
    private int friends_count = 0;
    private boolean post_anonymous_plurk = false;
    private int avatar = 0;
    private boolean setup_twitter_sync = false;
    private String date_of_birth = "";
    private String location = "";
    private String avatar_medium = "";
    private String accept_private_plurk_from = "";
    private int recruited = 0;
    private int bday_privacy = 0;
    private double karma = 0.0;

    public boolean isVerified_account() {
        return verified_account;
    }

    public void setVerified_account(boolean verified_account) {
        this.verified_account = verified_account;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public Language getDefault_lang() {
        return default_lang;
    }

    public void setDefault_lang(String default_lang) {
        this.default_lang = Language.getLang(default_lang);
    }

    public boolean isSetup_facebook_sync() {
        return setup_facebook_sync;
    }

    public void setSetup_facebook_sync(boolean setup_facebook_sync) {
        this.setup_facebook_sync = setup_facebook_sync;
    }

    public String getPage_title() {
        return page_title;
    }

    public void setPage_title(String page_title) {
        this.page_title = page_title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatar_big() {
        return avatar_big;
    }

    public void setAvatar_big(String avatar_big) {
        this.avatar_big = avatar_big;
    }

    public int getDateformat() {
        return dateformat;
    }

    public void setDateformat(int dateformat) {
        this.dateformat = dateformat;
    }

    public int getHas_profile_image() {
        return has_profile_image;
    }

    public void setHas_profile_image(int has_profile_image) {
        this.has_profile_image = has_profile_image;
    }

    public int getResponse_count() {
        return response_count;
    }

    public void setResponse_count(int response_count) {
        this.response_count = response_count;
    }

    public String getAvatar_small() {
        return avatar_small;
    }

    public void setAvatar_small(String avatar_small) {
        this.avatar_small = avatar_small;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getName_color() {
        return name_color;
    }

    public void setName_color(String name_color) {
        this.name_color = name_color;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSetup_weibo_sync() {
        return setup_weibo_sync;
    }

    public void setSetup_weibo_sync(boolean setup_weibo_sync) {
        this.setup_weibo_sync = setup_weibo_sync;
    }

    public int getProfile_views() {
        return profile_views;
    }

    public void setProfile_views(int profile_views) {
        this.profile_views = profile_views;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = Relationship.getRelationship(relationship);
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getPlurks_count() {
        return plurks_count;
    }

    public void setPlurks_count(int plurks_count) {
        this.plurks_count = plurks_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public boolean isPost_anonymous_plurk() {
        return post_anonymous_plurk;
    }

    public void setPost_anonymous_plurk(boolean post_anonymous_plurk) {
        this.post_anonymous_plurk = post_anonymous_plurk;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public boolean isSetup_twitter_sync() {
        return setup_twitter_sync;
    }

    public void setSetup_twitter_sync(boolean setup_twitter_sync) {
        this.setup_twitter_sync = setup_twitter_sync;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvatar_medium() {
        return avatar_medium;
    }

    public void setAvatar_medium(String avatar_medium) {
        this.avatar_medium = avatar_medium;
    }

    public String getAccept_private_plurk_from() {
        return accept_private_plurk_from;
    }

    public void setAccept_private_plurk_from(String accept_private_plurk_from) {
        this.accept_private_plurk_from = accept_private_plurk_from;
    }

    public int getRecruited() {
        return recruited;
    }

    public void setRecruited(int recruited) {
        this.recruited = recruited;
    }

    public int getBday_privacy() {
        return bday_privacy;
    }

    public void setBday_privacy(int bday_privacy) {
        this.bday_privacy = bday_privacy;
    }

    public double getKarma() {
        return karma;
    }

    public void setKarma(double karma) {
        this.karma = karma;
    }

    @Override
    public String getHumanId() {
        return uid;
    }

    @Override
    public String getHumanName() {
        if(!display_name.equals("") && !display_name.equals("null")) {
            return display_name;
        } else if(!full_name.equals("") && !full_name.equals("null")) {
            return full_name;
        } else {
            return nick_name;
        }
    }

    @Override
    public String getHumanImage() {
        return avatar_big;
    }

    public static final Parcelable.Creator<User_Info> CREATOR = new Parcelable.Creator<User_Info>() {
        public User_Info createFromParcel(Parcel in) {
            return new User_Info(in);
        }

        @Override
        public User_Info[] newArray(int size) {
            return new User_Info[size];
        }
    };

    private User_Info(Parcel in) {
        verified_account = in.readByte() != 0;
        default_lang = Language.valueOf(in.readString());
        setup_facebook_sync = in.readByte() != 0;
        page_title = in.readString();
        uid = in.readString();
        avatar_big = in.readString();
        dateformat = in.readInt();
        has_profile_image = in.readInt();
        response_count = in.readInt();
        avatar_small = in.readString();
        full_name = in.readString();
        name_color = in.readString();
        timezone = in.readString();
        id = in.readString();
        setup_weibo_sync = in.readByte() != 0;
        profile_views = in.readInt();
        about = in.readString();
        display_name = in.readString();
        relationship = Relationship.valueOf(in.readString());
        privacy = in.readString();
        nick_name = in.readString();
        gender = in.readInt();
        plurks_count = in.readInt();
        friends_count = in.readInt();
        post_anonymous_plurk = in.readByte() != 0;
        avatar = in.readInt();
        setup_twitter_sync = in.readByte() != 0;
        date_of_birth = in.readString();
        location = in.readString();
        avatar_medium = in.readString();
        accept_private_plurk_from = in.readString();
        recruited = in.readInt();
        bday_privacy = in.readInt();
        karma = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte)(verified_account ? 1 : 0));
        dest.writeString(default_lang.toString());
        dest.writeByte((byte) (setup_facebook_sync ? 1 : 0));
        dest.writeString(page_title);
        dest.writeString(uid);
        dest.writeString(avatar_big);
        dest.writeInt(dateformat);
        dest.writeInt(has_profile_image);
        dest.writeInt(response_count);
        dest.writeString(avatar_small);
        dest.writeString(full_name);
        dest.writeString(name_color);
        dest.writeString(timezone);
        dest.writeString(id);
        dest.writeByte((byte) (setup_weibo_sync ? 1 : 0));
        dest.writeInt(profile_views);
        dest.writeString(about);
        dest.writeString(display_name);
        dest.writeString(relationship.toString());
        dest.writeString(privacy);
        dest.writeString(nick_name);
        dest.writeInt(gender);
        dest.writeInt(plurks_count);
        dest.writeInt(friends_count);
        dest.writeByte((byte) (post_anonymous_plurk ? 1 : 0));
        dest.writeInt(avatar);
        dest.writeByte((byte) (setup_twitter_sync ? 1 : 0));
        dest.writeString(date_of_birth);
        dest.writeString(location);
        dest.writeString(avatar_medium);
        dest.writeString(accept_private_plurk_from);
        dest.writeInt(recruited);
        dest.writeInt(bday_privacy);
        dest.writeDouble(karma);
    }
}

/*

private boolean verified_account = false;
    private int fans_count = 0;
    private String default_lang = "";
    private boolean setup_facebook_sync = false;
    private String page_title = "";
    private String uid = "";
    private String avatar_big = "";
    private int dateformat = 0;
    private int has_profile_image = 0;
    private int response_count = 0;
    private String avatar_small = "";
    private String full_name = "";
    private String name_color = "";
    private String timezone = "";
    private String id = "";
    private boolean setup_weibo_sync = false;
    private int profile_views = 0;
    private String about = "";
    private String display_name = "";
    private String relationship = "";
    private String privacy = "";
    private String nick_name = "";
    private int gender = 2;
    private int plurks_count = 0;
    private int friends_count = 0;
    private boolean post_anonymous_plurk = false;
    private int avatar = 0;
    private boolean setup_twitter_sync = false;
    private String date_of_birth = "";
    private String location = "";
    private String avatar_medium = "";
    private String accept_private_plurk_from = "";
    private int recruited = 0;
    private int bday_privacy = 0;
    private double karma = 0.0;

 */