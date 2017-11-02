package nguyenhoangviet.vpcorp.messenger.services.message.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import nguyenhoangviet.vpcorp.welfare.adr.models.FileModel;

/**
 * Created by tien on 6/29/2017.
 */

public class RealmFileModel extends RealmObject {
    @PrimaryKey
    public int	key;
    public String	fileType;
    public String	fileName;
    public String	fileUrl;
    public Integer	fileSize;

    public RealmFileModel(){}

    public RealmFileModel(FileModel fileModel) {
        if (fileModel.key != null && !fileModel.key.isEmpty()) {
            this.key = Integer.parseInt(fileModel.key);
        }
        this.fileType = fileModel.fileType;
        this.fileName = fileModel.fileName;
        this.fileUrl = fileModel.fileUrl;
        this.fileSize = fileModel.fileSize;
    }
}
