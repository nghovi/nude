package nguyenhoangviet.vpcorp.calendar.services.todo.model;

import java.util.Date;

import com.bluelinelabs.logansquare.annotation.JsonField;

import nguyenhoangviet.vpcorp.calendar.commons.activites.MainClActivity;

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
