package trente.asia.addresscard.services.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import trente.asia.addresscard.R;

/**
 * Created by tien on 4/17/2017.
 */

public class CardFragment extends Fragment {
    @BindView(R.id.recyclerview_cards)
    RecyclerView recyclerViewCards;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.bind(this, view);
        ArrayList<CardModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new CardModel("4/18/2017", R.drawable.sample_image, "Update by Bkmsx"));
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewCards.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), list);
        recyclerViewCards.setAdapter(adapter);
        return view;
    }
}
