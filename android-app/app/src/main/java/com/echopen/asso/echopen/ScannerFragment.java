package com.echopen.asso.echopen;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingMode;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationContract;
import com.echopen.asso.echopen.echography_image_visualisation.EchographyImageVisualisationPresenter;

import static com.echopen.asso.echopen.utils.Constants.Http.REDPITAYA_PORT;

public class ScannerFragment extends Fragment {
    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        final ImageView image = (ImageView) view.findViewById(R.id.imageView);
        final imagesHandler imagesHandler = new imagesHandler(getActivity().getFilesDir());

        EchOpenApplication app = (EchOpenApplication) getActivity().getApplication();
        EchographyImageStreamingService stream = app.getEchographyImageStreamingService();

        EchographyImageVisualisationPresenter presenter = new EchographyImageVisualisationPresenter(stream, new EchographyImageVisualisationContract.View() {
            @Override
            public void refreshImage(final Bitmap iBitmap) {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imagesHandler.saveCacheImage(iBitmap);
                            image.setImageBitmap(iBitmap);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setPresenter(EchographyImageVisualisationContract.Presenter presenter) {

            }
        });
        EchographyImageStreamingMode mode = new EchographyImageStreamingTCPMode("10.236.115.40", REDPITAYA_PORT);

        stream.connect(mode, getActivity());
        presenter.start();
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final GestureDetector gd = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                Log.d("tap", "OnDoubleTap" + e);

                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d("tap", "onSingleTapConfirmed" + e);

                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d("tap", "onLongPress" + e);
            }


        });

        Button mainButton = (Button) getActivity().findViewById(R.id.button2);
        mainButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gd.onTouchEvent(event);
            }
        });

        //-----------------------------------------------------------------

    }
}