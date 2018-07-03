package com.shout.bluetoothclient;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.bridgefy.sdk.client.Bridgefy;
import com.bridgefy.sdk.client.BridgefyClient;
import com.bridgefy.sdk.client.Device;
import com.bridgefy.sdk.client.Message;
import com.bridgefy.sdk.client.MessageListener;
import com.bridgefy.sdk.client.RegistrationListener;
import com.bridgefy.sdk.client.Session;
import com.bridgefy.sdk.client.StateListener;
import com.bridgefy.sdk.framework.exceptions.MessageException;

import java.util.HashMap;
import java.util.UUID;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * BluetoothClientPlugin
 */
public class BluetoothClientPlugin{
    /**
     * Plugin registration.
     */



    public static void registerWith(Registrar registrar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registrar.activity().requestPermissions(new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
            },0);
        }
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "bluetooth_client");
        channel.setMethodCallHandler(new BluetoothClientMethodCallHandler(registrar.context()));
        final EventChannel messageReceivedChannel = new EventChannel(registrar.messenger(),"bluetooth_client_message_received");
        messageReceivedChannel.setStreamHandler(new BluetoothClientStreamHandler("messageReceived"));
        final EventChannel messageSentChannel = new EventChannel(registrar.messenger(),"bluetooth_client_message_sent");
        messageSentChannel.setStreamHandler(new BluetoothClientStreamHandler("messageSent"));
    }


}
