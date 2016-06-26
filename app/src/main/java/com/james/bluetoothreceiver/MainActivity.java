package com.james.bluetoothreceiver;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final UUID DEFAULT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private TextView connectionMessageView;
    private TextView messageView;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private Button button_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionMessageView = (TextView) findViewById(R.id.connectionMessage);
        messageView = (TextView) findViewById(R.id.message);
        button_connect = (Button) findViewById(R.id.button_connect);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Device doesnt Support Bluetooth", Toast.LENGTH_SHORT).show();
        } else if (!bluetoothAdapter.isEnabled()) {
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
        BluetoothDevice device = getBluetoothDevice();
        try {
            button_connect.setEnabled(false);
            socket = makeSocketConnection(device);
            final BufferedReader reader = getBufferedReader(socket);
            pollInput(reader);
        } catch (IOException e) {
            button_connect.setEnabled(true);
            e.printStackTrace();
        }
    }
    @Nullable
    private BluetoothDevice getBluetoothDevice() {
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices(); // get connected devices from the adapter
        BluetoothDevice device = null;
        if (bondedDevices.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Pair the Device first", Toast.LENGTH_SHORT).show();
        } else {
            for (BluetoothDevice iterator : bondedDevices) {
                device = iterator;
            }
        }
        return device;
    }
    private BluetoothSocket makeSocketConnection(BluetoothDevice device) throws IOException {
        socket = device.createRfcommSocketToServiceRecord(DEFAULT_UUID);
        socket.connect();
        setConnectionMessageText("Connected to socket");
        return socket;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private BufferedReader getBufferedReader(BluetoothSocket socket) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }
    private void pollInput(final BufferedReader reader) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        String line = reader.readLine();
                        setMessageText(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void setMessageText(final String messageText) {
        MainActivity.this.runOnUiThread(
                new Runnable() {
                    public void run() {
                        messageView.setText(messageText);
                    }
                });
    }
    private void setConnectionMessageText(final String messageText) {
        MainActivity.this.runOnUiThread(
                new Runnable() {
                    public void run() {
                        connectionMessageView.setHint(messageText);
                    }
                });
    }

    public void disconnect(View view) {
        try {
            socket.close();
            setConnectionMessageText("Waiting for connection");
            setMessageText("...");
            button_connect.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetSmallText(View view) {
        messageView.setText("");
    }
}
