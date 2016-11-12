package idv.funnybrain.plurkchat.model;

import idv.funnybrain.plurkchat.R;

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
        return this.relationship;
    }

    public int getReadableStringResources() {
        switch (this) {
            case NOT_SAYING:
                return R.string.not_saying;
            case SINGLE:
                return R.string.single;
            case MARRIED:
                return R.string.married;
            case DIVORCED:
                return R.string.divorced;
            case ENGAGED:
                return R.string.engaged;
            case IN_RELATIONSHIP:
                return R.string.in_relationship;
            case COMPLICATED:
                return R.string.complicated;
            case WIDOWED:
                return R.string.widowed;
            case UNSTABLE_RELATIONSHIP:
                return R.string.unstable_relatioinship;
            case OPEN_RELATIONSHIP:
                return R.string.open_relationship;
            default:
                return R.string.not_saying;
        }
    }
}