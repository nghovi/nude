package trente.asia.addresscard.services.card.model;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by tien on 5/10/2017.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Attachment {
    public String service;
    public int key;
    public String fileType;
    public String fileName;
    public String fileUrl;
    public int fileSize;
    public String attachedType;
    public Boolean isOrginal;
}