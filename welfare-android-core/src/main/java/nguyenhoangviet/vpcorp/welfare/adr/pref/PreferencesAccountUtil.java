package nguyenhoangviet.vpcorp.welfare.adr.pref;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import android.content.Context;

import asia.chiase.core.util.CCDateUtil;
import nguyenhoangviet.vpcorp.android.util.CAPreferences;
import nguyenhoangviet.vpcorp.welfare.adr.define.WelfareConst;
import nguyenhoangviet.vpcorp.welfare.adr.models.SettingModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;
import nguyenhoangviet.vpcorp.welfare.adr.utils.WelfareUtil;

/**
 * Created by TuVD on 8/6/2015.
 */
public class PreferencesAccountUtil{

	public static String	KEY_PREF_USER			= "pref_user";
	public static String	KEY_PREF_SETTING		= "pref_setting";
	public static String	KEY_PREF_ACTIVE_DATE	= "pref_active_date";

	public CAPreferences	pref;

	public PreferencesAccountUtil(Context cntx){
		pref = new CAPreferences(cntx);
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

	private Gson gson;

	/**
	 * Save Object User to Prefers.
	 *
	 * @param user
	 */
	public void saveUserPref(UserModel user){
		Gson gson = getGson();
		String json = gson.toJson(user);
		pref.set(KEY_PREF_USER, json);
	}

	/**
	 * Get Object User from Prefers.
	 *
	 * @return
	 */
	public UserModel getUserPref(){
		String json = pref.get(KEY_PREF_USER);
		UserModel userCS = null;
		Gson gson = getGson();
		userCS = gson.fromJson(json, UserModel.class);
		if(userCS == null) userCS = new UserModel();
		return userCS;
	}

	private Gson getGson(){
		if(gson != null){
			return gson;
		}

		JsonSerializer<Date> ser = new JsonSerializer<Date>() {

			@Override
			public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context){
				return src == null ? null : new JsonPrimitive(src.getTime());
			}
		};

		JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {

			@Override
			public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
				if(json == null){
					return null;
				}

				if(json.getAsString().contains("/")){
					return CCDateUtil.makeDateCustom(json.getAsString(), WelfareConst.WF_DATE_TIME);
				}else{
					return new Date(json.getAsLong());
				}
			}
		};

		gson = new GsonBuilder().registerTypeAdapter(Date.class, ser).registerTypeAdapter(Date.class, deser).create();
		return gson;
	}

	/**
	 * Save Object setting to Prefers.
	 *
	 * @param newSetting
	 */
	public void saveSetting(SettingModel newSetting){
		if(newSetting != null){
			SettingModel oldSetting = getSetting();
			if(isSettingChanged(newSetting, oldSetting)){
				Gson gson = new Gson();
				String json = gson.toJson(newSetting);
				pref.set(KEY_PREF_SETTING, json);
			}
		}
	}

	private boolean isSettingChanged(SettingModel newS, SettingModel old){
		boolean isPublicLevelChanged = newS.DR_PUBLIC_LEVEL != null && !newS.DR_PUBLIC_LEVEL.equals(old.DR_PUBLIC_LEVEL);
		boolean isPushSettingChanged = newS.WF_PUSH_SETTING != null && !newS.WF_PUSH_SETTING.equals(old.WF_PUSH_SETTING);
		boolean isMaxFileChanged = newS.WF_MAX_FILE_SIZE != null && !newS.WF_MAX_FILE_SIZE.equals(old.WF_MAX_FILE_SIZE);
		boolean isAppointmentChanged = newS.SW_APPOINTMENT_ENABLED != null && !newS.SW_APPOINTMENT_ENABLED.equals(old.SW_APPOINTMENT_ENABLED);
		boolean isFirstDayOfWeekChanged = newS.CL_START_DAY_IN_WEEK != null && !newS.CL_START_DAY_IN_WEEK.equals(old.CL_START_DAY_IN_WEEK);

		return isPublicLevelChanged || isPushSettingChanged || isMaxFileChanged || isAppointmentChanged || isFirstDayOfWeekChanged;
	}

	/**
	 * Get Object setting from Prefers.
	 *
	 * @return
	 */
	public SettingModel getSetting(){
		String json = pref.get(KEY_PREF_SETTING);
		Gson gson = new Gson();
		SettingModel setting = gson.fromJson(json, SettingModel.class);
		if(setting == null) setting = new SettingModel();
		return setting;
	}

	/**
	 * Save Object FilterRecruitment to Prefers.
	 *
	 * @param object
	 */
	public void setModel(String key, Object object){
		Gson gson = new Gson();
		String json = gson.toJson(object);
		pref.set(key, json);
	}

	/**
	 * Save active date to Prefers.
	 *
	 * @param activeDate
	 */
	public void saveActiveDate(Date activeDate){
		pref.set(KEY_PREF_ACTIVE_DATE, WelfareUtil.getDateString(activeDate));
	}

	/**
	 * Get active date from Prefers.
	 *
	 * @return
	 */
	public String getActiveDate(){
		String dateString = pref.get(KEY_PREF_ACTIVE_DATE);
		return dateString;
	}
}
