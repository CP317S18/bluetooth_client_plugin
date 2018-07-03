package com.shout.bluetoothclient;

import com.bridgefy.sdk.client.Bridgefy;
import com.bridgefy.sdk.client.Message;
import com.bridgefy.sdk.client.MessageListener;
import com.bridgefy.sdk.framework.exceptions.MessageException;

import java.util.HashMap;
import java.util.UUID;

import io.flutter.plugin.common.EventChannel;

public class BluetoothClient extends MessageListener {

    private static final BluetoothClient INSTANCE = new BluetoothClient();
    private HashMap<String,EventChannel.EventSink> sinks;

    private BluetoothClient(){
        sinks = new HashMap<>();
    }

    @Override
    public void onMessageReceived(Message message) {
        sendToSink("messageRecieved",message.getContent());
    }

    @Override
    public void onMessageSent(Message message) {
        sendToSink("messageSent",message.getContent());
    }

    @Override
    public void onMessageDataProgress(UUID message, long progress, long fullSize) {
        super.onMessageDataProgress(message, progress, fullSize);
    }

    @Override
    public void onMessageReceivedException(String sender, MessageException e) {
        super.onMessageReceivedException(sender, e);
    }

    @Override
    public void onMessageFailed(Message message, MessageException e) {
        super.onMessageFailed(message, e);
    }

    @Override
    public void onBroadcastMessageReceived(Message message) {
        sendToSink("messageRecieved",message.getContent());
    }

    private void sendToSink(String name, Object value){
        EventChannel.EventSink sink = sinks.get(name);
        if(sink != null){
            sink.success(value);
        }
    }

    public void setSink(String name, EventChannel.EventSink sink){
        sinks.put(name,sink);
    }

    public static BluetoothClient getInstance(){
        return INSTANCE;
    }







}
