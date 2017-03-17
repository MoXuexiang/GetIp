package com.example.xuexiang.getip;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private TextView ipTextView = null;
    private TextView nameTextView = null;
    private ConnectivityManager mConnectivityManager = null;
    private NetworkInfo mActiveNetInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nameTextView = (TextView) findViewById(R.id.nameTextView);
        ipTextView = (TextView) findViewById(R.id.ipTextView);
        mConnectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mActiveNetInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mActiveNetInfo == null)
            myDialog();
        else
            setUpInfo();

    }

    public String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface.getNetworkInterfaces(); mEnumeration.hasMoreElements(); ) {
                NetworkInterface intf = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIPAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            &&!(inetAddress instanceof Inet6Address)) {
                        return inetAddress.getHostAddress();
                    }
                }

            }
        } catch (SocketException ex) {
            Log.e("Error", ex.toString());
        }
        return null;
    }

    private void setUpInfo() {
        if (mActiveNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            nameTextView.setText("网络类型：WIFI");
            ipTextView.setText("IP地址：" + getLocalIPAddress());
        } else {
            nameTextView.setText("网络类型：未知");
            ipTextView.setText("IP地址：");
        }

    }

    private void myDialog() {
        AlertDialog mDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("注意")
                .setMessage("当前网络不可用，请检查网络！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        MainActivity.this.finish();
                    }
                })
                .create();
        mDialog.show();
    }

}
