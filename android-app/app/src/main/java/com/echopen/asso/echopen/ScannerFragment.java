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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        final ImageView image = (ImageView) view.findViewById(R.id.imageView);
        //instantiate images manager to save images selected by user 
        final imagesHandler imagesHandler = new imagesHandler(getActivity().getFilesDir());

        EchOpenApplication app = (EchOpenApplication) getActivity().getApplication();
        //listen to stream from probe to display images
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

        //gesture detector on main button
        final GestureDetector gd = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                //planned event : screenshot
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //planned event : freeze on a frame to annotate, draw or zoom
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                //planned event record video
            }
        });

        //gesture detector on view
        final GestureDetector gdOnView = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
            
            @Override
            public boolean onDown(MotionEvent e) {
                //return true on that event otherwise does not work
                return true;
            }

            //flag to handle two actions on single event : display or hide seekbars
            boolean toClose = false;
            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                //event to make rulers settings appear
                Log.d("tap", "OnDoubleTap" + e);
                if (e.getAction() == MotionEvent.ACTION_DOWN && !toClose) {
                    //make seekbars and texts visible
                    View seekBarH = getActivity().findViewById(R.id.seekBarH);
                    seekBarH.setVisibility(View.VISIBLE);
                    View freqText = getActivity().findViewById(R.id.frequence);
                    freqText.setVisibility(View.VISIBLE);
                    View seekBarV = getActivity().findViewById(R.id.seekBarV);
                    seekBarV.setVisibility(View.VISIBLE);
                    View gainText = getActivity().findViewById(R.id.gain);
                    gainText.setVisibility(View.VISIBLE);
                    
                    //flag to true to know event done
                    toClose = true;
                } else if (e.getAction() == MotionEvent.ACTION_DOWN && toClose) {
                    //hide seekbars and texts
                    View seekBarH = getActivity().findViewById(R.id.seekBarH);
                    seekBarH.setVisibility(View.INVISIBLE);
                    View freqText = getActivity().findViewById(R.id.frequence);
                    freqText.setVisibility(View.INVISIBLE);
                    View seekBarV = getActivity().findViewById(R.id.seekBarV);
                    seekBarV.setVisibility(View.INVISIBLE);
                    View gainText = getActivity().findViewById(R.id.gain);
                    gainText.setVisibility(View.INVISIBLE);

                    //flag to false to know event done
                    toClose = false;
                }
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                //planned event : annotate frame
                return true;
            }
        });

        //onTouch listener on view
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gdOnView.onTouchEvent(event);
            }
        });

        //onTouch listener on main button
        Button mainButton = (Button) view.findViewById(R.id.button2);
        mainButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
}
