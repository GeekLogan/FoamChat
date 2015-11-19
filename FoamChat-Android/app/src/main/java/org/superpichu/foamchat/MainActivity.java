package org.superpichu.foamchat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Handler;

import foamchat.ChatLog;
import foamchat.EncryptionMachine;
import foamchat.FoamChatKernel;
import foamchat.Message;
import foamchat.User;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayAdapter adapter;
    List<String> users;
    Thread thread;
    Handler updater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        users = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        listView.setAdapter(adapter);
        thread = new Thread(new KernelThread());
        thread.start();
    }

    public class KernelThread implements Runnable{

        @Override
        public void run() {
            String keyFile = getFilesDir()+"/key";
            String name = "M8";
            String bootIp = "10.0.0.100";
            FoamChatKernel kernel = null;
            EncryptionMachine encryptionMachine = null;
            try {
                kernel = new FoamChatKernel(keyFile, name, bootIp);
                encryptionMachine = kernel.encryptor;
                ChatLog chatLog = kernel.chatLog;

                do{
                    if(chatLog.users.length > adapter.getCount()) {
                        chatLog.lockWait();
                        for (User user : chatLog.users) {
                            String msg = user.displayName + ":" + user.id;
                            if (adapter.getPosition(msg) < 0) {
                                adapter.add(msg);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        chatLog.unlock();
                    }
                    //System.out.println(chatLog.users);
                }while (true);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
