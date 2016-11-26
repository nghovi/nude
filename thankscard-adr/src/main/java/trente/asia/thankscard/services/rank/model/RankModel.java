package trente.asia.thankscard.services.rank.model;

/**
 * Created by viet on 8/10/2016.
 */
public class RankModel{

	public String	userId;

	public String	userName;

	public Integer	cnt;

	public Integer	number;

	public Integer	point;

	public Integer	rank;

	public String	avatarPath;

	public boolean	isTitle;

	public String	title;

	public RankModel(){

	}

	public RankModel(String title){
		this.isTitle = true;
		this.title = title;
	}
}
