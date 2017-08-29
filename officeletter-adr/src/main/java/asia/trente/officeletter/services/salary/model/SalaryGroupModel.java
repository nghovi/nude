package asia.trente.officeletter.services.salary.model;

import java.util.List;

/**
 * Created by tien on 8/22/2017.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class SalaryGroupModel {
    public int key;
    public String groupName;
    public List<ItemValueModel> itemValues;

    public SalaryGroupModel() {
    }
}
