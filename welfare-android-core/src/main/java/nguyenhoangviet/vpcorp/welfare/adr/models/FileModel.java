package nguyenhoangviet.vpcorp.welfare.adr.models;

/**
 * LikeModel
 *
 * @author TrungND
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class FileModel {
	public String	key;
	public String	fileType;
	public String	fileName;
	public String	fileUrl;
	public Integer	fileSize;

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getFileType(){
		return fileType;
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public String getFileName(){
		return fileName;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getFileUrl(){
		return fileUrl;
	}

	public void setFileUrl(String fileUrl){
		this.fileUrl = fileUrl;
	}

	public Integer getFileSize(){
		return fileSize;
	}

	public void setFileSize(Integer fileSize){
		this.fileSize = fileSize;
	}
}
