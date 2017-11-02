package nguyenhoangviet.vpcorp.thankscard.utils;

import java.math.BigDecimal;

import asia.chiase.core.util.CCNumberUtil;

/**
 * Created by viet on 3/29/2016.
 */
public class CCCalcUtil{

	public CCCalcUtil(){
	}

	public static Integer add(Integer number1, Integer number2){
		try{
			return number1 == null && number2 == null ? Integer.valueOf(0) : (number1 == null ? number2 : (number2 == null ? number1 : Integer.valueOf(number1.intValue() + number2.intValue())));
		}catch(Exception var3){
			return Integer.valueOf(0);
		}
	}

	public static BigDecimal add(BigDecimal number1, BigDecimal number2){
		try{
			return number1 == null && number2 == null ? new BigDecimal(0) : (number1 == null ? number2 : (number2 == null ? number1 : number1.add(number2)));
		}catch(Exception var3){
			return new BigDecimal(0);
		}
	}

	public static BigDecimal subtract(BigDecimal number1, BigDecimal number2){
		try{
			return number1 == null && number2 == null ? new BigDecimal(0) : (number1 == null ? number2.multiply(new BigDecimal(-1)) : (number2 == null ? number1 : number1.subtract(number2)));
		}catch(Exception var3){
			return new BigDecimal(0);
		}
	}

	public static BigDecimal multiply(BigDecimal number1, Object number2){
		try{
			if(number1 != null && number2 != null){
				BigDecimal ex;
				if(number2 instanceof BigDecimal){
					ex = (BigDecimal)number2;
				}else{
					if(!(number2 instanceof Integer)){
						return new BigDecimal(0);
					}

					ex = new BigDecimal(((Integer)number2).intValue());
				}

				return number1.multiply(ex);
			}else{
				return new BigDecimal(0);
			}
		}catch(Exception var3){
			return new BigDecimal(0);
		}
	}

	public static BigDecimal divide(BigDecimal number1, Object number2, Integer point){
		try{
			if(number1 == null){
				return new BigDecimal(0);
			}else{
				BigDecimal ex;
				if(number2 instanceof BigDecimal){
					ex = (BigDecimal)number2;
				}else{
					if(!(number2 instanceof Integer)){
						return new BigDecimal(0);
					}

					ex = new BigDecimal(((Integer)number2).intValue());
				}

				return ex.compareTo(new BigDecimal(0)) == 0 ? new BigDecimal(0) : number1.divide(ex, point.intValue(), 4);
			}
		}catch(Exception var4){
			return new BigDecimal(0);
		}
	}

	public static BigDecimal divide(BigDecimal number1, Object number2){
		return divide(number1, number2, Integer.valueOf(2));
	}

	public static BigDecimal divide(Integer number1, Object number2){
		return divide(CCNumberUtil.toBigDecimal(number1), number2, Integer.valueOf(2));
	}

	public static BigDecimal divideZero(BigDecimal number1, Object number2){
		return divide(number1, number2, Integer.valueOf(0));
	}

	public static BigDecimal rate(Integer number1, Object number2){
		BigDecimal tmp = divide(CCNumberUtil.toBigDecimal(number1), number2, Integer.valueOf(4));
		return multiply(tmp, Integer.valueOf(100));
	}
}
