package trente.asia.thankscard.services.rank.model;

/**
 * Created by viet on 3/30/2016.
 */
public class RankStage{

	public static final int	RANK_STAGE_ACHIEVED		= 1;
	public static final int	RANK_STAGE_UNACHIEVED	= 0;

	public Integer			rankStage;
	public String			rankName;
	public int				require;
	public int				status;
	public String			url;

	public RankStage(String rankName, int require, int status, String url){
		this.rankName = rankName;
		this.require = require;
		this.status = status;
		this.url = url;
	}
}
