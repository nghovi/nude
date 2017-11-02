package nguyenhoangviet.vpcorp.calendar.services.calendar.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by trungnd on 3/2/17.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class CategoryModel{

	public String	key;

	public String	categoryName;

	public String	categoryColor;

	public String	categoryNote;

	public int		userId;
	public String	hoursOfSchedule;

	public CategoryModel(String colorCode){
		this.categoryColor = colorCode;
	}

	public CategoryModel(){
	}
}
