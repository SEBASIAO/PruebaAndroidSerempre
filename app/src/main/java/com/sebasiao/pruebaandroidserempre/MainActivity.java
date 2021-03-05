package com.sebasiao.pruebaandroidserempre;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.sebasiao.pruebaandroidserempre.adapters.PostsAdapter;
import com.sebasiao.pruebaandroidserempre.models.PostModel;
import com.sebasiao.pruebaandroidserempre.network.ApiData;
import com.sebasiao.pruebaandroidserempre.network.NetworkBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.reloadIv)
    ImageView reloadIv;
    @BindView(R.id.postRv)
    RecyclerView postRv;
    @BindView(R.id.clearPostBt)
    Button clearPostBt;

    private NetworkBuilder networkBuilder = new NetworkBuilder();
    private ApiData apiData;
    private PostsAdapter postsAdapter;
    private final ArrayList<PostModel> postModelArrayList = new ArrayList<>();

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
        Observable<Response<ResponseBody>> observable = apiData.getPosts();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        if (responseBodyResponse.isSuccessful()){
                            try {
                                assert responseBodyResponse.body() != null;
                                JSONArray object = new JSONArray(responseBodyResponse.body().string());
                                initRecycler(object);
                            }catch (JSONException |NullPointerException| IOException e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void initRecycler(JSONArray array) {
        try {
            postModelArrayList.clear();
            for (int i = 0 ; i < array.length() ; i++){
                JSONObject post = array.getJSONObject(i);
                PostModel postModel = new PostModel(post.getInt("id"),post.getString("title"),post.getString("body"));
                postModelArrayList.add(postModel);
            }
            postRv.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));
            postsAdapter = new PostsAdapter(MainActivity.this,postModelArrayList);
            postRv.setAdapter(postsAdapter);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}