package uw.lmanker.controller;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;



public class ClientFrag extends Fragment{
    String ip;
    int port;
    private ClientThread clientThread;
    private Thread thread;
    String servermsg;
    ImageButton moveU;
    ImageButton moveL;
    ImageButton moveR;
    ImageButton moveD;
    ImageButton fireU;
    ImageButton fireL;
    ImageButton fireR;
    ImageButton fireD;
    ProgressBar moveProg, fireProg;
    int moveMax = 0;
    int fireMax = 0;
    String stats = "luke 0 0 3";

    ClientFrag(String ip, int port, String stats){
        this.ip = ip;
        this.port = port;
        this.stats = stats;
        Log.v("info", this.ip + this.port);
        Log.v("stats:", stats);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_client, container, false);
        return rootView;
    }
    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState){
        clientThread = new ClientThread();
        thread = new Thread(clientThread);
        thread.start();
        moveU = rootView.findViewById(R.id.moveU);
        moveL = rootView.findViewById(R.id.moveL);
        moveR = rootView.findViewById(R.id.moveR);
        moveD = rootView.findViewById(R.id.moveD);
        fireU = rootView.findViewById(R.id.fireU);
        fireL = rootView.findViewById(R.id.fireL);
        fireR = rootView.findViewById(R.id.fireR);
        fireD = rootView.findViewById(R.id.fireD);
        moveProg = rootView.findViewById(R.id.moveProg);
        fireProg = rootView.findViewById(R.id.fireProg);
        moveU.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(servermsg.contains("Status")){
                    clientThread.sendMessage("move 0 -1");
                }
            }
        });
        moveR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(servermsg.contains("Status")){
                    clientThread.sendMessage("move 1 0");
                }
            }
        });
        moveL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(servermsg.contains("Status")){
                    clientThread.sendMessage("move -1 0");
                }
            }
        });
        moveD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(servermsg.contains("Status")){
                    clientThread.sendMessage("move 0 1");
                }
            }
        });

        fireU.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(servermsg.contains("Status")){
                    clientThread.sendMessage("fire 0");
                }
            }
        });
        fireR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(servermsg.contains("Status")){
                    clientThread.sendMessage("fire 90");
                }
            }
        });
        fireL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(servermsg.contains("Status")){
                    clientThread.sendMessage("fire 270");
                }
            }
        });
        fireD.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(servermsg.contains("Status")){
                    clientThread.sendMessage("fire 180");
                }
            }
        });

    }

    class ClientThread implements Runnable {

        private Socket socket;
        private BufferedReader input;
        private String message;

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(ip);
                socket = new Socket(serverAddr, port);

                while (!Thread.currentThread().isInterrupted()) {

                    this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    message = input.readLine();
                    if (null == message || "Disconnect".contentEquals(message)) {
                        Thread.interrupted();
                        message = "Server Disconnected.";
                        break;
                    }
                    servermsg = message.split(" ")[0];
                    Log.v("server sent:", message);
                    boolean test = (servermsg.contains("setup"));
                    boolean status = (servermsg.contains("Status"));
                    if(test){
                        sendMessage(stats);
                    }
                    if(status){
                        int move = Integer.parseInt(message.split(" ")[3]) * -1;
                        int fire = Integer.parseInt(message.split(" ")[4]) * -1;
                        if(move > moveMax){
                            moveMax = move;
                            moveProg.setProgress(0);
                        }
                        else{
                            float percent = ((float)moveMax - move)/moveMax * 100;
                            moveProg.setProgress((int)percent);
                        }
                        if(fire > fireMax){
                            fireMax = fire;
                            fireProg.setProgress(0);
                        }
                        else{
                            float percent = ((float)fireMax - fire)/fireMax * 100;
                            fireProg.setProgress((int)percent);
                        }
                    }
                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        void sendMessage(final String message) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (socket != null) {
                            PrintWriter out = new PrintWriter(new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream())),
                                    true);
                            out.println(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != clientThread) {
            clientThread.sendMessage("Disconnect");
            clientThread = null;
        }
    }
}