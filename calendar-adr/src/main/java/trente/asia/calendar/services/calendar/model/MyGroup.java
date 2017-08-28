package trente.asia.calendar.services.calendar.model;

import java.util.List;

import trente.asia.welfare.adr.models.GroupModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by hviet on 8/21/17.
 */

@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class MyGroup extends GroupModel{

	public List<UserModel> listGroupUser;
}
