package asia.chiase.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import asia.chiase.core.exception.CCException;

/**
 * <strong>CCFormatNumberUtil</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCFormatNumberUtil{

	public static final Character	PRODUCT_DECIMAL_SEPERATOR	= '.';
	public static final Character	PRODUCT_DECIMAL_GROUPING	= ',';

	public static final String		PRODUCT_DECIMAL_FORMAT_STR	= "#,#00.00";

	/**
	 * <strong>formatNumber</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatNumber(Object data){
		try{
			if(data == null){
				return "";
			}else{

				if(data instanceof Integer){
					NumberFormat df = NumberFormat.getIntegerInstance();
					return df.format(data);
				}else if(data instanceof BigDecimal){
					NumberFormat df = NumberFormat.getIntegerInstance();
					return df.format(data);
				}

				NumberFormat df = NumberFormat.getIntegerInstance();
				return df.format(data);
			}
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatCurrency</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatCurrency(Object data){
		try{
			if(data == null){
				return "";
			}else{

				if(data instanceof Integer){
					NumberFormat df = NumberFormat.getCurrencyInstance();
					return df.format(data);
				}else if(data instanceof BigDecimal){
					NumberFormat df = NumberFormat.getCurrencyInstance();
					return df.format(data);
				}
				return "";

			}
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatRate</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @return
	 */
	public static String formatRate(Object data){
		try{
			if(data == null){
				return "";
			}else{

				if(data instanceof Integer){
					NumberFormat df = NumberFormat.getPercentInstance();
					return df.format(data);
				}else if(data instanceof BigDecimal){
					NumberFormat df = NumberFormat.getPercentInstance();
					return df.format(data);
				}

				return "";
			}
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatDecimalView</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @param count
	 * @return
	 */
	public static String formatDecimalView(BigDecimal data, Integer count){
		try{
			if(data == null){
				return "";
			}else{

				DecimalFormatSymbols symbols = new DecimalFormatSymbols();
				symbols.setDecimalSeparator(PRODUCT_DECIMAL_SEPERATOR);
				symbols.setGroupingSeparator(PRODUCT_DECIMAL_GROUPING);
				DecimalFormat ft;

				switch(count){
				case 1:
					ft = new DecimalFormat("#,#00.0", symbols);
					break;
				case 2:
					ft = new DecimalFormat("#,#00.00", symbols);
					break;
				case 3:
					ft = new DecimalFormat("#,#00.000", symbols);
					break;
				case 4:
					ft = new DecimalFormat("#,#00.0000", symbols);
					break;
				case 5:
					ft = new DecimalFormat("#,#00.00000", symbols);
					break;
				default:
					ft = new DecimalFormat("#,#00.00", symbols);
				}

				return ft.format(data);
			}
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

	/**
	 * <strong>formatDecimalHide</strong><br>
	 * <br>
	 * 
	 * @param data
	 * @param count
	 * @return
	 */
	public static String formatDecimalHide(BigDecimal data, Integer count){
		try{
			if(data == null){
				return "";
			}else{

				DecimalFormatSymbols symbols = new DecimalFormatSymbols();
				symbols.setDecimalSeparator(PRODUCT_DECIMAL_SEPERATOR);
				symbols.setGroupingSeparator(PRODUCT_DECIMAL_GROUPING);
				DecimalFormat ft;

				switch(count){
				case 1:
					ft = new DecimalFormat("#,##0.#", symbols);
					break;
				case 2:
					ft = new DecimalFormat("#,##0.##", symbols);
					break;
				case 3:
					ft = new DecimalFormat("#,##0.###", symbols);
					break;
				case 4:
					ft = new DecimalFormat("#,##0.####", symbols);
					break;
				case 5:
					ft = new DecimalFormat("#,##0.#####", symbols);
					break;
				default:
					ft = new DecimalFormat("#,##0.##", symbols);
				}

				return ft.format(data);
			}
		}catch(Exception ex){
			new CCException(ex);
			return "";
		}
	}

}
