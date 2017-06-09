package asia.chiase.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.beanutils.Converter;

/**
 * <strong>ConverterString2Date</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class ConverterString2Date implements Converter{

	/** Date formatter(yyyy/MM/dd) */
	public static SimpleDateFormat	SDF_DATE_SLS	= new SimpleDateFormat("yyyy/MM/dd");

	@SuppressWarnings({"rawtypes", "unchecked"})
	public Object convert(Class type, Object value){

		try{
			if(value == null){
				return null;
			}else{
				String typeName = type.getName();
				if("java.util.Date".equals(typeName)){
					return SDF_DATE_SLS.parse((String)value);
				}else{
					return value;
				}
			}
		}catch(ParseException e){
			return null;
		}
	}
}
