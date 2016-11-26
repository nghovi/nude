package trente.asia.thankscard.services.common.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by viet on 2/18/2016.
 */
public class ThanksCardPackage{

	int					rowNum;
	int					colNum;
	List<HistoryModel>	historyModels;

	public ThanksCardPackage(int rowNum, int colNum){
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.historyModels = new ArrayList<>();
	}

	public void add(HistoryModel historyModel){
		this.historyModels.add(historyModel);
	}

	public boolean isFull(){
		return historyModels.size() == rowNum * colNum;
	}
}
