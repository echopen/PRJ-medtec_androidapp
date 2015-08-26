package com.echopen.asso.echopen.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.echopen.asso.echopen.R;

import java.util.ArrayList;

/**
 * Created by mehdibenchoufi on 26/08/15.
 */
public class FilterDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ArrayList filtersItems = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        builder.setTitle(getResources().getString(R.string.filter_message))
                .setMultiChoiceItems(R.array.filter_choice, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    filtersItems.add(which);
                                } else if (filtersItems.contains(which)) {
                                    filtersItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                .setPositiveButton(getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // todo : to be completed filters in action
            }
        })
                .setNegativeButton(getResources().getString(R.string.cancel_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // todo : cancel dialog box
                    }
                });
        return builder.create();
    }
}

