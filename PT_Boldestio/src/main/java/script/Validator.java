package script;

import java.util.ArrayList;

public class Validator {
    ArrayList<Tour> syncData;
    String $schema;

    public Validator(String schema) {
        this.syncData = new ArrayList<>();
        this.$schema = schema;
    }

    public void annadirTour(Tour tour){
        this.syncData.add(tour);
    }
}
