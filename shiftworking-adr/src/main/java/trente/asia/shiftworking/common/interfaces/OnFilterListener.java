package trente.asia.shiftworking.common.interfaces;

import java.util.Map;

import trente.asia.welfare.adr.models.ApiObjectModel;
import trente.asia.welfare.adr.models.DeptModel;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by tien on 9/20/2017.
 */

public interface OnFilterListener {
    void onFilterCompleted(DeptModel dept, UserModel user, ApiObjectModel type);
}
