package trente.asia.calendar.services.todo.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by hviet on 7/20/17.
 */

@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Todo{

	public String	key;
	public String	name;
	public String	note;
	public Boolean	isFinish	= false;
	public String	limitDate;
}
