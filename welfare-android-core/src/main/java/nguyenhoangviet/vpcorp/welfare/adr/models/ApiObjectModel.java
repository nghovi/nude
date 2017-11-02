package nguyenhoangviet.vpcorp.welfare.adr.models;

import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * ApiObjectModel
 *
 * @author TrungND
 */

@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class ApiObjectModel{

	public String	key;
	public String	value;

    public ApiObjectModel(){

    }

    public ApiObjectModel(String key, String value){
        this.key = key;
        this.value = value;
    }
}
