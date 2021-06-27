package com.sommelier.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sommelier.services.GalleryApi;
import com.sommelier.adapter.GalleryRecyclerAdapter;
import com.sommelier.R;
import com.sommelier.model.GalleryModel.FlickrPhoto;

import retrofit2.Call;
import retrofit2.Callback;


public class GalleryFragment extends Fragment {

    public static final String TAG = "GalleryFragmentTag";

    // int[] integers = null;
    private Context context;
    private RecyclerView recyclerView;
    private FlickrPhoto list;
    private GalleryRecyclerAdapter adapter;
    private String userInput;
    private SwipeRefreshLayout swipeRefreshLayout;
    // private ProgressBar progressBar;
    //private List<Photo> mItemList;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_main_activity,
                container, false);
        setHasOptionsMenu(true);

        context = getContext();

        int numColumns = getResources().getInteger(R.integer.gallery_columns);
        recyclerView = view.findViewById(R.id.imageList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numColumns));

        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        // Настройте прослушиватель обновления, который запускает загрузку новых данных
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Не забудьте очистить старые элементы, прежде чем добавлять в новые
                adapter.clear();
                // ...данные вернулись, добавьте новые элементы в ваш адаптер...
                adapter.addAll(list.getPhotos().getPhoto());
                // Теперь мы вызываем setRefreshing (false), чтобы завершить обновление сигнала
                swipeRefreshLayout.setRefreshing(false);

                getData();
            }
        });
        // Настройте освежающие цвета
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        getData();

        return view;
    }

    private void getData() {
//        progressBar = new ProgressBar(getContext());
//        progressBar = getActivity().findViewById(R.id.progressGallery);

        Call<FlickrPhoto> imageList = GalleryApi.getImageService().searchTitle();
        imageList.enqueue(new Callback<FlickrPhoto>() {

            @Override
            public void onResponse(Call<FlickrPhoto> call, retrofit2.Response<FlickrPhoto> response) {
                if (response.isSuccessful()) {

                    // progressBar.setVisibility(View.GONE);

                    list = response.body();
                    assert list != null;
                    adapter = new GalleryRecyclerAdapter(context, list.getPhotos().getPhoto());
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(context, R.string.toast_error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FlickrPhoto> call, Throwable t) {
                Toast.makeText(getContext(), R.string.toast_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }
}












