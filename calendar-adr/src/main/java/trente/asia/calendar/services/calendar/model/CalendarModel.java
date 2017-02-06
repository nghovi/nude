package trente.asia.calendar.services.calendar.model;

/**
 * Created by viet on 11/25/2016.
 */

public class CalendarModel{

	public String	name;
	public String	imagePath;

	public CalendarModel(){

	}

    public CalendarModel(String name, String imagePath){
        this.name = name;
        this.imagePath = imagePath;
    }
}
