package Criminel;

public class Crime {

    private int id;
    private int peine;
    private String intitule;
    private int maxId = 0;

    public Crime(int peine, String intitule) {
        this.id = this.maxId;
        this.peine = peine;
        this.intitule = intitule;
        incrementeMaxId();
    }

    public void incrementeMaxId(){
        this.maxId++;
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

    public int getMaxId() {
        return this.maxId;
    }
}
