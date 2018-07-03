import 'package:flutter/material.dart';
import 'dart:async';
import 'package:bluetooth_client/bluetooth_client.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<Message> messages = [new Message("TestUser", "Hello World")];
  TextEditingController messageInputController;
  FocusNode messageInputFocusNode;
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    await BluetoothClient.initialize("7724652a-7e71-4a32-81ca-6be0a9e82d3b");
    await BluetoothClient.start();

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    final stream = BluetoothClient.messageReceivedStream;
    if (!mounted) return;

    stream.listen((event) {
      print(event);
      setState(() {
        messages.add(event);
      });
    });
    messageInputController = new TextEditingController();
    messageInputFocusNode = new FocusNode();
  }

  _sendMessage(Message m) {
    BluetoothClient.sendBroadcastMessage(m);
  }


  Widget _messageInputWidget(BuildContext context) {
    return new Container(
        margin: const EdgeInsets.symmetric(horizontal: 8.0),
        child: new Row(
          children: <Widget>[
            new Flexible(
              child: new TextField(
                decoration: new InputDecoration(
                    hintText: "Enter your message"),
                controller: messageInputController,
                onSubmitted: (text){
                  FocusScope.of(context).requestFocus(messageInputFocusNode);
                  if(text.length > 0){
                    messageInputController.clear();
                    Message chatMessage = new Message("username",text);
                    _sendMessage(chatMessage);
                    setState(() {
                      //used to rebuild our widget
                      messages.insert(0, chatMessage);
                    });
                  }

                },
                focusNode: messageInputFocusNode,
              ),
            )
          ],
        ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: "Flutter Chat App",
      home: new Scaffold(
          appBar: new AppBar(
            title: new Text("Chat App"),
          ),
          body: new Column(
            children: <Widget>[
              new Flexible(
                child: new ListView.builder(
                  padding: new EdgeInsets.all(8.0),
                  reverse: true,
                  itemBuilder:(_,int index) =>
                  new Container(
                    child: new Text(messages[index].authorUsername+': '+messages[index].content),
                  )
                  ,
                  itemCount: messages.length,
                ),
              ),
              new Divider(height: 1.0,),
              new Container(
                decoration: new BoxDecoration(
                  color: Theme.of(context).cardColor,
                ),
                child: _messageInputWidget(context),
              )
            ],
          )
      ),
    );
  }
}
