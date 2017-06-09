package asia.chiase.core.util;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import asia.chiase.core.exception.CCException;
import asia.chiase.core.model.GsonModel;

/**
 * <strong>CCJsonUtil</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCJsonUtil{

	/**
	 * <strong>convertToModel</strong><br>
	 * <br>
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> T convertToModel(String jsonString, Class<T> cls){
		T result = null;
		try{

			Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();

			if(jsonString != null){
				result = gson.fromJson(jsonString, cls);
			}
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
		return result;
	}

	/**
	 * <strong>convertToModel2</strong><br>
	 * <br> convert json to model that don't care format date
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> T convertToModel2(String jsonString, Class<T> cls){
		T result = null;
		try{
			Gson gson = new GsonBuilder().create();
			if(jsonString != null){
				result = gson.fromJson(jsonString, cls);
			}
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
		return result;
	}

	/**
	 * <strong>convertToModelList</strong><br>
	 * <br>
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> List<T> convertToModelList(String jsonString, Class<T> cls){
		List<T> result = new ArrayList<T>();
		try{
			Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
			if(jsonString != null){
				result = gson.fromJson(jsonString, new GsonModel<T>(cls));
			}
		}catch(Exception ex){
			new CCException(ex);
			return new ArrayList<T>();
		}
		return result;
	}

	/**
	 * <strong>convertToJson</strong><br>
	 * <br>
	 * 
	 * @param object
	 * @return
	 */
	public static JsonObject convertToJson(Object object){
		JsonObject jsonObject = null;
		try{
			Gson gson = new GsonBuilder().create();
			if(object != null){
				String result = gson.toJson(object);
				jsonObject = new JsonParser().parse(result).getAsJsonObject();
			}
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
		return jsonObject;
	}

	/**
	 * <strong>getStringMapList</strong><br>
	 * <br>
	 * 
	 * @param value
	 * @return
	 */
	public static List<Map<String, String>> getStringMapList(String value){
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		try{
			Gson gson = new Gson();
			if(!CCStringUtil.isEmpty(value)){
				result = gson.fromJson(value, new TypeToken<List<Map<String, String>>>() {
				}.getType());
			}
		}catch(Exception ex){
			new CCException(ex);
			return null;
		}
		return result;
	}

	/**
	 * <strong>fromJsonList</strong><br>
	 * <br>
	 * 
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T> List<T> fromJsonList(String json, Class<T> cls){

		List<T> list = new ArrayList<T>();
		try{
			Type collectionType = new TypeToken<List<T>>() {
			}.getType();
			list = new Gson().fromJson(json, collectionType);

			for(Map<String, String> map : getStringMapList(json)){

				T tmp = cls.newInstance();
				ConvertUtils.register(new ConverterString2Date(), Date.class);
				ConvertUtils.register(new IntegerConverter(null), Integer.class);
				ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);

				BeanUtils.copyProperties(tmp, map);
				list.add(tmp);
			}

		}catch(Exception ex){
			new CCException(ex);
		}

		return list;
	}

}
