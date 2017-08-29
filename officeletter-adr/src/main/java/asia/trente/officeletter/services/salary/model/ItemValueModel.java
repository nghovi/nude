package asia.trente.officeletter.services.salary.model;

/**
 * Created by tien on 8/22/2017.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class ItemValueModel {
    public int key;
    public String value;
    public String itemName;
    public String itemNote;
    public String itemDetail;
    public String itemType;
    public String itemSub;
    public String groupName;
    public boolean isGroup = false;
    public boolean showSub = true;

    public ItemValueModel(String groupName) {
        this.groupName = groupName;
        isGroup = true;
    }

    public ItemValueModel() {
    }


}
