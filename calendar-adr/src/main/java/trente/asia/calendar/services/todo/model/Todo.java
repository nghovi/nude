package trente.asia.calendar.services.todo.model;

import java.util.Date;

import com.bluelinelabs.logansquare.annotation.JsonField;

import trente.asia.calendar.commons.activites.MainClActivity;

/**
 * Created by hviet on 7/20/17.
 */

@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Todo{

	public String	key;
	public String	name;
	public String	note;
	public Boolean	isFinish	= false;
	@JsonField(typeConverter = MainClActivity.WelfareTimeConverter.class)
	public Date		limitDate;
}
