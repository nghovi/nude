package trente.asia.calendar.services.summary;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

import trente.asia.calendar.services.calendar.model.CategoryModel;

/**
 * Created by viet on 3/22/2017.
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class SummaryModel{

	String				month;
	List<CategoryModel>	categories;
}
