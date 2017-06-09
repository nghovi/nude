package asia.chiase.core.util.comparator;

import java.util.Calendar;
import java.util.Comparator;

/**
 * <strong>ChiaseComparatorCalenderDesc</strong><br>
 * <br>
 * 
 * @author takano-yasuhiro
 * @version $Id$
 */
public class ChiaseComparatorCalenderDesc implements Comparator<Calendar>{

	@Override
	public int compare(Calendar o1, Calendar o2){

		// long time1 = o1.getTimeInMillis();
		// long time2 = o2.getTimeInMillis();

		return (int)-(o1.getTimeInMillis() - o2.getTimeInMillis()); // IDの値に従い降順で並び替えたい場合

		// return user1.getId() - user2.getId(); // IDの値に従い昇順で並び替えたい場合
		// return -(user1.getId() - user2.getId()); // IDの値に従い降順で並び替えたい場合
	}

}
