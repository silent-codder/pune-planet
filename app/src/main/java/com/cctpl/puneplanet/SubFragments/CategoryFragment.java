package com.cctpl.puneplanet.SubFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cctpl.puneplanet.Adapter.AllCategoryAdapter;
import com.cctpl.puneplanet.R;
import com.cctpl.puneplanet.model.AllCategory;


public class CategoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        AllCategory[] allCategories = new AllCategory[]{
          new AllCategory("थरारक गोष्टी",R.drawable.horrer),
          new AllCategory("भयकथा",R.drawable.horrer_1),
          new AllCategory("हास्य कथा",R.drawable.joker),
          new AllCategory("प्रेमकथा",R.drawable.love),
          new AllCategory("रहस्य कथा",R.drawable.horrer_2),
          new AllCategory("अन्य कथा",R.drawable.book),
          new AllCategory("लघु कथा",R.drawable.book_1),
          new AllCategory("विज्ञान कथा",R.drawable.science),
          new AllCategory("ऐतिहासिक कथा",R.drawable.historical),
          new AllCategory("पाककला",R.drawable.vegetables),
          new AllCategory("जीवन-चरित्र",R.drawable.ghandi),
          new AllCategory("बालकथा",R.drawable.boy),
          new AllCategory("अनुभव",R.drawable.experince),
          new AllCategory("बोथकथा",R.drawable.nature),
          new AllCategory("कविता",R.drawable.poem),
          new AllCategory("प्रवास वर्णन",R.drawable.road)
        };

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        AllCategoryAdapter adapter = new AllCategoryAdapter(allCategories);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}