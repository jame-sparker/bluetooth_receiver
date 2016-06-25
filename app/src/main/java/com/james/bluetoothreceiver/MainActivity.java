package com.james.bluetoothreceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final UUID PORT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    TextView largeText;
    TextView smallText;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    OutputStream outputStream;

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
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableAdapter, 0);
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

    public void connect(View view) {
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices(); // get connected devices from the adapter

        if (bondedDevices.isEmpty()) { // if the device is connected
            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                Log.i("Device", iterator.getAddress());
                device = iterator;
                Log.i("Device", String.valueOf(bondedDevices.size()));
            }
            Log.i("Device","Does this print");
        }

        System.out.println("HELLOOOOOOOOOOOOOOOOOOOOOOOO");
        Log.i("Device","Hello1");
        BluetoothSocket socket = null;
        try {
            Log.i("Device","Hello2");
            socket = device.createRfcommSocketToServiceRecord(PORT_UUID);
            socket.connect();
            outputStream = socket.getOutputStream();
            String msg = "HELLOO!!!!! James\n";
            outputStream.write(msg.getBytes());
            Log.i("Device","Hello!!!!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect(View view) {

    }

    public void setLargeText(String s) {
        largeText.setText(s);
    }

    public void addToSamllText(char c) {
        smallText.setText(smallText.getText().toString() + c);
    }

    public void resetSmallText(View view) {
        smallText.setText("");

    }
}
