package com.sommelier.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.sommelier.R;
import com.sommelier.model.SpecificationsModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

// Для відображення більш складних призначених для користувача даних ,
// необхідно реалізувати свій власний TableDataAdapter.
// Тому нам необхідно реалізувати метод GetCellView(int rowIndex, int columnIndex,
// ViewGroup parentView) в getCellView.


public class SortableTableDataAdapter extends LongPressAwareTableDataAdapter<SpecificationsModel> {

    private static final int TEXT_SIZE = 14;
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    public SortableTableDataAdapter(final Context context,
                                    final List<SpecificationsModel> data,
                                    final TableView<SpecificationsModel> tableView) {
        super(context, data, tableView);
    }

    // Цей метод викликається для кожного елементу таблиці і має повернула View, яке буде
    // відображатися в осередку з даними RowIndex і ColumnIndex.
    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        // Отримати дані рядка
        final SpecificationsModel grapes = getRowData(rowIndex);
        // Створити осередок для кожного стовпчика
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderGrapesName(grapes);
                break;
            case 1:
                renderedView = renderSorty(grapes);
                break;
            case 2:
                renderedView = renderTerm(grapes);
                break;
            case 3:
                renderedView = renderFrost(grapes);
                break;
            case 4:
                renderedView = renderColor(grapes);
                break;
            case 5:
                renderedView = renderGrowth(grapes);
                break;
            case 6:
                renderedView = renderWeight(grapes);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
//        final SpecificationsModel grapes = getRowData(rowIndex);
//        View renderedView = null;
//
//        switch (columnIndex) {
//            case 1:
//                renderedView = renderSorty(grapes);
//                //renderedView = renderEditableGrapesName(grapes);
//                break;
//            default:
//                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
//        }

        return null;
    }

//    private View renderEditableGrapesName(final SpecificationsModel grapes) {
//        final EditText editText = new EditText(getContext());
//        editText.setText(grapes.getName());
//        editText.setPadding(20, 40, 20, 40);
//        editText.setTextSize(TEXT_SIZE);
//        editText.setSingleLine();
//        editText.addTextChangedListener(new GrapesNameUpdater(grapes));
//        return editText;
//    }

    private View renderGrapesName(final SpecificationsModel grapes) {
        return renderString(grapes.getName());
    }

    private View renderSorty(final SpecificationsModel grapes) {
        return renderString(grapes.getSort());
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 0, 20, 0);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorSecondary));
        return textView;
    }

    private View renderTerm(final SpecificationsModel grapes) {
        return renderString(grapes.getTerm());
    }

    private View renderFrost(final SpecificationsModel grapes) {
        final String frostString = NUMBER_FORMAT.format(grapes.getFrost()) + "";

        final TextView textView = new TextView(getContext());
        textView.setText(frostString);
        textView.setPadding(20, 60, 20, 60);
        textView.setTextSize(TEXT_SIZE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorSecondary));

        if (grapes.getFrost() < 22) {
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        } else if (grapes.getFrost() > 27) {
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorError));
        }

        return textView;
    }

    private View renderColor(final SpecificationsModel grapes) {
        return renderString(grapes.getColor());
    }

    private View renderGrowth(final SpecificationsModel grapes) {
        return renderString(grapes.getGrowth());
    }

    private View renderWeight(final SpecificationsModel grapes) {
        final String weightString = NUMBER_FORMAT.format(grapes.getWeight()) + "";

        final TextView textView = new TextView(getContext());
        textView.setText(weightString);
        textView.setPadding(20, 0, 20, 0);
        textView.setTextSize(TEXT_SIZE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.textColorSecondary));
        return textView;
    }

//    private static class GrapesNameUpdater implements TextWatcher {
//
//        private SpecificationsModel grapesToUpdate;
//
//        public GrapesNameUpdater(SpecificationsModel grapesToUpdate) {
//            this.grapesToUpdate = grapesToUpdate;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            // no used
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            // not used
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            grapesToUpdate.setName(s.toString());
//        }
//    }
}

