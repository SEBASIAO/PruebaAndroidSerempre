package com.sebasiao.pruebaandroidserempre;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.reloadIv)
    ImageView reloadIv;
    @BindView(R.id.postRv)
    RecyclerView postRv;
    @BindView(R.id.clearPostBt)
    Button clearPostBt;
    @BindView(R.id.noPostTv)
    TextView noPostTv;
    @BindView(R.id.filterIV)
    ImageView filterIV;
    @BindView(R.id.filterFrameLy)
    FrameLayout filterFrameLy;


    private NetworkBuilder networkBuilder = new NetworkBuilder();
    private ApiData apiData;
    private PostsAdapter postsAdapter;
    private final ArrayList<PostModel> postModelArrayList = new ArrayList<>();
    private final ArrayList<PostModel> favList = new ArrayList<>();
    PostModel deletedPost = null;
    FilterFragment filterFragment = new FilterFragment();
    private boolean fragmentVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }
    //Init Main Activity
    private void init() {
        //API Call to get all Posts from https://jsonplaceholder.typicode.com/
        getPost();
    }

    public void getPost() {
        //Get Posts using Observable from RX Java
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
                                //Init RecyclerView with all the Posts
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
    //Init RecyclerView with all the Posts
    private void initRecycler(JSONArray array) {
        if (array.length() == 0 || array.equals(null)){
        }else{
        try {
            postModelArrayList.clear();
            //Add all the Posts info into ArrayList
            for (int i = 0 ; i < array.length() ; i++){
                JSONObject post = array.getJSONObject(i);
                PostModel postModel = new PostModel(post.getInt("id"),post.getString("title"),post.getString("body"),post.getInt("userId"));
                postModelArrayList.add(postModel);
            }
            postRv.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));
            postsAdapter = new PostsAdapter(MainActivity.this,postModelArrayList,false);
            //Attach ITH to RecyclerView
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(postRv);
            postRv.setAdapter(postsAdapter);
            noPostTv.setText(getString(R.string.slideToDelete));
            //Animation to RecyclerView
            Transition transition = new Slide(Gravity.BOTTOM);
            transition.setDuration(500);
            transition.addTarget(postRv);
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.postRv);
            TransitionManager.beginDelayedTransition(viewGroup,transition);
            postRv.setVisibility(View.VISIBLE);
        }catch (JSONException e){
            e.printStackTrace();
        }
        }
    }
    //On click method from ButterKnife Library
    @OnClick({R.id.clearPostBt,R.id.reloadIv,R.id.filterIV})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.clearPostBt:
                clearRv();
                break;
            case  R.id.reloadIv:
                getPost();
                break;
            case R.id.filterIV:
                loadFragment(filterFragment);
                break;
            default:
                break;
        }
    }
    //Load filter fragment and toogle visibility
    private void loadFragment(Fragment fragment) {
        if (fragmentVisible){
            filterFrameLy.setVisibility(View.GONE);
            fragmentVisible=false;
        }else{
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.filterFrameLy,fragment);
            transaction.commit();
            filterFrameLy.setVisibility(View.VISIBLE);
            fragmentVisible = true;
        }
    }
    //Method to remove all the Posts even Favorites Post
    public void clearRv() {
        Transition transition = new Fade();
        transition.setDuration(500);
        transition.addTarget(postRv);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.postRv);
        TransitionManager.beginDelayedTransition(viewGroup,transition);
        postModelArrayList.clear();
        favList.clear();
        postsAdapter.notifyDataSetChanged();
        noPostTv.setText(getString(R.string.noPost));
        postRv.setVisibility(View.GONE);
    }

    //Item Touch Helper to add Swipe to delete function
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            deletedPost = new PostModel(postModelArrayList.get(position).getId(),postModelArrayList.get(position).getTitle(),postModelArrayList.get(position).getBody(),postModelArrayList.get(position).getUserId());
            String deleted = getString(R.string.undoSlide)+(deletedPost.getId());
            postModelArrayList.remove(viewHolder.getAdapterPosition());
            postsAdapter.notifyDataSetChanged();
            //Snackbar to undo delete
            Snackbar.make(postRv,deleted, Snackbar.LENGTH_LONG)
                    .setAction("Deshacer", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            postModelArrayList.add(position,deletedPost);
                            postsAdapter.notifyDataSetChanged();
                        }
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.RedDelete))
                    .addActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    //Method called from adapter to add one Post to Favorite ArrayList
    public boolean addToFav (int position){
        PostModel favModel = new PostModel(postModelArrayList.get(position).getId(),postModelArrayList.get(position).getTitle(),postModelArrayList.get(position).getBody(),postModelArrayList.get(position).getUserId());
        if (favList.size() > 0){
            boolean isAdded = false;
            for (int i =0 ; i < favList.size(); i++){
                if (favModel.getId() == favList.get(i).getId()){
                    isAdded = true;
                    Toast.makeText(this,"Este Post ya está en tu lista de favoritos",Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            if (!isAdded){
                favList.add(favModel);
                Toast.makeText(this,"Post Nro: "+favModel.getId()+" añadido a favoritos",Toast.LENGTH_SHORT).show();
                return true;
            }
        }else{
            favList.add(favModel);
            Toast.makeText(this,"Post Nro: "+favModel.getId()+" añadido a favoritos",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
    //Method called from FilterFragment to show the Favorite ArrayList into RecyclerView
    public void showFavs () {
        if (favList.size() > 0){
            postRv.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));
            postsAdapter = new PostsAdapter(MainActivity.this,favList,true);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(postRv);
            postRv.setAdapter(postsAdapter);
        }else{
            Toast.makeText(this,"Tu lista de favoritos está vacia",Toast.LENGTH_SHORT).show();
        }
    }
    //Method called from FilterFragment to show the complete Post ArrayList
    public void cleanFilter () {
        if (postModelArrayList.size() > 0){
            postRv.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));
            postsAdapter = new PostsAdapter(MainActivity.this,postModelArrayList,false);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(postRv);
            postRv.setAdapter(postsAdapter);
        }
    }
}