package Criminel;

public class Crime {

    private int id;
    private int peine;
    private String intitule;

    public Crime(int peine, String intitule) {
        this.id = 0;
        this.peine = peine;
        this.intitule = intitule;
    }

    public void incrementeId(){
        this.id++;
    }

    public int getPeine() {
        return this.peine;
    }

    public int getId() {
        return this.id;
    }

    public String getIntitule() {
        return this.intitule;
    }
}
