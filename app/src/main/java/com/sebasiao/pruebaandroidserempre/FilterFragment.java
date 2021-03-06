package com.sebasiao.pruebaandroidserempre;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FilterFragment extends Fragment {
    @BindView(R.id.filterFavTv)
    TextView filterFavTv;
    @BindView(R.id.cleanFilterTv)
    TextView cleanFilterTv;

    public FilterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_fragment,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick({R.id.cleanFilterTv,R.id.filterFavTv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.cleanFilterTv:
                ((MainActivity)getActivity()).getPost();
                break;
            case  R.id.filterFavTv:
                ((MainActivity)getActivity()).showFavs();
                break;
            default:
                break;
        }
    }
}
