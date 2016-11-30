package trente.asia.shiftworking.services.transit.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.shiftworking.R;

/**
 * UserListAdapter.
 *
 * @author TrungND
 */
public class PlaceHistoryAdapter extends ArrayAdapter<String> {

    private List<String> mLstPlace;
    private Context mContext;
    private List<String> mLstPlaceDisplay = new ArrayList<>();

    public class UserViewHolder {

        public TextView txtPlace;

        public UserViewHolder(View view) {
            txtPlace = (TextView) view.findViewById(R.id.txt_id_place);
        }
    }

    public PlaceHistoryAdapter(Context context, List<String> lstPlace) {
        super(context, R.layout.item_place_list, new ArrayList<String>());
        this.mContext = context;
        this.mLstPlace = lstPlace;
    }

    @Override
    public int getCount() {
        return this.mLstPlaceDisplay.size();
    }

    @Override
    public String getItem(int position) {
        return mLstPlaceDisplay.get(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final String place = this.mLstPlaceDisplay.get(position);

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.item_place_list, null);
        UserViewHolder holder = new UserViewHolder(convertView);

        holder.txtPlace.setText(place);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                mLstPlaceDisplay = (ArrayList<String>) results.values; // has the filtered values
                notifyDataSetChanged(); // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults(); // Holds the results of a filtering operation in values
                ArrayList<String> filterArrayList = new ArrayList<>();

                // if(mOriginalValues == null){
                // mOriginalValues = new ArrayList<Product>(mDisplayedValues); // saves the original data in mOriginalValues
                // }

                /********
                 * If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 * else does the Filtering and returns FilteredArrList(Filtered)
                 ********/
                if (CCStringUtil.isEmpty(CCStringUtil.toString(constraint))) {

                    // set the Original result to return
                    results.count = 0;
                    results.values = filterArrayList;
                } else {
                    constraint = constraint.toString().toLowerCase().trim();
                    for (String place : mLstPlace) {
                        if (place.toLowerCase().contains(constraint)) {
                            filterArrayList.add(place);
                        }
                    }
                    // set the Filtered result to return
                    results.count = filterArrayList.size();
                    results.values = filterArrayList;
                }
                return results;
            }
        };
        return filter;
    }
}
