package idv.funnybrain.plurkchat.model;

/**
 * Created by freeman on 2014/7/23.
 *
 * FIXME SHOULD USE THIS CLASS, NOT IHuman
 *
 */
public abstract class Human {
    protected String id;
    protected String display_name;
    protected String full_name;
    protected String nick_name;
    protected int has_profile_image = 0;
    protected String avatar = "null";

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
        String imgURL = "http://www.plurk.com/static/default_big.gif";
        if(has_profile_image>0) {
            if(avatar.equals("null")) {
                imgURL = "http://avatars.plurk.com/" + id + "-big.jpg";
            } else {
                if(avatar.equals("0")) { avatar = ""; }
                imgURL = "http://avatars.plurk.com/" + id + "-big" + avatar + ".jpg";
            }
        }
        return imgURL;
    }
}