package script;

import java.util.LinkedList;

public class Day {
    private String day;
    private String title_es;
    private String htmlDescription_es;
    private String htmlContent_es;
    private LinkedList<Place> resourceIDs;

    public Day(String day, String title, String htmlDescription, String htmlContent) {
        this.day = day;
        this.title_es = title;
        this.htmlDescription_es = htmlDescription;
        this.htmlContent_es = htmlContent;
        this.resourceIDs = new LinkedList<Place>();
    }

    public void annadirPlace(Place place){
        this.resourceIDs.add(place);
    }
}
