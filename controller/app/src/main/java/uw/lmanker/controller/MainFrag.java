package uw.lmanker.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainFrag extends Fragment {
    private OnFragmentInteractionListener mListener;
    EditText ip;
    EditText port;
    Button button;
    EditText name, armor, bullet, scan;
    public MainFrag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState){
        ip = rootView.findViewById(R.id.ip);
        port = rootView.findViewById(R.id.port);
        name = rootView.findViewById(R.id.name);
        armor = rootView.findViewById(R.id.armor);
        bullet = rootView.findViewById(R.id.bullet);
        scan = rootView.findViewById(R.id.scan);
        button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mListener != null) {
                    int sum = Integer.parseInt(armor.getText().toString())+
                            Integer.parseInt(bullet.getText().toString())+
                            Integer.parseInt(scan.getText().toString());
                    if (sum <= 5) {
                        String temp = ip.getText().toString()
                                + "," + port.getText().toString();
                        temp += "," + name.getText().toString() + " " +
                                armor.getText().toString() + " " +
                                bullet.getText().toString() + " " +
                                scan.getText().toString();
                        mListener.onFragmentInteraction(temp);
                    }
                    else{
                        Toast toast = Toast.makeText(getContext(), "Total stats" +
                                        " must be less than or equal to five."
                                , Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String name);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Activity activity = getActivity();
        try{
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + "must have frag listenener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mListener = null;
    }
}
