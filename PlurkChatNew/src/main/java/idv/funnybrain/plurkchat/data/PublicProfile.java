package idv.funnybrain.plurkchat.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by freeman on 2014/12/18.
 */
public class PublicProfile implements IProfile {
    private boolean is_fan = false;
    private int fans_count = 0;
    private int friends_count = 0;
    private String privacy = "";
    private boolean has_read_permission = false;
    private User_Info user_info = null;
    private List<Plurks> plurks = new ArrayList<Plurks>();

    public boolean isIs_fan() {
        return is_fan;
    }

    public void setIs_fan(boolean is_fan) {
        this.is_fan = is_fan;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public boolean isHas_read_permission() {
        return has_read_permission;
    }

    public void setHas_read_permission(boolean has_read_permission) {
        this.has_read_permission = has_read_permission;
    }

    public User_Info getUser_info() {
        return user_info;
    }

    public void setUser_info(User_Info user_info) {
        this.user_info = user_info;
    }

    public List<Plurks> getPlurks() {
        return plurks;
    }

    public void setPlurks(List<Plurks> plurks) {
        this.plurks = plurks;
    }

    @Override
    public int getFriendsCount() {
        return friends_count;
    }

    @Override
    public int getFansCount() {
        return fans_count;
    }

    @Override
    public User_Info getUserInfo() {
        return null;
    }

    @Override
    public List<Plurks> getFormattedPlurks() {
        return null;
    }
}

/*
{
    "is_fan": false,
    "fans_count": 5,
    "friends_count": 21,
    "privacy": "world",
    "has_read_permission": true,
    "user_info": {
        "verified_account": false,
        "fans_count": 5,
        "default_lang": "tr_ch",
        "setup_facebook_sync": false,
        "page_title": "0Mission0's Plurk",
        "uid": 8120302,
        "avatar_big": "http://avatars.plurk.com/8120302-big2.jpg",
        "dateformat": 0,
        "has_profile_image": 1,
        "response_count": 14266,
        "avatar_small": "http://avatars.plurk.com/8120302-small2.gif",
        "full_name": "0Mission0",
        "name_color": null,
        "timezone": null,
        "id": 8120302,
        "setup_weibo_sync": false,
        "profile_views": 2116,
        "about": "I can accept failure. But I can't accept not trying.",
        "display_name": "咪咪獎",
        "relationship": "single",
        "privacy": "world",
        "nick_name": "0Mission0",
        "gender": 1,
        "plurks_count": 1441,
        "friends_count": 21,
        "post_anonymous_plurk": false,
        "avatar": 2,
        "setup_twitter_sync": false,
        "date_of_birth": "Tue, 14 Feb 1984 00:01:00 GMT",
        "location": "Hsinchu, Taiwan",
        "avatar_medium": "http://avatars.plurk.com/8120302-medium2.jpg",
        "accept_private_plurk_from": "all",
        "recruited": 1,
        "bday_privacy": 2,
        "karma": 96.65
    },
    "are_friends": true,
    "is_following": true,
    "friend_status": 1,
    "plurks": [
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246882413,
            "response_count": 1,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "搞定，回家睡覺～",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "搞定，回家睡覺～",
            "replurker_id": null,
            "posted": "Wed, 17 Dec 2014 15:06:21 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246861317,
            "response_count": 4,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "這幾天剛上任的新同事，禮拜一請了病假",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "這幾天剛上任的新同事，禮拜一請了病假",
            "replurker_id": null,
            "posted": "Wed, 17 Dec 2014 12:52:53 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246857091,
            "response_count": 0,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "「今日放送」\nhttps://www.youtube.com/watch?v=abzbVFuxigg",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "「今日放送」<br /><a href=\"https://www.youtube.com/watch?v=abzbVFuxigg\" class=\"ogvideo meta\" rel=\"nofollow\"><img src=\"https://i.ytimg.com/vi/abzbVFuxigg/hqdefault.jpg\" data-video=\"http://www.youtube.com/embed/abzbVFuxigg?feature=oembed\" data-width=\"480\" data-height=\"270\" height=\"40px\" />Bon Jovi - Who Says You Can't Go Home</a>",
            "replurker_id": null,
            "posted": "Wed, 17 Dec 2014 12:20:57 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246713091,
            "response_count": 8,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "神腦的同事滿怒抱怨中",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "神腦的同事滿怒抱怨中",
            "replurker_id": null,
            "posted": "Tue, 16 Dec 2014 13:07:10 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246701296,
            "response_count": 1,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "最近有點太鬆懈(？)",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "最近有點太鬆懈(？)",
            "replurker_id": null,
            "posted": "Tue, 16 Dec 2014 11:37:48 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246673336,
            "response_count": 2,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "提早下班又有香蕉飴可以吃，好棒 =w=",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "提早下班又有香蕉飴可以吃，好棒 =w=",
            "replurker_id": null,
            "posted": "Tue, 16 Dec 2014 07:30:43 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246641790,
            "response_count": 5,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "跟Paypal合作不曉得會不會贈送一個有儲值的帳號 :P",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "跟Paypal合作不曉得會不會贈送一個有儲值的帳號 <img src=\"https://s.plurk.com/2d5e21929e752498e36d74096b1965e1.gif\" class=\"emoticon\" alt=\":-P\" />",
            "replurker_id": null,
            "posted": "Tue, 16 Dec 2014 02:34:16 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246582534,
            "response_count": 10,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "還沒睡著的人，歡迎在底下留言，咪咪獎今夜跟各位在空中相會",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "還沒睡著的人，歡迎在底下留言，咪咪獎今夜跟各位在空中相會",
            "replurker_id": null,
            "posted": "Mon, 15 Dec 2014 15:03:40 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246568092,
            "response_count": 2,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "咪咪咪咪…………(陷入加班地獄，語焉不詳)",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "咪咪咪咪…………(陷入加班地獄，語焉不詳)",
            "replurker_id": null,
            "posted": "Mon, 15 Dec 2014 13:39:18 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246550588,
            "response_count": 2,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "禮拜一，不免俗的要開始加班了……",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "禮拜一，不免俗的要開始加班了……",
            "replurker_id": null,
            "posted": "Mon, 15 Dec 2014 11:36:33 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246411403,
            "response_count": 0,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "沒有古巴三明治，不過附近剛好有餐車，就買了一個軟法豬排蛋試試看 :P",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "沒有古巴三明治，不過附近剛好有餐車，就買了一個軟法豬排蛋試試看 <img src=\"https://s.plurk.com/2d5e21929e752498e36d74096b1965e1.gif\" class=\"emoticon\" alt=\":-P\" />",
            "replurker_id": null,
            "posted": "Sun, 14 Dec 2014 12:36:54 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246397434,
            "response_count": 2,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "「今日放送」\nhttps://www.youtube.com/watch?v=ye5BuYf8q4o",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "「今日放送」<br /><a href=\"https://www.youtube.com/watch?v=ye5BuYf8q4o\" class=\"ogvideo meta\" rel=\"nofollow\"><img src=\"https://i.ytimg.com/vi/ye5BuYf8q4o/hqdefault.jpg\" data-video=\"http://www.youtube.com/embed/ye5BuYf8q4o?feature=oembed\" data-width=\"459\" data-height=\"344\" height=\"40px\" />Lynyrd Skynyrd - Sweet Home Alabama</a>",
            "replurker_id": null,
            "posted": "Sun, 14 Dec 2014 10:29:27 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246396110,
            "response_count": 1,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 1,
            "lang": "tr_ch",
            "favorers": [
                3403712
            ],
            "content_raw": "正在看「五星主廚快餐車」，突然好想大口地咬一口起司啊 >_<",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "正在看「五星主廚快餐車」，突然好想大口地咬一口起司啊 &gt;_&lt;",
            "replurker_id": null,
            "posted": "Sun, 14 Dec 2014 10:15:10 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246373170,
            "response_count": 0,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "「今日放送」https://www.youtube.com/watch?v=ZGoWtY_h4xo",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "「今日放送」<a href=\"https://www.youtube.com/watch?v=ZGoWtY_h4xo\" class=\"ex_link meta\" rel=\"nofollow\"><img src=\"https://i.ytimg.com/vi/ZGoWtY_h4xo/hqdefault.jpg\" height=\"40px\" />Bryan Adams - (Everything I Do) I Do It For You</a>",
            "replurker_id": null,
            "posted": "Sun, 14 Dec 2014 06:04:38 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246350829,
            "response_count": 15,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "看能不能用三個小時左右把今天的工作告一個段落",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "看能不能用三個小時左右把今天的工作告一個段落",
            "replurker_id": null,
            "posted": "Sun, 14 Dec 2014 01:36:38 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246313816,
            "response_count": 0,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "http://www.ettoday.net/news/20141213/438338.htm?from=fb_et_pets\n可愛到太犯規了 (lmao)",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "<a href=\"http://www.ettoday.net/news/20141213/438338.htm?from=fb_et_pets\" class=\"ex_link meta\" rel=\"nofollow\"><img src=\"http://static.ettoday.net/images/853/d853403.jpg\" height=\"40px\" />「呆萌天竺鼠」守秩序排隊過橋　網友笑：出貨囉～</a>可愛到太犯規了 <img src=\"https://s.plurk.com/92b595a573d25dd5e39a57b5d56d4d03.gif\" class=\"emoticon\" alt=\"(lmao)\" />",
            "replurker_id": null,
            "posted": "Sat, 13 Dec 2014 16:10:24 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246307001,
            "response_count": 4,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "糟糕啦～～～加班到現在會不會趕不上捷運 @@\"",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "糟糕啦～～～加班到現在會不會趕不上捷運 @@\"",
            "replurker_id": null,
            "posted": "Sat, 13 Dec 2014 15:26:50 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246281487,
            "response_count": 1,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "「今日放送」https://www.youtube.com/watch?v=QSN4M-zQcH8",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "「今日放送」<a href=\"https://www.youtube.com/watch?v=QSN4M-zQcH8\" class=\"ogvideo meta\" rel=\"nofollow\"><img src=\"https://i.ytimg.com/vi/QSN4M-zQcH8/hqdefault.jpg\" data-video=\"http://www.youtube.com/embed/QSN4M-zQcH8?feature=oembed\" data-width=\"480\" data-height=\"270\" height=\"40px\" />[City Hunter 91 OAS] Smile & Smile [HD]</a>",
            "replurker_id": null,
            "posted": "Sat, 13 Dec 2014 12:24:49 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246179464,
            "response_count": 0,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "http://www.gamecity.ne.jp/shop/sangokushi/30th/\n三國志30週年咧……1~12代，突然有點心動 (evilsmirk)",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "<a href=\"http://www.gamecity.ne.jp/shop/sangokushi/30th/\" class=\"ex_link\" rel=\"nofollow\">GAMECITY [オンラインショッピング]</a><br />三國志30週年咧……1~12代，突然有點心動 <img src=\"https://s.plurk.com/22416dced8b59446db8cd366cc925d09.gif\" class=\"emoticon\" alt=\"(evilsmirk)\" />",
            "replurker_id": null,
            "posted": "Fri, 12 Dec 2014 15:47:01 GMT",
            "owner_id": 8120302
        },
        {
            "responses_seen": 0,
            "qualifier": "says",
            "replurkers": [],
            "plurk_id": 1246167209,
            "response_count": 8,
            "replurkers_count": 0,
            "anonymous": false,
            "replurkable": true,
            "limited_to": null,
            "favorite_count": 0,
            "lang": "tr_ch",
            "favorers": [],
            "content_raw": "又～～～被Larry訓了一頓。",
            "user_id": 8120302,
            "plurk_type": 0,
            "qualifier_translated": "說",
            "replurked": null,
            "no_comments": 0,
            "content": "又～～～被Larry訓了一頓。",
            "replurker_id": null,
            "posted": "Fri, 12 Dec 2014 14:32:44 GMT",
            "owner_id": 8120302
        }
    ]
}
 */