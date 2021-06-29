package com.example.flutter_gamepad_android.controller

import io.flutter.plugin.common.EventChannel

class EventManager {
    companion object {
        private const val EVENT_DPAD = "androidType=%s,~direction=%d"
    }

    private var eventSink: EventChannel.EventSink? = null

    fun setEventSink(eventSink: EventChannel.EventSink?){
        this.eventSink = eventSink
    }

    fun sendDpadEvent(direction: Int){
        eventSink?.success("androidType=${ControllerKeysData.DPAD},~direction=$direction")
    }

    fun sendAxisEvent(source: ControllerKeysData, xValue: Float = 0f, yValue: Float = 0f){
        eventSink?.success("androidType=${ControllerKeysData.AXIS},~sourceInput=$source,~x=$xValue,~y=$yValue")
    }

    fun sendButtonEvent(action: String, code: Int){
        eventSink?.success("androidType=${ControllerKeysData.BUTTON},~keyAction=$action,~keyCode=$code")
    }
}
