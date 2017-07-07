package com.echopen.asso.echopen.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.echopen.asso.echopen.R;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocumentFragment extends Fragment {

    private boolean modaleVisible = false;

    private FragmentManager fragmentManager;

    public DocumentFragment() {
        // Required empty public constructor
    }

    AsyncTaskLoadFiles myAsyncTaskLoadFiles;

    public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

        File targetDirector;
        ImageAdapter myTaskAdapter;

        public AsyncTaskLoadFiles(ImageAdapter adapter) {
            myTaskAdapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            String ExternalStorageDirectoryPath = Environment
                    .getExternalStorageDirectory().getAbsolutePath();

            String targetPath = ExternalStorageDirectoryPath + "/Echopen/";
            targetDirector = new File(targetPath);
            myTaskAdapter.clear();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            File[] files = targetDirector.listFiles();
            for (File file : files) {
                if(!file.isDirectory()) {
                    publishProgress(file.getAbsolutePath());
                }
                if (isCancelled()) break;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            myTaskAdapter.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            myTaskAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        ArrayList<String> itemList = new ArrayList<String>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        void add(String path) {
            itemList.add(path);
        }

        void clear() {
            itemList.clear();
        }

        void remove(int index){
            itemList.remove(index);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) { // if it's not recycled, initialize some
                // attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220,
                    220);

            imageView.setImageBitmap(bm);
            return imageView;
        }

        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
                                                 int reqHeight) {

            Bitmap bm = null;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float) height
                            / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) width / (float) reqWidth);
                }
            }

            return inSampleSize;
        }

    }

    ImageAdapter myImageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_document, container, false);

        final GridView gallery = (GridView) v.findViewById(R.id.gallery);
        final ImageView imageView = (ImageView) v.findViewById(R.id.expanded_image);
        myImageAdapter = new ImageAdapter(getActivity());
        gallery.setAdapter(myImageAdapter);

        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
        myAsyncTaskLoadFiles.execute();

        //Button buttonReload = (Button) v.findViewById(R.id.reload);
        final Button buttonCloseZoom = (Button) v.findViewById(R.id.btn_closezoom);
        Button buttonBackToDashboard = (Button) v.findViewById(R.id.btn_back);
        Button btn_login = (Button) v.findViewById(R.id.btn_login);
        final RelativeLayout modale_login = (RelativeLayout) v.findViewById(R.id.modale_connexion);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle connexion modale
                if (modaleVisible) {
                    modale_login.setVisibility(View.INVISIBLE);
                    modaleVisible = false;
                } else {
                    modale_login.setVisibility(View.VISIBLE);
                    modaleVisible = true;
                }
            }
        });

        // Shows image
        AdapterView.OnItemClickListener myOnItemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item = parent.getItemAtPosition(position).toString();
                Log.d("Image path: ", item);

                Bitmap itemBitmap = BitmapFactory.decodeFile(item);
                imageView.setImageBitmap(itemBitmap);
                imageView.setVisibility(v.VISIBLE);
                buttonCloseZoom.setVisibility(v.VISIBLE);
            }
        };

        // Close image zoom
        buttonCloseZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setVisibility(v.INVISIBLE);
                buttonCloseZoom.setVisibility(v.INVISIBLE);
            }
        }) ;

        fragmentManager = getActivity().getSupportFragmentManager();

        // Back to dashboard
        buttonBackToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DashboardFragment dashboardFragment = new DashboardFragment();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                fragmentTransaction.replace(R.id.pager, dashboardFragment).commit();
            }
        });

        gallery.setOnItemClickListener(myOnItemClickListener);
      
        // Reload images list
        /*buttonReload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {

                //Cancel the previous running task, if exist.
                myAsyncTaskLoadFiles.cancel(true);

                //new another ImageAdapter, to prevent the adapter have
                //mixed files
                myImageAdapter = new ImageAdapter(getContext());
                gallery.setAdapter(myImageAdapter);
                myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
                myAsyncTaskLoadFiles.execute();
            }});*/

        return v;
    }


}
