package asia.trente.officeletter.services.wiki.model;

/**
 * Created by tien on 8/24/2017.
 */
@com.bluelinelabs.logansquare.annotation.JsonObject(fieldDetectionPolicy = com.bluelinelabs.logansquare.annotation.JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class WikiModel{

	public int		key;
	public String	wikiTitle;
	public String	wikiContent;
	public String	wikiDate;
	public String	wikiStatus;
}
