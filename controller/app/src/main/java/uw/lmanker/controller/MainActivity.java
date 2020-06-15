package uw.lmanker.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity implements
        MainFrag.OnFragmentInteractionListener{
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFrag())
                    .commit();
        }
    }
    public void onFragmentInteraction(String name) {
        Log.v(name, "this is what the fragment sent");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        getSupportFragmentManager().beginTransaction();
        String ip = name.split(",")[0];
        int port = Integer.parseInt(name.split(",")[1]);
        String stats = name.split(",")[2];
        transaction.replace(R.id.container, new ClientFrag(ip,port,stats));
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
