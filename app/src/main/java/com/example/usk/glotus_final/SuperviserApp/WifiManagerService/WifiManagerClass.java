package com.example.usk.glotus_final.SuperviserApp.WifiManagerService;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usk.glotus_final.R;
import com.example.usk.glotus_final.SuperviserApp.ReceptionFiles.Etiketka;
import com.example.usk.glotus_final.SuperviserApp.ReceptionFiles.ExpedPage;
import com.example.usk.glotus_final.SuperviserApp.SuperviserListFiles.SuperviserListActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION;

public class WifiManagerClass extends AppCompatActivity {
    private Button btnStatusWifi, btnSearchWifi, btnSendWifi;
    private ListView listView;
    private TextView messageWifi;
    TextView connectStatus;
    private EditText sendData;
    private MenuItem btn_generate;
    private MenuItem btn_ok;
    private MenuItem btn_print;
    private MenuItem btn_next;

    private WifiManager wifiManager;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;

    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;

    private List<WifiP2pDevice> peers=new ArrayList<WifiP2pDevice>();
    private String[] deviceNameArray;
    private WifiP2pDevice[] deviceArray;

    static final int MESSAGE_READ=1;

    private ServerClass serverClass;
    private ClientClass clientClass;
    private SendReceive sendReceive;
    private writeOnStream wrt;

    private ArrayList<File> imgFile=Etiketka.imgFile;
    private ArrayList<File> imgS;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_direct);

        initialWork();
        exqListener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pdf_menu, menu);
        btn_generate = menu.findItem(R.id.pdf_create);
        btn_generate.setVisible(false);

        btn_next=menu.findItem(R.id.btn_next);
        btn_next.setVisible(false);

        btn_ok=menu.findItem(R.id.btn_ok);
        btn_ok.setVisible(true);

        btn_print=menu.findItem(R.id.btn_print);
        btn_print.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.btn_ok){
            Intent myintent = new Intent(WifiManagerClass.this, SuperviserListActivity.class);
            startActivity(myintent);
        }
        return super.onOptionsItemSelected(item);
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case MESSAGE_READ:
                    byte[] readBuffer=(byte[]) message.obj;
                    String tempMessage=new String(readBuffer,0,message.arg1);
                    messageWifi.setText(tempMessage);
                    break;
            }
            return true;
        }
    });

    private void exqListener() {
        btnStatusWifi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(wifiManager.isWifiEnabled()){
                    wifiManager.setWifiEnabled(false);
                    btnStatusWifi.setText("On");
                }else{
                    wifiManager.setWifiEnabled(true);
                    btnStatusWifi.setText("Off");
                }
            }
        });

        btnSearchWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        connectStatus.setText("Discovery Started");
                    }
                    @Override
                    public void onFailure(int i) {
                        connectStatus.setText("Discovery starting failed");
                    }
                });
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final WifiP2pDevice device=deviceArray[i];
                WifiP2pConfig config=new WifiP2pConfig();
                config.deviceAddress=device.deviceAddress;

                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getApplicationContext(),"Coneccted to"+device.deviceName,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int i) {
                        Toast.makeText(getApplicationContext(),"Not connected",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnSendWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=sendData.getText().toString();
                for(File imgFiles:imgFile) {
                    wrt = new writeOnStream(imgFiles, sendReceive.getOutputStream());
                    wrt.start();
                }
            }
        });
    }

    private void initialWork() {
        btnStatusWifi=findViewById(R.id.btn_wifiStatus);
        btnSearchWifi=findViewById(R.id.btn_search);
        btnSendWifi=findViewById(R.id.btn_sendWifi);
        listView=findViewById(R.id.wifiPeerList);
        messageWifi=findViewById(R.id.tv_message);
        connectStatus=findViewById(R.id.status);
        sendData=findViewById(R.id.editText);

        wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel=mManager.initialize(this,getMainLooper(),null);

        mReceiver=new WifiDirectBroadcastReceiver(mManager,mChannel, this);
        mIntentFilter=new IntentFilter();

        mIntentFilter.addAction(WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    WifiP2pManager.PeerListListener peerListListener=new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            if(!wifiP2pDeviceList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(wifiP2pDeviceList.getDeviceList());

                deviceNameArray=new String[wifiP2pDeviceList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];
                int index=0;

                for(WifiP2pDevice device: wifiP2pDeviceList.getDeviceList()){
                    deviceNameArray[index]=device.deviceName;
                    deviceArray[index]=device;
                    index++;
                }

                ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),
                                                    android.R.layout.simple_list_item_1,deviceNameArray);
                listView.setAdapter(adapter);

            }
            if(peers.size()==0){
                Toast.makeText(getApplicationContext(),"No device found",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener=new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
            final InetAddress groupOwnerAddress=wifiP2pInfo.groupOwnerAddress;
            if(wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner){
                connectStatus.setText("Host");
                serverClass=new ServerClass();
                serverClass.start();
            }else if(wifiP2pInfo.groupFormed){
                connectStatus.setText("Client");
                clientClass=new ClientClass(groupOwnerAddress);
                clientClass.start();
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        registerReceiver(mReceiver,mIntentFilter);

    }

    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public class ServerClass extends Thread{
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run(){
            try {
                serverSocket=new ServerSocket(8888);
                socket=serverSocket.accept();
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class SendReceive extends Thread{
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public OutputStream getOutputStream() {
            return outputStream;
        }

        public void setOutputStream(OutputStream outputStream) {
            this.outputStream = outputStream;
        }

        public SendReceive(Socket skt){
            socket=skt;

            try {
                inputStream=socket.getInputStream();
                outputStream=socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            byte[] buffer=new byte[1024];
            int bytes;

            File mFolder;
            File pdf=null;
            while (socket!=null){
                try {
                    mFolder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                    pdf=new File(mFolder,"Exped"+ "_"+ System.currentTimeMillis() + ".pdf");
                    if (!mFolder.exists()) {
                        mFolder.mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(pdf);
                    bytes=inputStream.read(buffer);
                    while(bytes!=-1){
                        handler.obtainMessage(MESSAGE_READ,bytes,-1,buffer).sendToTarget();
                        out.write(buffer,0,bytes);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           imgS.add(pdf);
        }
    }

    private class writeOnStream extends Thread{
        OutputStream outputStream;
        File imageFile;

        public writeOnStream(File file,OutputStream outputStr){
            this.imageFile=file;
            this.outputStream=outputStr;
        }

        public void run(){
            byte[] bytes=new byte[1024];
            int count=0;
            try {
                String uri=imageFile.toURI().toString();
                File file=new File(uri);
                InputStream in=new FileInputStream(file);

                while ((count=in.read(bytes))>0){
                    outputStream.write(bytes,0,count);
                }
                sendReceive.setOutputStream(outputStream);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public class ClientClass extends Thread{
        Socket socket;
        String hostAdd;

        public ClientClass(InetAddress hostAddress){
            hostAdd=hostAddress.getHostAddress();
            socket=new Socket();
        }

        @Override
        public void run(){
            try {
                socket.connect(new InetSocketAddress(hostAdd,8888),500);
                sendReceive=new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
