package com.shout.bluetoothclient;

import android.content.Context;

import com.bridgefy.sdk.client.Bridgefy;
import com.bridgefy.sdk.client.BridgefyClient;
import com.bridgefy.sdk.client.Device;
import com.bridgefy.sdk.client.RegistrationListener;
import com.bridgefy.sdk.client.Session;
import com.bridgefy.sdk.client.StateListener;

import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class BluetoothClientMethodCallHandler implements MethodChannel.MethodCallHandler {

    private BluetoothClient bluetoothClient;
    private BridgefyClient client;
    private Context c;
    public BluetoothClientMethodCallHandler(Context c){
        bluetoothClient = BluetoothClient.getInstance();
        this.c = c;
    }


    @Override
    public void onMethodCall(MethodCall call, final MethodChannel.Result result) {
        switch (call.method){
            case "getPlatformVersion":
                result.success("Android " + android.os.Build.VERSION.RELEASE);
                break;
            case "initialize":
                Bridgefy.initialize(c, "7724652a-7e71-4a32-81ca-6be0a9e82d3b", new RegistrationListener() {
                    @Override
                    public void onRegistrationSuccessful(BridgefyClient bridgefyClient) {
                        result.success(true);
                        client = bridgefyClient;
                    }

                    @Override
                    public void onRegistrationFailed(int errorCode, String message) {
                        result.error(String.valueOf(errorCode),message,null);
                    }
                });
                break;
            case "sendBroadcastMessage":
                if(call.hasArgument("content") && call.hasArgument("username")){
                    HashMap<String,Object> data = new HashMap<>();
                    data.put("content",call.argument("content"));
                    data.put("username",call.argument("username"));
                    Bridgefy.sendBroadcastMessage(data);
                    result.success(true);
                }else{
                    result.error("1","Missing argument",null);
                }
                break;
            case "start":
                if(client == null){
                    result.error("1","Bridgefy needs to be initialized",null);
                }else{
                    Bridgefy.start(bluetoothClient, new StateListener() {
                        @Override
                        public void onStarted() {
                            result.success(true);
                        }

                        @Override
                        public void onStartError(String message, int errorCode) {
                            result.error(String.valueOf(errorCode),message,null);
                        }

                        @Override
                        public void onStopped() {
                        }

                        @Override
                        public void onDeviceConnected(Device device, Session session) {
                        }

                        @Override
                        public void onDeviceLost(Device device) {
                        }
                    });
                }
                break;
            default:
                result.notImplemented();
        }
    }
}
