package Criminel;

public class Crime {

    private int id;
    private int peine;

    public Crime(int id, int peine) {
        this.id = id;
        this.peine = peine;
    }

    public int getPeine() {
        return this.peine;
    }

    public int getId() {
        return this.id;
    }
}
