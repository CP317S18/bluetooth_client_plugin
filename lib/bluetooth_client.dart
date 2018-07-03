import 'dart:async';

import 'package:flutter/services.dart';

class BluetoothClient {
  static const MethodChannel _channel =
  const MethodChannel('bluetooth_client');
  static const EventChannel _messageReceivedChannel =
  const EventChannel('bluetooth_client_message_received');
  static const EventChannel _messageSentChannel =
  const EventChannel('bluetooth_client_message_sent');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<bool> initialize() async{
    final bool res = await _channel.invokeMethod("initialize");
    return res;
  }

  static Future<bool> start() async{
    final bool res = await _channel.invokeMethod("start");
    return res;
  }


  static Future<bool> sendBroadcastMessage(Message m) async{
    final bool res = await _channel.invokeMethod("sendBroadcastMessage",Message.mapFromMessage(m));
    return res;
  }

  static Stream<Message> get messageReceivedStream {
    return _messageReceivedChannel.receiveBroadcastStream().map((event) => Message.createMessage(event));
  }

  static Stream<Map<String,dynamic>> get messageSentStream {
    return _messageSentChannel.receiveBroadcastStream().map((event) => event.cast<Map<String,dynamic>>());
  }
}

class Message {
  final String authorUsername;
  final String content;

  static Message createMessage(dynamic event){
    return new Message(event['username'], event['content']);
  }

  static Map<String,String> mapFromMessage(Message m){
    return {
      'content':m.content,
      'username':m.authorUsername
    };
  }

  Message(this.authorUsername,this.content);
}