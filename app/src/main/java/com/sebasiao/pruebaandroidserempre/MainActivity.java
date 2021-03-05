package com.sebasiao.pruebaandroidserempre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.sebasiao.pruebaandroidserempre.network.ApiData;
import com.sebasiao.pruebaandroidserempre.network.NetworkBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.reloadIv)
    ImageView reloadIv;
    @BindView(R.id.postRv)
    RecyclerView postRv;
    @BindView(R.id.clearPostBt)
    Button clearPostBt;

    private NetworkBuilder networkBuilder = new NetworkBuilder();
    private ApiData apiData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getPost();
    }

    private void getPost() {
        apiData = networkBuilder.getApiData();
    }
}