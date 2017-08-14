package trente.asia.thankscard.services.rank.model;

/**
 * Created by viet on 8/10/2016.
 */
public class RankModel{

	public String	userId;

	public String	userName;

	public int	cnt;

	public int	number;

	public int	point;

	public int	rank;

	public String	avatarPath;

	public boolean	isTitle;

	public String	title;

	public int	yearPoint;

	public RankModel(){

	}

	public RankModel(String title){
		this.isTitle = true;
		this.title = title;
	}
}
