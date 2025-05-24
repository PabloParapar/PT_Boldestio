package script;

import java.util.LinkedList;


public class Tour {

    //@CsvBindByPosition(position = 1)
    private String externalID;
    //@CsvBindByPosition(position = 4)
    private String title_es;
    //@CsvBindByPosition(position = 5)
    private String htmlDescription_es;
    //@CsvBindByPosition(position = 6)
    private String htmlContent_es;
    private LinkedList<Day> program;

    public Tour(String id, String title, String htmlDescription, String htmlContent) {

        this.externalID = id;
        this.title_es = title;
        this.htmlDescription_es = htmlDescription;
        this.htmlContent_es = htmlContent;
        this.program = new LinkedList<Day>();
    }

    public void annadirDay(Day day){
        this.program.add(day);
    }
}
