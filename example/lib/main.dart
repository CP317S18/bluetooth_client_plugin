import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:bluetooth_client/bluetooth_client.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  List<String> messages = [];

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    await BluetoothClient.initialize();
    await BluetoothClient.start();
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await BluetoothClient.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    final stream = BluetoothClient.messageReceivedStream;
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });

    stream.listen((event) {
      print(event);
      setState(() {
        messages.add(event.content);
      });
    });
  }

  _sendMessage(){
    BluetoothClient.sendBroadcastMessage(new Message("User A", "Hello World"));
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Plugin example app'),
        ),
        body: new Center(
          child: new Text(messages.join('\n')),
        ),
        floatingActionButton: new FloatingActionButton(onPressed: _sendMessage),
      ),
    );
  }
}
