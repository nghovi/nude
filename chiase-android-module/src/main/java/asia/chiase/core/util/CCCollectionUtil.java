package asia.chiase.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <strong>CCCollectionUtil</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCCollectionUtil{

	/**
	 * <strong>isEmpty</strong><br>
	 * <br>
	 * 
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(Collection<?> list){

		if(list == null){
			return true;
		}else if(list.size() == 0){
			return true;
		}else{
			return false;
		}

	}

	/**
	 * <strong>isEmpty</strong><br>
	 * <br>
	 * 
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> map){
		if(map == null){
			return true;
		}else if(map.size() == 0){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * <strong>size</strong><br>
	 * <br>
	 *
	 * @param list
	 * @return
	 */
	public static int size(Collection<?> list){
		if(CCCollectionUtil.isEmpty(list)){
			return 0;
		}
		return list.size();
	}

	/**
	 * <strong>toInteger</strong><br>
	 * <br>
	 * 
	 * @param list
	 * @return
	 */
	public static <T> List<Integer> toInteger(List<T> list){
		List<Integer> result = new ArrayList<Integer>();
		for(T str : list){
			result.add(CCNumberUtil.toInteger(str));
		}
		return result;
	}

	/**
	 * <strong>toString</strong><br>
	 * <br>
	 * 
	 * @param list
	 * @return
	 */
	public static <T> List<String> toString(List<T> list){
		List<String> result = new ArrayList<String>();
		for(T val : list){
			result.add(CCStringUtil.toString(val));
		}
		return result;
	}

	/**
	 * <strong>toInteger</strong><br>
	 * <br>
	 * 
	 * @param list
	 * @return
	 */
	public static <T> Set<Integer> toInteger(Set<T> list){
		Set<Integer> result = new LinkedHashSet<Integer>();
		for(T str : list){
			result.add(CCNumberUtil.toInteger(str));
		}
		return result;
	}

	/**
	 * <strong>toString</strong><br>
	 * <br>
	 * 
	 * @param list
	 * @return
	 */
	public static <T> Set<String> toString(Set<T> list){
		Set<String> result = new LinkedHashSet<String>();
		for(T val : list){
			result.add(CCStringUtil.toString(val));
		}
		return result;
	}

	/**
	 * <strong>toInteger</strong><br>
	 * <br>
	 * 
	 * @param map
	 * @return
	 */
	public static Map<Integer, String> toInteger(Map<String, String> map){

		Map<Integer, String> result = new LinkedHashMap<Integer, String>();
		for(Map.Entry<String, String> target : map.entrySet()){
			result.put(CCNumberUtil.toInteger(target.getKey()), target.getValue());
		}
		return result;
	}

	/**
	 * <strong>toString</strong><br>
	 * <br>
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, String> toString(Map<Integer, String> map){

		Map<String, String> result = new LinkedHashMap<String, String>();
		for(Map.Entry<Integer, String> target : map.entrySet()){
			result.put(CCStringUtil.toString(target.getKey()), target.getValue());
		}
		return result;
	}

	/**
	 * <strong>converList2Set</strong><br>
	 * <br>
	 * 
	 * @param list
	 * @return
	 */
	public static <T> Set<T> converList2Set(List<T> list){

		Set<T> set = new HashSet<T>();
		for(T data : list){
			set.add(data);
		}
		return set;
	}

	/**
	 * <strong>converSet2List</strong><br>
	 * <br>
	 * 
	 * @param set
	 * @return
	 */
	public static <T> List<T> converSet2List(Set<T> set){

		List<T> list = new ArrayList<T>();
		for(T data : set){
			list.add(data);
		}
		return list;
	}

	/**
	 * <strong>mergeList</strong><br>
	 * <br>
	 * 
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static <T> List<T> mergeList(List<T> list1, List<T> list2){

		List<T> list = new ArrayList<T>();

		if(CCCollectionUtil.isEmpty(list1)){
			return list2;
		}else if(CCCollectionUtil.isEmpty(list2)){
			return list1;
		}else{
			for(T data : list1){
				if(list2.contains(data)){
					list.add(data);
				}
			}
		}

		return list;
	}

	/**
	 * <strong>mergeSet</strong><br>
	 * <br>
	 * 
	 * @param set1
	 * @param set2
	 * @return
	 */
	public static <T> Set<T> mergeSet(List<T> set1, List<T> set2){

		Set<T> set = converList2Set(set1);
		for(T data : set2){
			set.add(data);
		}
		return set;
	}

	/**
	 * <strong>mergeMap</strong><br>
	 * <br>
	 * 
	 * @param map1
	 * @param map2
	 * @return
	 */
	public static <T> Map<String, T> mergeMap(Map<String, T> map1, Map<String, T> map2){

		if(CCCollectionUtil.isEmpty(map1)){
			return map2;
		}else if(CCCollectionUtil.isEmpty(map2)){
			return map1;
		}else{

			for(String key : map2.keySet()){
				map1.put(key, map2.get(key));
			}
			return map1;
		}

	}

	/**
	 * <strong>removeDuplicateItem</strong><br>
	 * <br>
	 * List.contain(key) distinguish normal and case character. This method not distinguish
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> removeDuplicateItem(List<String> list){
		Set<String> set = converList2Set(list);
		return converSet2List(set);
	}

	/**
	 * <strong>isExistInList</strong><br>
	 * <br>
	 * 
	 * @param list
	 * @param key
	 * @return
	 */
	public static boolean isExistInList(List<String> list, String key){
		if(list == null || list.size() < 1) return false;
		if(key == null) return false;
		if(list.contains(key)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * <strong>isExistInList</strong><br>
	 * <br>
	 * 
	 * @param list
	 * @param key
	 * @return
	 */
	public static boolean isExistInList(List<Integer> list, Integer key){
		if(list == null || list.size() < 1) return false;
		if(key == null) return false;
		if(list.contains(key)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * HashMap.contain(key) distinguish normal and case character. This method not distinguish
	 */
	public static boolean isExistKeyInHashMap(Map<String, String> map, Object key){

		if(key == null) return false;
		if(map == null) return false;

		if(map.containsKey(CCStringUtil.toString(key))){
			return true;
		}else{
			return false;
		}

	}

	/**
	 * <strong>isExistListInHashMap</strong><br>
	 * <br>
	 * HashMap.contain(list) distinguish normal and case character. This method not distinguish
	 * 
	 * @param map
	 * @param list
	 * @return
	 */
	public static boolean isExistListInHashMap(Map<String, String> map, List<Object> list){

		if(list == null) return true;
		if(map == null) return false;

		for(Object object : list){
			if(!map.containsKey(CCStringUtil.toString(object))){
				return false;
			}
		}

		return true;
	}

	/**
	 * <strong>makeString</strong><br>
	 * <br>
	 * from map to string with comma
	 * <ul>
	 * <li>{1=test1, 2=test2, 3=test3}
	 * <li>1=test1,2=test2,3=test,
	 * </ul>
	 * note : when screen use 2 step select box, sort isn't sure. so once we should convert map to string.
	 * 
	 * @param map
	 * @return
	 */
	public static String convertMap2String(Map<String, String> map){

		StringBuffer buffer = new StringBuffer();
		boolean isFirst = true;
		for(String key : map.keySet()){
			if(isFirst){
				isFirst = false;
			}else{
				buffer.append(",");
			}
			String element = key + "=" + map.get(key);
			buffer.append(element);

		}
		return CCStringUtil.toString(buffer);
	}

	/**
	 * <strong>convertString2Map</strong><br>
	 * <br>
	 * 
	 * @param value
	 * @return
	 */
	public static Map<String, String> convertString2Map(String value){

		if(CCStringUtil.isEmpty(value)){
			return null;
		}
		Map<String, String> map = new LinkedHashMap<String, String>();
		String[] items = value.split(",");
		for(String item : items){
			String[] values = item.split("=");
			map.put(values[0], values[1]);
		}
		return map;
	}
}
