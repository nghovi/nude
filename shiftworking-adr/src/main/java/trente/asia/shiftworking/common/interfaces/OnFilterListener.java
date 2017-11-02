package nguyenhoangviet.vpcorp.shiftworking.common.interfaces;

import java.util.Map;

import nguyenhoangviet.vpcorp.welfare.adr.models.ApiObjectModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;
import nguyenhoangviet.vpcorp.welfare.adr.models.UserModel;

/**
 * Created by tien on 9/20/2017.
 */

public interface OnFilterListener {
    void onFilterCompleted(DeptModel dept, UserModel user, ApiObjectModel type);
}
