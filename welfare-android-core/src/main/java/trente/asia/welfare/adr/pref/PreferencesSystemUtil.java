package trente.asia.welfare.adr.pref;

import android.content.Context;

import trente.asia.android.util.CAPreferences;

/**
 * Created by TuVD on 8/6/2015.
 */
public class PreferencesSystemUtil{

	public static String	KEY_PREF_USER_NAME	= "pref_user_name";

	public CAPreferences	pref;

	public PreferencesSystemUtil(Context cntx){
		pref = new CAPreferences(cntx, true);
	}

	public void clear(){
		pref.clear();
	}

	public void removeKey(String key){
		pref.removeKey(key);
	}

	public void set(String key, String val){
		pref.set(key, val);
	}

	public String get(String key){
		return pref.get(key);
	}
}
