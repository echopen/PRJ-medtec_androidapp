package com.echopen.asso.echopen.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.echopen.asso.echopen.MainActivity;
import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.model.Data.BitmapDisplayerFactory;
import com.echopen.asso.echopen.utils.Constants;

/**
 * Created by mehdibenchoufi on 26/08/15.
 */
public class ConstantDialogFragment extends DialogFragment {

    private AlertDialog.Builder builder;

    private AlertDialog alertDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int[] constantProtocol = {0};
        builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        builder.setTitle(getResources().getString(R.string.protocol_message))
                .setMultiChoiceItems(R.array.protocol_choice, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    constantProtocol[0] = which;
                                } else if (constantProtocol[0] == which) {
                                    constantProtocol[0] = 0;
                                }
                            }
                        })
                .setPositiveButton(getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener() {

                    class LoadConfigTask {
                        private ProgressDialog dialog;
                        private Activity activity;

                        public LoadConfigTask(Activity activity) {
                            this.activity = activity;
                            dialog = new ProgressDialog(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        }

                        public void execute() {
                            try {
                                if (constantProtocol[0] == 0) {
                                    Constants.PreProcParam.NUM_SAMPLES = Constants.PreProcParam.LOCAL_NUM_SAMPLES;
                                    Constants.PreProcParam.NUM_IMG_DATA = Constants.PreProcParam.LOCAL_IMG_DATA;
                                    Constants.PreProcParam.SCALE_IMG_FACTOR = Constants.PreProcParam.LOCAL_SCALE_IMG_FACTOR;
                                    MainActivity.LOCAL_ACQUISITION = true;
                                } else if (constantProtocol[0] == 1) {
                                    Constants.PreProcParam.NUM_SAMPLES = Constants.PreProcParam.TCP_NUM_SAMPLES;
                                    Constants.PreProcParam.NUM_IMG_DATA = Constants.PreProcParam.TCP_IMG_DATA;
                                    Constants.PreProcParam.SCALE_IMG_FACTOR = Constants.PreProcParam.TCP_SCALE_IMG_FACTOR;
                                    MainActivity.TCP_ACQUISITION = true;
                                } else if (constantProtocol[0] == 2) {
                                    Constants.PreProcParam.NUM_SAMPLES = Constants.PreProcParam.UDP_NUM_SAMPLES;
                                    Constants.PreProcParam.NUM_IMG_DATA = Constants.PreProcParam.UDP_IMG_DATA;
                                    Constants.PreProcParam.SCALE_IMG_FACTOR = Constants.PreProcParam.UDP_SCALE_IMG_FACTOR;
                                    MainActivity.UDP_ACQUISITION = true;
                                }

                            } catch (Exception e) {
                                Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT);
                                toast.show();
                                System.out.println("loading configuration failed");
                            }
                        }
                    }

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // todo : to be completed filters in action
                        MainActivity activity = (MainActivity) getActivity();
                        LoadConfigTask loadConfigTask = new LoadConfigTask(activity);
                        loadConfigTask.execute();
                        BitmapDisplayerFactory bitmapDisplayerFactory = new BitmapDisplayerFactory();
                        activity.fetchData(bitmapDisplayerFactory);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // todo : cancel dialog box
                    }
                });
        builder.create();
        alertDialog = builder.show();
        return alertDialog;
    }

    public AlertDialog getAlertDialog(){
        return alertDialog;
    }
}

