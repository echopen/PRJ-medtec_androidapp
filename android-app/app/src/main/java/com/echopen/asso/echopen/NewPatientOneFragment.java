package com.echopen.asso.echopen;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPatientOneFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Button nextButton;
    private ImageButton men_button;
    private ImageButton women_button;
    public NewPatientOneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        nextButton = (Button) getView().findViewById(R.id.goStep2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goStep2();
            }
        });

        men_button = (ImageButton) getView().findViewById(R.id.imageButtonMan1);
        women_button = (ImageButton) getView().findViewById(R.id.imageButtonWoman1);

        // when you click on the Men Button
        men_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String resource = (String) men_button.getTag();

                if("man_blue".equals(resource)){
                    men_button.setImageResource(R.drawable.man);
                    men_button.setTag("man");
                }else {
                    men_button.setImageResource(R.drawable.man_blue);
                    women_button.setImageResource(R.drawable.woman);
                    men_button.setTag("man_blue");
                }
            }
        });

        // when you click on the woman Button
        women_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String resource = (String) women_button.getTag();

                if("woman_blue".equals(resource)){
                    women_button.setImageResource(R.drawable.woman);
                    women_button.setTag("woman");
                }else {
                    women_button.setImageResource(R.drawable.woman_blue);
                    men_button.setImageResource(R.drawable.man);
                    women_button.setTag("woman_blue");
                }
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_patient_one, container, false);
    }

    public static NewPatientOneFragment newInstance () {
        NewPatientOneFragment f = new NewPatientOneFragment();
        return f;
    }

    public interface OnFragmentInteractionListener {
        void goStep2();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString());
        }

    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }

}
