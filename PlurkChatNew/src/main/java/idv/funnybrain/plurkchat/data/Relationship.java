package idv.funnybrain.plurkchat.data;

/**
 * Created by Freeman on 2014/4/3.
 * not_saying
 * single
 * married
 * divorced
 * engaged
 * in_relationship
 * complicated
 * widowed
 * unstable_relationship
 * open_relationship
 */
public enum Relationship {
    NOT_SAYING("not_saying"),
    SINGLE("single"),
    MARRIED("married"),
    DIVORCED("divorced"),
    ENGAGED("engaged"),
    IN_RELATIONSHIP("in_relationship"),
    COMPLICATED("complicated"),
    WIDOWED("widowed"),
    UNSTABLE_RELATIONSHIP("unstable_relationship"),
    OPEN_RELATIONSHIP("open_relationship");


    private String relationship;

    Relationship(String relationship) {
        this.relationship = relationship;
    }

    public static Relationship getRelationship(String action) {
        for(Relationship r : Relationship.values()) {
            if(r.toString().equals(action)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return relationship;
    }
}
