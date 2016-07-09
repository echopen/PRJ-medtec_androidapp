package com.echopen.asso.echopen.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.echopen.asso.echopen.R;
import com.echopen.asso.echopen.filters.BaseProcess;
import com.echopen.asso.echopen.filters.ImageEnhancement;
import com.echopen.asso.echopen.filters.WaveletDenoise;

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

                    class ProcessImageTask extends AsyncTask<Void,Void,Bitmap>{
                        private ProgressDialog dialog;
                        private Activity activity;
                        private ImageView image;

                        public ProcessImageTask(Activity activity, ImageView image) {
                            this.activity = activity;
                            this.image = image;
                            dialog = new ProgressDialog(activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            dialog.setTitle("Image is being processed");
                            dialog.setMessage("Please wait...");
                            dialog.show();
                        }

                        @Override
                        protected Bitmap doInBackground(Void... voids) {
                            Bitmap bitmap = null;
                            try {
                                if(filtersItems.contains(0)) {
                                    WaveletDenoise waveletDenoise = new WaveletDenoise(image);
                                    waveletDenoise.denoise();
                                    bitmap = waveletDenoise.getBitmap();
                                }
                                else if(filtersItems.contains(1)) {
                                    ImageEnhancement imageEnhancement = new ImageEnhancement(image);
                                    imageEnhancement.enhance();
                                    bitmap = imageEnhancement.getBitmap();
                                }
                                else{
                                    BaseProcess baseProcess = new BaseProcess(image);
                                    bitmap = baseProcess.getBitmap();
                                }
                            } catch (Exception e) {
                                Toast toast = Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT);
                                toast.show();
                                System.out.println("the wavelet denoise failed");
                            }
                            return bitmap;
                        }

                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            image.setImageBitmap(bitmap);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // todo : to be completed filters in action
                        Activity activity = getActivity();
                        ImageView image = (ImageView) activity.findViewById(R.id.echo);
                        ProcessImageTask processImageTask = new ProcessImageTask(activity,image);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            processImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
                        else
                            processImageTask.execute((Void[])null);
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

