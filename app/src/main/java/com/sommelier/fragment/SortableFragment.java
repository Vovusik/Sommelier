package com.sommelier.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sommelier.R;
import com.sommelier.helper.SortableGrapesTableView;
import com.sommelier.activity.DetailsActivity;
import com.sommelier.adapter.SortableTableDataAdapter;
import com.sommelier.model.SpecificationsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.codecrafters.tableview.SortState;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.providers.SortStateViewProvider;


public class SortableFragment extends Fragment {

    public static final String TAG = "SortableFragmentTag";

    private List<SpecificationsModel> sortable;
    private SortableTableDataAdapter grapesTableDataAdapter;
   private SortableGrapesTableView grapesTableView;

    public SortableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sortable_activity,
                container, false);

        grapesTableView = view.findViewById(R.id.tableView);

        assert grapesTableView != null;
        grapesTableView.setHeaderSortStateViewProvider(new MySortStateViewProvider());
        grapesTableView.setSaveEnabled( true );

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("grapes").child("uk");
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
        databaseReference.keepSynced(true);

        return view;
    }

    private static class MySortStateViewProvider implements SortStateViewProvider {

        private static final int NO_IMAGE_RES = -1;

        @Override
        public int getSortStateViewResource(SortState state) {
            switch (state) {
                case SORTABLE:
                    return R.drawable.sortable_ic_arrow_defoult;
                case SORTED_ASC:
                    return R.drawable.sortable_ic_arrow_up;
                case SORTED_DESC:
                    return R.drawable.sortable_ic_arrow_down;
                default:
                    return NO_IMAGE_RES;
            }
        }
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            sortable = new ArrayList<>();
            if (dataSnapshot.exists()) {
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    SpecificationsModel pidval = productSnapshot.getValue(SpecificationsModel.class);
                    sortable.add(pidval);

                    // Сортую об’єкти за алфавітом
                    Collections.sort(sortable, new Comparator<SpecificationsModel>() {
                        @Override
                        public int compare(SpecificationsModel o1, SpecificationsModel o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }

                if (grapesTableView != null) {
                    grapesTableDataAdapter =
                            new SortableTableDataAdapter(getContext(), sortable, grapesTableView);
                    grapesTableView.addDataClickListener(new GrapesClickListener());
                    grapesTableView.setDataAdapter(grapesTableDataAdapter);
                    grapesTableView.setHeaderElevation(10);

                    grapesTableView.setHeaderBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }

                grapesTableDataAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getContext(), "Opsss.... Щось не так", Toast.LENGTH_SHORT).show();
        }
    };

    private class GrapesClickListener implements TableDataClickListener<SpecificationsModel> {

        @Override
        public void onDataClicked(final int rowIndex, final SpecificationsModel clickedData) {

            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra(DetailsActivity.EXTRA_GRAPES_ID, sortable.get(rowIndex));
            startActivity(intent);
        }
    }
}