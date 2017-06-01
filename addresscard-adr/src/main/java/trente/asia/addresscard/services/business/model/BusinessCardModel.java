package trente.asia.addresscard.services.business.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by tien on 5/10/2017.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class BusinessCardModel extends AddressCardModel{
    @JsonField
    public int customerId;
    @JsonField
    public String customerName;
}
