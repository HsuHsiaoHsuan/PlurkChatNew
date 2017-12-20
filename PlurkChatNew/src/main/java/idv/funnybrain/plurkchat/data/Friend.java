package idv.funnybrain.plurkchat.data;

/**
 * Created by Freeman on 2014/4/5.
 "email_confirmed":true,
 "uid":1367985,
 "following_tl":1,
 "location":"Taipei, Taiwan",
 "verified_account":false,
 "settings":true,
 "following_im":1,
 "recruited":17,
 "date_of_birth":"Thu, 06 Oct 1983 00:01:00 GMT",
 "avatar":0,
 "nick_name":"brianhsu",
 "relationship":"single",
 "id":1367985,
 "karma":128.46,
 "display_name":"墳墓（Brian Hsu）"
 ,"name_color":null,
 "following":true,
 "timezone":"Asia\/Taipei",
 "dateformat":0,
 "bday_privacy":2,
 "gender":1,
 "has_profile_image":1,
 "default_lang":"tr_ch",
 "full_name":"BrianHsu"
 *
 */

public class Friend implements IHuman {
    private boolean email_confirmed;
    private String uid;
    private int following_tl;
    private String location;
    private boolean verified_account;
    private boolean settings; // FIXME
    private int following_im;
    private int recruited;
    private String date_of_birth;
    private String avatar;
    private String nick_name;
    private String relationship;
    private String id;
    private double karma;
    private String display_name;
    private String name_color; // FIXME
    private boolean following;
    private String timezone;
    private int dateformat;
    private int bday_privacy;
    private int gender; // 1 is male, 0 is female, 2 is not stating/other.
    private int has_profile_image;
    private String default_lang;
    private String full_name;

    public boolean isEmail_confirmed() {
        return email_confirmed;
    }

    public void setEmail_confirmed(boolean email_confirmed) {
        this.email_confirmed = email_confirmed;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getFollowing_tl() {
        return following_tl;
    }

    public void setFollowing_tl(int following_tl) {
        this.following_tl = following_tl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isVerified_account() {
        return verified_account;
    }

    public void setVerified_account(boolean verified_account) {
        this.verified_account = verified_account;
    }

    public boolean isSettings() {
        return settings;
    }

    public void setSettings(boolean settings) {
        this.settings = settings;
    }

    public int getFollowing_im() {
        return following_im;
    }

    public void setFollowing_im(int following_im) {
        this.following_im = following_im;
    }

    public int getRecruited() {
        return recruited;
    }

    public void setRecruited(int recruited) {
        this.recruited = recruited;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getKarma() {
        return karma;
    }

    public void setKarma(double karma) {
        this.karma = karma;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getName_color() {
        return name_color;
    }

    public void setName_color(String name_color) {
        this.name_color = name_color;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getDateformat() {
        return dateformat;
    }

    public void setDateformat(int dateformat) {
        this.dateformat = dateformat;
    }

    public int getBday_privacy() {
        return bday_privacy;
    }

    public void setBday_privacy(int bday_privacy) {
        this.bday_privacy = bday_privacy;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getHas_profile_image() {
        return has_profile_image;
    }

    public void setHas_profile_image(int has_profile_image) {
        this.has_profile_image = has_profile_image;
    }

    public String getDefault_lang() {
        return default_lang;
    }

    public void setDefault_lang(String default_lang) {
        this.default_lang = default_lang;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getHumanId() {
        return id;
    }

    public String getHumanName() {
        if(!display_name.equals("") && !display_name.equals("null")) {
            return display_name;
        } else if(!full_name.equals("") && !full_name.equals("null")) {
            return full_name;
        } else {
            return nick_name;
        }
    }

    public String getHumanImage() {
        String imgURL = "https://www.plurk.com/static/default_big.gif";
        if(has_profile_image>0) {
            if(avatar.equals("null")) {
                imgURL = "https://avatars.plurk.com/" + id + "-big.jpg";
            } else {
                if(avatar.equals("0")) { avatar = ""; }
                imgURL = "https://avatars.plurk.com/" + id + "-big" + avatar + ".jpg";
            }
        }
        return imgURL;
    }
}