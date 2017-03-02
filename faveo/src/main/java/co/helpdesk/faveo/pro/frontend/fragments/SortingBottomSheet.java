package co.helpdesk.faveo.pro.frontend.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import co.helpdesk.faveo.pro.R;

/**
 * Created by narendra on 16/02/17.
 */

public class SortingBottomSheet extends BottomSheetDialogFragment {
    public static SortingBottomSheet getInstance() {
        return new SortingBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sort_layout, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
