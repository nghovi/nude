package nguyenhoangviet.vpcorp.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by takyas on 27/9/14.
 */
public class CAPreferences{

	public static String				USER_ID		= "user_id";

	public static String				USER_TOKEN	= "user_token";

	protected SharedPreferences			pref;

	protected SharedPreferences.Editor	editor;

	public static final String			PREFS_NAME	= "PreferencesSystemCS";

	public CAPreferences(){
	}

	public CAPreferences(Context cntx){
		pref = PreferenceManager.getDefaultSharedPreferences(cntx);
		editor = pref.edit();
	}

	public CAPreferences(Context cntx, boolean isSystem){
		pref = cntx.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		editor = pref.edit();
	}

	public void clear(){
		editor.clear();
		editor.commit();
	}

	public void removeKey(String key){
		editor.remove(key);
		editor.commit();
	}

	@SuppressLint("CommitPrefEdits")
	public void set(String key, String val){
		editor.putString(key, val);
		editor.commit();
	}

	@SuppressLint("CommitPrefEdits")
	public void set(String key, int val){
		editor.putInt(key, val);
		editor.commit();
	}

	public String get(String key){
		return pref.getString(key, "");
	}

	@SuppressLint("CommitPrefEdits")
	public void setUserId(Integer val){
		editor.putInt(USER_ID, val);
		editor.commit();
	}

	public Integer getUserId(){
		return pref.getInt(USER_ID, 0);
	}

	@SuppressLint("CommitPrefEdits")
	public void setUserToken(String value){
		editor.putString(USER_TOKEN, value);
		editor.commit();
	}

	public String getUserToken(){
		return pref.getString(USER_TOKEN, "");
	}

}
