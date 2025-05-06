package Criminel;

import com.google.gson.annotations.Expose;

public class Crime {

    @Expose private int peine;
    @Expose private String intitule;

    public Crime(int peine, String intitule) {
        this.peine = peine;
        this.intitule = intitule;
    }

    public int getPeine() {
        return this.peine;
    }

    public String getIntitule() {
        return this.intitule;
    }

}
