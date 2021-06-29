import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_gamepad_android/eventsTypes.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Gamepad-Android',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const eventChannel =
      EventChannel('com.example.flutter_gamepad_android/gamepad_channel');
  late StreamSubscription _eventChannel;

  void initState() {
    _eventChannel = eventChannel.receiveBroadcastStream().listen((event) {
      final eventParameters = <String, dynamic>{};

      if (event != null && event is String) {
        event.split(',~').forEach((e) {
          var createPairList = e.split('=');

          eventParameters[createPairList[0]] = createPairList[1];
        });
      }

      if (eventParameters[EventTypes.androidType] == EventTypes.button) {
        print('BUTTON: $eventParameters');
      } else if (eventParameters[EventTypes.androidType] == EventTypes.axis) {
        print('AXIS: $eventParameters');
      } else if (eventParameters[EventTypes.androidType] == EventTypes.dpad) {
        print('DPAD: $eventParameters');
      }
    });

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Container(),
    );
  }

  @override
  void dispose() {
    super.dispose();
    _eventChannel.cancel();
  }
}
