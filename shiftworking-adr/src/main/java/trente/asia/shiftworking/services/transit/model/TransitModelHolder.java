package nguyenhoangviet.vpcorp.shiftworking.services.transit.model;

import java.util.List;

import nguyenhoangviet.vpcorp.welfare.adr.models.ApiObjectModel;

/**
 * TransitModel
 *
 * @author TrungND
 */

public class TransitModelHolder{

	public List<ApiObjectModel>	wayTypes;
	public List<ApiObjectModel>	costTypes;
	public List<ApiObjectModel>	transTypes;

	public TransitModel			transit;
	public List<String>			historyNames;
}
