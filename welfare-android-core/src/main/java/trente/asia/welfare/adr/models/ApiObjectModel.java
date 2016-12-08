package trente.asia.welfare.adr.models;

import java.io.Serializable;

/**
 * ApiObjectModel
 *
 * @author TrungND
 */
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
