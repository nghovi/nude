package asia.trente.officeletter.services.salary.model;

/**
 * Created by tien on 8/22/2017.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class PasswordModel {
    public int key;
    public String passwordName;
    public String passwordHint;
    public String passwordApp;
    public String passwordMail;

    public PasswordModel() {
    }
}
