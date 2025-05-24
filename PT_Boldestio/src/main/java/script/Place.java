package script;

public class Place {

    private String externalID;
    private String submap;
    private transient String day;
    private String title_es;
    private String htmlDescription_es;
    private String htmlContent_es;
    //private String externalUrl;
    //private String contactCity;
    //private String contactProvince;
    private String locationLat;
    private String locationLon;

    public Place(String id, String subrecurso, String day, String title, String htmlDescription, String htmlContent, String locationLat, String locationLon) {
        this.externalID = id;
        this.submap = subrecurso;
        this.day = day;
        this.title_es = title;
        this.htmlDescription_es = htmlDescription;
        this.htmlContent_es = htmlContent;
        this.locationLat = locationLat;
        this.locationLon = locationLon;
    }
}
