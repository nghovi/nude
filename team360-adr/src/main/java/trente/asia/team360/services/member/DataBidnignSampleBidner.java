package trente.asia.team360.services.member;

import android.databinding.BindingAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.List;

import trente.asia.welfare.adr.models.UserModel;

/**
 * Created by takano-yasuhiro on 2017/06/18.
 */

public class DataBidnignSampleBidner {

    @BindingAdapter("items")
    public static void setItems(GridView listView, List<UserModel> tasks) {
        TmMemberAdapter adapter = new TmMemberAdapter(listView.getContext());
        //adapter.addAll(tasks);
        listView.setAdapter(adapter);
    }
}
