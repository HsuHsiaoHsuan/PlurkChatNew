package idv.funnybrain.plurkchat.data;

/**
 * Created by Freeman on 2014/4/3.
 loves
 likes
 shares
 gives
 hates
 wants
 has
 will
 asks
 wishes
 was
 feels
 thinks
 says
 is
 :
 freestyle
 hopes
 needs
 wonders
 */
public enum Qualifier {
    LOVES("loves"),
    LIKES("likes"),
    SHARES("shares"),
    GIVES("gives"),
    HATES("hates"),
    WANTS("wants"),
    HAS("has"),
    WILL("will"),
    ASKS("asks"),
    WISHES("wishes"),
    WAS("was"),
    FEELS("feels"),
    THINKS("thinks"),
    SAYS("says"),
    IS("is"),
    NULL(":"),
    FREESTYLE("freestyle"),
    HOPES("hopes"),
    NEEDS("needs"),
    WONDERS("wonders"),
    WHISPERS("whispers");

    private String qualifier;

    Qualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    public static Qualifier getQualifier(String action) {
        for(Qualifier q : Qualifier.values()) {
            if(q.toString().equals(action)) {
                return q;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return qualifier;
    }
}
