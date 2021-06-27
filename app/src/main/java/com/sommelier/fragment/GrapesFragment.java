package com.sommelier.fragment;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sommelier.R;
import com.sommelier.adapter.RecyclerAdapter;
import com.sommelier.model.SpecificationsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class GrapesFragment extends Fragment implements SearchView.OnQueryTextListener {

    public static final String TAG = "GrapesFragmentTag";

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private Query query;
    private Spinner mSpinner;
    private List<SpecificationsModel> modelList;
    private RecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    public GrapesFragment() {
        // Обязательный пустой публичный конструктор
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grapes_fragment,
                container, false);
        setHasOptionsMenu(true);

//        getActivity().setRequestedOrientation(
//                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        assert activity != null;
        activity.setSupportActionBar(toolbar);
        Objects.requireNonNull(activity.getSupportActionBar()).setTitle("");

        progressBar = view.findViewById(R.id.progressGrapes);

        mSpinner = view.findViewById(R.id.spinner);

        int numColumns = getResources().getInteger(R.integer.search_results_columns);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), numColumns, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        // 1. Увесь список об’єктів

        //databaseReference = FirebaseDatabase.getInstance().getReference().child("grapes").child("ru");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("grapes/uk");
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
        databaseReference.keepSynced(true);

//        query = FirebaseDatabase
//                .getInstance()
//                .getReference("grapes")
//                .child("uk");
//        query.addListenerForSingleValueEvent(valueEventListener);

        spinnerRegionToolbar();

        swipeRefreshLayout = view.findViewById(R.id.swipeContainer);
        // Настройте прослушиватель обновления, который запускает загрузку новых данных
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Не забудьте очистить старые элементы, прежде чем добавлять в новые
                adapter.clear();
                // ...данные вернулись, добавьте новые элементы в ваш адаптер...
                //adapter.addAll(list.getPhotos().getPhoto());
                // Теперь мы вызываем setRefreshing (false), чтобы завершить обновление сигнала
                swipeRefreshLayout.setRefreshing(false);

                databaseReference.addListenerForSingleValueEvent(valueEventListener);

                spinnerRegionToolbar();
            }
        });
        // Настройте освежающие цвета
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // getData();

        return view;
    }

    private final ValueEventListener valueEventListener = new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            modelList = new ArrayList<>();

            if (dataSnapshot.exists()) {

                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    SpecificationsModel grapes = productSnapshot.getValue(SpecificationsModel.class);
                    modelList.add(grapes);

                    // Сортую об’єкти за алфавітом
                    Collections.sort(modelList, new Comparator<SpecificationsModel>() {
                        @Override
                        public int compare(SpecificationsModel o1, SpecificationsModel o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }

                if (getActivity() != null) {
                    adapter = new RecyclerAdapter(getActivity(), modelList);
                    recyclerView.setAdapter(adapter);

                    adapter.notifyDataSetChanged();

                    progressBar.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getContext(), "Opsss.... Щось не так", Toast.LENGTH_SHORT).show();
        }
    };

    private void spinnerRegionToolbar() {

        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(getContext(),
                R.layout.maps_item_spinner,
                getResources().getStringArray(R.array.region));
        mAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch (position) {
                    case 0:
                        databaseReference.addListenerForSingleValueEvent(valueEventListener);
                        break;
                    case 1:
                        query = FirebaseDatabase
                                .getInstance()
                                .getReference("grapes")
                                .child("uk")
                                .orderByChild("sort")
                                .equalTo("столовий");
                        query.addListenerForSingleValueEvent(valueEventListener);
                        break;
                    case 2:
                        query = FirebaseDatabase
                                .getInstance()
                                .getReference("grapes")
                                .child("uk")
                                .orderByChild("sort")
                                .equalTo("технічний");
                        query.addListenerForSingleValueEvent(valueEventListener);
                        break;
                    case 3:
                        query = FirebaseDatabase
                                .getInstance()
                                .getReference("grapes")
                                .child("uk")
                                .orderByChild("sort")
                                .equalTo("киш-миш");
                        query.addListenerForSingleValueEvent(valueEventListener);
                        break;
                    case 4:
                        query = FirebaseDatabase
                                .getInstance()
                                .getReference("grapes")
                                .child("uk")
                                .orderByChild("sort")
                                .equalTo("підщепа");

                        query.addListenerForSingleValueEvent(valueEventListener);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @SuppressLint("UseRequireInsteadOfGet")
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        searchView.setQueryHint("Пошук сорту винограда");
        //  searchView.st.setHintTextColor(getResources().getColor(Color.WHITE));

        // Програмно змінюю шрифт підказки у пошуку сорту винограду
        Typeface tf = ResourcesCompat.getFont(getContext(),R.font.open_sans_regular);
        TextView searchText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchText.setTypeface(tf);

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adapter.setItems(modelList);
                return true;
            }
        });

        ImageView searchIcon = searchView.findViewById(R.id.search_button);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(Objects.requireNonNull(getContext()), R.drawable.menu_ic_search));

        ImageView closeIcon = searchView.findViewById(R.id.search_close_btn);
        closeIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.toolbar_ic_close));

        // встановлюю ширину вікна пошуку на весь екран
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // При натисканні на пошук, роблю спінер невидимим
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // searchView расширен
                    mSpinner.setVisibility(View.INVISIBLE);
                } else {
                    mSpinner.setVisibility(View.VISIBLE);
                }
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        final List<SpecificationsModel> filteredModelList = filter(modelList, newText);
        adapter.setItems(filteredModelList);

        return true;
    }

    private List<SpecificationsModel> filter(List<SpecificationsModel> datas, String newText) {
        newText = newText.toLowerCase().trim();

        final List<SpecificationsModel> filteredModelList = new ArrayList<>();
        for (SpecificationsModel data : datas) {

            final String text = data.getName().toLowerCase().trim();
            if (text.contains(newText)) {
                filteredModelList.add(data);
            }
        }

        return filteredModelList;
    }
}
