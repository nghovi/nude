package nguyenhoangviet.vpcorp.android.item;

import android.util.Pair;

/**
 * <strong>KeyValuePair</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class KeyValuePair extends Pair<String, String>{

	public KeyValuePair(String key, String value){
		super(key, value);
	}

	public String getKey(){
		return super.first;
	}

	public String getValue(){
		return super.second;
	}
}
