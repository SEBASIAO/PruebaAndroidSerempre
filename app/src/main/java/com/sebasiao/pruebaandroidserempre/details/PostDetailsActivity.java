package com.sebasiao.pruebaandroidserempre.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sebasiao.pruebaandroidserempre.MainActivity;
import com.sebasiao.pruebaandroidserempre.R;
import com.sebasiao.pruebaandroidserempre.adapters.PostsAdapter;
import com.sebasiao.pruebaandroidserempre.models.PostModel;
import com.sebasiao.pruebaandroidserempre.network.ApiData;
import com.sebasiao.pruebaandroidserempre.network.NetworkBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class PostDetailsActivity extends AppCompatActivity {
    @BindView(R.id.nameTv)
    TextView nameTv;
    @BindView(R.id.usernameTv)
    TextView usernameTv;
    @BindView(R.id.emailTv)
    TextView emailTv;
    @BindView(R.id.addressTv)
    TextView addressTv;
    @BindView(R.id.locationTv)
    TextView locationTv;
    @BindView(R.id.phoneTv)
    TextView phoneTv;
    @BindView(R.id.websiteTv)
    TextView websiteTv;
    @BindView(R.id.companyTv)
    TextView companyTv;
    @BindView(R.id.backDetailsIv)
    ImageView backDetailsIv;
    private int userId;
    private NetworkBuilder networkBuilder = new NetworkBuilder();
    private ApiData apiData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        ButterKnife.bind(this);
        //Get the info coming from MainActivity
        if (getIntent() != null){
            //userId is used to find the User who wrote the Post
            userId = Integer.parseInt(getIntent().getExtras().getString("userId"));
        }
        ////API Call to get all Users from https://jsonplaceholder.typicode.com/
        getUsers();
    }

    private void getUsers() {
        //Get Users using Observable from RX Java
        apiData = networkBuilder.getApiData();
        Observable<Response<ResponseBody>> observable = apiData.getUsers();
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
                                showUserInfo(object);
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
    //Method to find the User who wrote the Post and display the User info
    private void showUserInfo(JSONArray object) {
        if (object.length() != 0 && !object.equals(null)){
            try {
                for (int i = 0 ; i < object.length() ; i++){
                    JSONObject user = object.getJSONObject(i);
                    JSONObject address = user.getJSONObject("address");
                    JSONObject latLong = address.getJSONObject("geo");
                    String addressComplete = address.getString("street")+", "+address.getString("suite")+", "+address.getString("city");
                    JSONObject companyObj = user.getJSONObject("company");

                    if (userId == user.getInt("id")){
                         String name = user.getString("name");
                         String username = user.getString("username");
                         String email = user.getString("email");
                         String addressUser = addressComplete;
                         String location = latLong.getString("lat")+", "+latLong.getString("lng");
                         String phone = user.getString("phone");
                         String website = user.getString("website");
                         String company = companyObj.getString("name");
                         nameTv.setText(name);
                         usernameTv.setText(username);
                         emailTv.setText(email);
                         addressTv.setText(addressUser);
                         locationTv.setText(location);
                         phoneTv.setText(phone);
                         websiteTv.setText(website);
                         companyTv.setText(company);
                         break;
                    }
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
    //Method OnClick from ButterKnife library
    @OnClick({R.id.backDetailsIv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.backDetailsIv:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}