package com.shout.bluetoothclient;

import io.flutter.plugin.common.EventChannel;

public class BluetoothClientStreamHandler implements EventChannel.StreamHandler  {

    BluetoothClient client;
    String streamName;

    public BluetoothClientStreamHandler(String streamName){
        client = BluetoothClient.getInstance();
        this.streamName = streamName;
    }


    @Override
    public void onListen(Object o, EventChannel.EventSink eventSink) {
        client.setSink(streamName,eventSink);
    }

    @Override
    public void onCancel(Object o) {
        client.setSink(streamName,null);
    }
}
