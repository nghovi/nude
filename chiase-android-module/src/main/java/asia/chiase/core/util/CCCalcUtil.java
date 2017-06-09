package asia.chiase.core.util;

import java.math.BigDecimal;

import asia.chiase.core.exception.CCException;

/**
 * <strong>CCCalcUtil</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class CCCalcUtil{

	/**
	 * <strong>add</strong><br>
	 * <br>
	 * <ul>
	 * <li> return = number1 + number2
	 * </ul>
	 *
	 * @param number1 Integer
	 * @param number2 Integer
	 * @return Integer
	 */
	public static Integer add(Integer number1, Integer number2){

		try{
			if(number1 == null && number2 == null){
				return 0;
			}

			if(number1 == null){
				return number2;
			}else if(number2 == null){
				return number1;
			}else{
				return number1 + number2;
			}

		}catch(Exception ex){
			new CCException(ex);
			return 0;
		}
	}

	/**
	 * <strong>add</strong><br>
	 * <br>
	 * <ul>
	 * <li> return = number1 + number2
	 * </ul>
	 *
	 * @param number1 Long
	 * @param number2 Long
	 * @return Long
	 */
	public static Long add(Long number1, Long number2){

		try{
			if(number1 == null && number2 == null){
				return new Long(0);
			}

			if(number1 == null){
				return number2;
			}else if(number2 == null){
				return number1;
			}else{
				return number1 + number2;
			}

		}catch(Exception ex){
			new CCException(ex);
			return new Long(0);
		}
	}

	/**
	 * <strong>add</strong><br>
	 * <br>
	 * <ul>
	 * <li> return = number1 + number2
	 * </ul>
	 *
	 * @param number1 BigDecimal
	 * @param number2 BigDecimal
	 * @return BigDecimal
	 */
	public static BigDecimal add(BigDecimal number1, BigDecimal number2){

		try{
			if(number1 == null && number2 == null){
				return BigDecimal.ZERO;
			}

			if(number1 == null){
				return number2;
			}else if(number2 == null){
				return number1;
			}else{
				return number1.add(number2);
			}

		}catch(Exception ex){
			new CCException(ex);
			return BigDecimal.ZERO;
		}
	}

	/**
	 * <strong>subtract</strong><br>
	 * <br>
	 * <ul>
	 * <li> return = number1 - number2
	 * </ul>
	 *
	 * @param number1 BigDecimal
	 * @param number2 BigDecimal
	 * @return BigDecimal
	 */
	public static BigDecimal subtract(BigDecimal number1, BigDecimal number2){

		try{
			if(number1 == null && number2 == null){
				return BigDecimal.ZERO;
			}
			if(number1 == null){
				return number2.multiply(new BigDecimal(-1));
			}else if(number2 == null){
				return number1;
			}else{
				return number1.subtract(number2);
			}

		}catch(Exception ex){
			new CCException(ex);
			return BigDecimal.ZERO;
		}
	}

	/**
	 * <strong>subtract</strong><br>
	 * <br>
	 * <ul>
	 * <li> return = number1 - number2
	 * </ul>
	 *
	 * @param number1 Long
	 * @param number2 Long
	 * @return Long
	 */
	public static Long subtract(Long number1, Long number2){

		try{
			if(number1 == null && number2 == null){
				return new Long(0);
			}
			if(number1 == null){

				BigDecimal tmp = CCNumberUtil.toBigDecimal(number2).multiply(new BigDecimal(-1));
				return CCNumberUtil.toLong(tmp);
			}else if(number2 == null){
				return number1;
			}else{

				BigDecimal tmp = CCNumberUtil.toBigDecimal(number1).subtract(CCNumberUtil.toBigDecimal(number2));
				return CCNumberUtil.toLong(tmp);
			}

		}catch(Exception ex){
			new CCException(ex);
			return new Long(0);
		}
	}

	/**
	 * <strong>multiply</strong><br>
	 * <br>
	 * <ul>
	 * <li> return = number1 * number2
	 * </ul>
	 *
	 * @param number1 BigDecimal
	 * @param number2 BigDecimal
	 * @return BigDecimal
	 */
	public static BigDecimal multiply(BigDecimal number1, Object number2){

		try{

			if(number1 == null || number2 == null){
				return BigDecimal.ZERO;
			}

			BigDecimal number3;
			if(number2 instanceof BigDecimal){
				number3 = (BigDecimal)number2;
			}else if(number2 instanceof Integer){
				number3 = new BigDecimal((Integer)number2);
			}else if(number2 instanceof Long){
				number3 = new BigDecimal((Long)number2);
			}else{
				return BigDecimal.ZERO;
			}

			return number1.multiply(number3);

		}catch(Exception ex){
			new CCException(ex);
			return BigDecimal.ZERO;
		}

	}

	/**
	 * <strong>divide</strong><br>
	 * <br>
	 * <ul>
	 * <li> return = number1 / number2
	 * </ul>
	 *
	 * @param number1 BigDecimal
	 * @param number2 Object
	 * @param point Integer
	 * @return BigDecimal
	 */
	public static BigDecimal divide(BigDecimal number1, Object number2, Integer point){

		try{

			if(number1 == null){
				return BigDecimal.ZERO;
			}

			BigDecimal number3;
			if(number2 instanceof BigDecimal){
				number3 = (BigDecimal)number2;
			}else if(number2 instanceof Integer){
				number3 = new BigDecimal((Integer)number2);
			}else if(number2 instanceof Long){
				number3 = new BigDecimal((Long)number2);
			}else if(number2 instanceof Double){
				number3 = new BigDecimal((Double)number2);
			}else{
				return BigDecimal.ZERO;
			}

			if(number3.compareTo(BigDecimal.ZERO) == 0){
				return BigDecimal.ZERO;
			}

			return number1.divide(number3, point, BigDecimal.ROUND_HALF_UP);

		}catch(Exception ex){
			new CCException(ex);
			return BigDecimal.ZERO;
		}
	}

	/**
	 * <strong>divide</strong><br>
	 * <br>
	 * <ul>
	 * <li> return = number1 / number2
	 * </ul>
	 * 
	 * @param number1 BigDecimal
	 * @param number2 Object
	 * @return BigDecimal
	 */
	public static BigDecimal divide(BigDecimal number1, Object number2){
		return divide(number1, number2, 2);
	}

	/**
	 * <strong>divide</strong><br>
	 * <br>
	 * <ul>
	 * <li> return = number1 / number2
	 * </ul>
	 *
	 * @param number1 Integer
	 * @param number2 Object
	 * @return BigDecimal
	 */
	public static BigDecimal divide(Integer number1, Object number2){
		return divide(CCNumberUtil.toBigDecimal(number1), number2, 2);
	}

	/**
	 * <strong>divideZero</strong><br>
	 * <br>
	 * <ul>
	 * <li> return = number1 / number2, after that delete after point
	 * </ul>
	 *
	 * @param number1 BigDecimal
	 * @param number2 Object
	 * @return BigDecimal
	 */
	public static BigDecimal divideZero(BigDecimal number1, Object number2){
		return divide(number1, number2, 0);
	}

	/**
	 * <strong>rate</strong><br>
	 * <br>
	 * <ul>
	 * <li> return show rate, for example, 10(%), 10.5(%), 10.25(%)
	 * </ul>
	 *
	 * @param number1 Integer
	 * @param number2 Object
	 * @return BigDecimal
	 */
	public static BigDecimal rate(Integer number1, Object number2){
		BigDecimal tmp = divide(CCNumberUtil.toBigDecimal(number1), number2, 4);
		return multiply(tmp, 100);
	}

}
