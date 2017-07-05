package trente.asia.messenger.services.message.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by tien on 6/29/2017.
 */

public class RealmUserModel extends RealmObject {
    @PrimaryKey
    public int				key;
    public String				userName;
    public String				userAccount;
    public String				userMail;
    public String				userNameKana;

    public String				adminFlag;
    public String				loginUserId;
    public String				avatarPath;

    public RealmUserModel(){}

    public RealmUserModel(UserModel user) {
        this.key = Integer.parseInt(user.key);
        this.userName = user.userName;
        this.userAccount = user.userAccount;
        this.userMail = user.userMail;
        this.userNameKana = user.userNameKana;
        this.adminFlag = user.adminFlag;
        this.loginUserId = user.loginUserId;
        this.avatarPath = user.avatarPath;
    }
}
