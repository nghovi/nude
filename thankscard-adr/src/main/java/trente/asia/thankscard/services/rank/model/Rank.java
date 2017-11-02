package nguyenhoangviet.vpcorp.thankscard.services.rank.model;

import java.util.List;

/**
 * Created by viet on 2/17/2016.
 */
public class Rank{

	public class PostRank{

		public boolean	enabled;
		public String	pos_num;
		public String	pos_rank;
		public String	poster_name;
		public String	poster_id;
		public String	user_avatar;
	}

	public class RecRank{

		public boolean	enabled;
		public String	rec_num;
		public String	rec_rank;
		public String	receiver_name;
		public String	receiver_id;
		public String	user_avatar;
	}

	public boolean			status;
	public List<PostRank>	pos_ranking;
	public List<RecRank>	rec_ranking;
}
