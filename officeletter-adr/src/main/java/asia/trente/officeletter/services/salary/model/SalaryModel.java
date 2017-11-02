package asia.trente.officeletter.services.salary.model;

import java.util.List;

import nguyenhoangviet.vpcorp.welfare.adr.models.AttachmentModel;

/**
 * Created by tien on 8/21/2017.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class SalaryModel{

	public int						key;
	public String					salaryMonth;
	public String					salaryStatus;
	public String					deliveryDate;
	public List<SalaryGroupModel>	salaryGroups;
	public PasswordModel			password;
	public AttachmentModel			attachment;

	public SalaryModel(){
	}
}
