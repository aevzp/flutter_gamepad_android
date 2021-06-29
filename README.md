# flutter_gamepad_android

Flutter gamepad listener for android

## Getting Started

We had a problem using Flutter with a gamepad on Android devices. The problem was how to listen to events from the gamepad sticks after upgrade to 1.12 and deprecated FlutterView. 
This solution must resolve this issue.

The main idea it's implement method: `onGenericMotionEvent`, `onKeyDown` and `onKeyUp` to the class `MainActivity` on the following path:
``YOUR_APP_NAME/android/app/src/main/kotlin/com/example/flutter_gamepad_android/MainActivity.kt``
(your path will be different, but you must find `MainActivity.kt` file) and then just adapt code from this library to your project.

Next.
`/flutter_gamepad_android/lib/main.dart` there you'll see some example how exaclly you can listen all gamepad events in app:

````
  // Start your widget
  
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
  
  // rest of code
````

Using this snippet you'll get console messages like this:
````
BUTTON: {androidType: BUTTON, keyAction: ACTION_DOWN, keyCode: 99}
DPAD: {androidType: DPAD, direction: 3}
AXIS: {androidType: AXIS, sourceInput: STICK_LEFT, x: 0.21568632, y: 0.4039216}
AXIS: {androidType: AXIS, sourceInput: STICK_RIGHT, x: 0.9058825, y: -0.25490195}
AXIS: {androidType: AXIS, sourceInput: AXIS_LTRIGGER, x: 0.83921576, y: 0.0}
AXIS: {androidType: AXIS, sourceInput: AXIS_RTRIGGER, x: 0.82745105, y: 0.0}
````
...parse it as you need.

Enjoy!)

PS. Thanks for the help: [Egorman7](https://github.com/Egorman7)
