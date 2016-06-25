package com.james.bluetoothreceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private static final UUID PORT_UUID = new UUID(115200, 0);
    TextView largeText;
    TextView smallText;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    BufferedInputStream inputStream;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        largeText = (TextView) findViewById(R.id.textView_large);
        smallText = (TextView) findViewById(R.id.textView_small);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device doesnt Support Bluetooth", Toast.LENGTH_SHORT).show();
        }

        if(!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
        }

        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices(); // get connected devices from the adapter

        if(bondedDevices.isEmpty()) { // if the device is connected
            Toast.makeText(getApplicationContext(),"Please Pair the Device first",Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                Log.i("Device Address", iterator.getAddress());
            }
        }

        BluetoothSocket socket = null;

//        handler = new FileHandler();
        try {
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
            inputStream = new BufferedInputStream(socket.getInputStream());
            int byteCount = inputStream.available();

            if(byteCount > 0){
                byte[] rawBytes = new byte[byteCount];
                inputStream.read(rawBytes);
                final String string=new String(rawBytes,"UTF-8");

                MainActivity.this.runOnUiThread(
                        new Runnable() {
                            public void run() { smallText.append(string); } });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void connect(View view){

    }

    public void disconnect(View view){

    }

    public void setLargeText(String s){
        largeText.setText(s);
    }

    public void addToSamllText(char c) {
        smallText.setText(smallText.getText().toString() + c );
    }

    public void resetSmallText(View view){
        smallText.setText("");

    }
}
