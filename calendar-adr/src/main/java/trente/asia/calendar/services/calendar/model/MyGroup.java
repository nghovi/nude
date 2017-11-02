package nguyenhoangviet.vpcorp.calendar.services.calendar.model;

import java.util.List;

import nguyenhoangviet.vpcorp.welfare.adr.models.GroupModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * Created by hviet on 8/21/17.
 */

@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class MyGroup extends GroupModel{

	public List<UserModel> listGroupUser;
}
