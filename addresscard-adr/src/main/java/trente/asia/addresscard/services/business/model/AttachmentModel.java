package trente.asia.addresscard.services.business.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by tien on 5/10/2017.
 */
@JsonObject
public class AttachmentModel {
    public String service;
    @JsonField
    public int key;

    public String fileType;

    public String fileName;

    public String fileUrl;

    public int fileSize;

    public String attachedType;

    public Boolean isOrginal;

}