package asia.chiase.core.util;

import java.math.BigDecimal;

import asia.chiase.core.exception.CCException;

/**
 * <strong>CCNumberUtil</strong><br>
 * <br>
 * <ul>
 * <li>toXXX : convert field type
 * <li>isXXX : judge
 * </ul>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCNumberUtil{

	/**
	 * <strong>toInteger</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static Integer toInteger(Object data){

		if(data == null){
			return null;
		}else{
			Integer result;
			try{
				if(data instanceof String){
					result = new Integer(((String)data).trim());
				}else if(data instanceof BigDecimal){
					result = ((BigDecimal)data).intValue();
				}else if(data instanceof Double){
					Double db = (Double)data;
					result = db.intValue();
				}else{
					result = new Integer(data.toString());
				}
			}catch(Exception ex){
				return null;
			}
			return result;
		}
	}

	/**
	 * <strong>toLong</strong><br>
	 * <br>
	 *
	 * @param data
	 * @return
	 */
	public static Long toLong(Object data){

		if(data == null){
			return null;
		}else{
			Long result;
			try{
				if(data instanceof String){
					result = new Long(((String)data).trim());
				}else if(data instanceof BigDecimal){
					result = ((BigDecimal)data).longValue();
				}else if(data instanceof Double){
					Double db = (Double)data;
					result = db.longValue();
				}else{
					result = new Long(data.toString());
				}
			}catch(Exception ex){
				return null;
			}
			return result;
		}
	}

	/**
	 * <strong>toFloat</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static Float toFloat(Object data){
		try{
			if(data == null){
				return (float)0;
			}else{
				if(data instanceof String){
					return new Float(data.toString());
				}else if(data instanceof Integer){
					return new Integer(data.toString()).floatValue();
				}else{
					return (float)0;
				}
			}

		}catch(Exception ex){
			new CCException(ex);
			return (float)0;
		}
	}

	/**
	 * <strong>toDouble</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static Double toDouble(Object data){
		try{
			if(data == null){
				return (double)0;
			}else{
				if(data instanceof String){
					if(CCStringUtil.isEmpty(data.toString())){
						return (double)0;
					}else{
						return new Double(data.toString());
					}
				}else if(data instanceof Integer){
					return new Integer(data.toString()).doubleValue();
				}else if(data instanceof BigDecimal){
					return ((BigDecimal)data).doubleValue();
				}else{
					return (double)0;
				}
			}

		}catch(Exception ex){
			new CCException(ex);
			return (double)0;
		}
	}

	/**
	 * <strong>toBigDecimal</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static BigDecimal toBigDecimal(Object data){
		try{
			if(data == null){
				return new BigDecimal("0");
			}else{
				if(data instanceof String){
					if(!CCStringUtil.isEmpty(data)){
						data = ((String)data).replace(",", "");
						return new BigDecimal(data.toString());
					}
				}else if(data instanceof Integer){
					return new BigDecimal(data.toString());
				}else if(data instanceof Double){
					return new BigDecimal(data.toString());
				}else if(data instanceof Long){
					return new BigDecimal(data.toString());
				}
				return new BigDecimal("0");
			}

		}catch(Exception ex){
			new CCException(ex);
			return new BigDecimal("0");
		}
	}

	/**
	 * <strong>checkNull</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static Integer checkNull(Integer data){
		if(data == null){
			return 0;
		}else{
			return data;
		}
	}

	/**
	 * <strong>checkNull</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static Integer checkNull(String data){
		if(CCStringUtil.isEmpty(data)){
			return 0;
		}else{
			return Integer.parseInt(data);
		}
	}

	/**
	 * <strong>checkNull</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static Double checkNull(Double data){
		if(data == null){
			return (double)0;
		}else{
			return data;
		}
	}

	/**
	 * <strong>checkNull</strong><br>
	 * <br>
	 *
	 * @param data
	 * @return
	 */
	public static Long checkNull(Long data){
		if(data == null){
			return (long)0;
		}else{
			return data;
		}
	}

	/**
	 * <strong>checkNull</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static Float checkNull(Float data){
		if(data == null){
			return (float)0.0f;
		}else{
			return data;
		}
	}

	/**
	 * <strong>checkNull</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static BigDecimal checkNull(BigDecimal data){
		if(data == null){
			return new BigDecimal("0");
		}else{
			return data;
		}
	}

	/**
	 * <strong>isNumber</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isNumber(String data){
		if(CCStringUtil.isEmpty(data)){
			return false;
		}else{
			char c;
			for(int i = 0; i < data.length(); i++){
				c = data.charAt(i);
				if(c < '0' || c > '9'){
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * <strong>isDouble</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isDouble(String data){
		if(CCStringUtil.isEmpty(data)){
			return false;
		}else{
			char c;
			for(int i = 0; i < data.length(); i++){
				c = data.charAt(i);
				if(c < '0' || c > '9'){
					if(c != '.'){
						return false;
					}
				}
			}
			return true;
		}
	}

	/**
	 * <strong>isZero</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isZero(Object data){

		try{
			if(data instanceof String){
				return CCStringUtil.isEmpty(data);
			}else if(data instanceof Integer){
				return (Integer)data == 0 ? true : false;
			}else if(data instanceof Float){
				return (Float)data == 0 ? true : false;
			}else if(data instanceof Double){
				return (Double)data == 0 ? true : false;
			}else if(data instanceof BigDecimal){
				return ((BigDecimal)data).equals(BigDecimal.ZERO) ? true : false;
			}else{
				return false;
			}
		}catch(Exception ex){
			new CCException(ex);
			return false;
		}
	}
}
