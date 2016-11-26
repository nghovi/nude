package trente.asia.android.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.define.CAConst;

//import org.apache.http.conn.ClientConnectionManager;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;

/**
 * <strong>CCAndroidUtil</strong><br>
 * <br>
 * 
 * @author TrungND
 * @version $Id$
 */
public class CAMsgUtil{

	public static Map<String, String> map(Activity activity, Map<String, String> map){

		Map<String, String> languageMap = new LinkedHashMap<String, String>();
		for(String key : map.keySet()){
			languageMap.put(key, message(activity, map.get(key)));
		}

		return languageMap;
	}

	public static String message(Context context, String key, Object... formatArgs){
		try{
			if(CCStringUtil.isEmpty(key)){
				return "";
			}else{
				Resources resources = context.getResources();
				Integer keyId = resources.getIdentifier(key, "string", context.getPackageName());
				if(keyId != null && !CCConst.ZERO.equals(keyId)){
                    String value = "";
                    if(formatArgs != null){
                        value = resources.getString(keyId, formatArgs);
                    }else{
                        value = resources.getString(keyId);
                    }
					if(value != null){
						key = value;
					}
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return key;
	}

	public static String message(Resources resources, String packageName, String key){
		try{
			if(CCStringUtil.isEmpty(key)){
				return "";
			}else{
				Integer keyId = resources.getIdentifier(key, "string", packageName);
				if(keyId != null && !CCConst.ZERO.equals(keyId)){
					String value = resources.getString(keyId);
					if(value != null){
						key = value;
					}
				}
			}
		}catch(Exception ex){
			Log.e(CAConst.ROOT, ex.toString());
		}
		return key;
	}

	/**
	 * <strong>convertArray2List</strong><br>
	 * <br>
	 * convert a array to list
	 *
	 * @param array
	 * @return
	 */
	public static List<Integer> convertArray2List(int[] array){

		List<Integer> list = new ArrayList<Integer>();
		if(array != null){
			for(int i = 0; i < array.length; i++){
				list.add(array[i]);
			}
		}
		return list;
	}

	// public static DefaultHttpClient getThreadSafeClient(Context context){
	//
	// DefaultHttpClient client = new DefaultHttpClient();
	// ClientConnectionManager mgr = client.getConnectionManager();
	// HttpParams params = client.getParams();
	// // set timeout is 5 minutes
	// HttpConnectionParams.setConnectionTimeout(params, 300000);
	// client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
	// // CookieStore cookieStore = new Coo(context);
	// // client.setCookieStore(cookieStore);
	//
	// return client;
	// }

}
