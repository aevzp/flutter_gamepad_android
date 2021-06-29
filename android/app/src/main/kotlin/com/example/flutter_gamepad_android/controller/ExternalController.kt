package com.example.flutter_gamepad_android.controller

import android.view.KeyEvent
import android.view.MotionEvent
import android.view.InputDevice
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel

class ExternalController {
    companion object {
        private const val CHANNEL = "com.example.flutter_gamepad_android/gamepad_channel"
    }

    private val eventManager = EventManager()

    private var isRTriggerZero = true
    private var isLTriggerZero = true
    private var isStickLeftZero = true
    private var isStickRightZero = true

    fun initialize(flutterEngine: FlutterEngine){
        EventChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                eventManager.setEventSink(events)
            }

            override fun onCancel(arguments: Any?) {
                eventManager.setEventSink(null)
            }
        })
    }

    fun handleOnGenericMotionEvent(event: MotionEvent) : Boolean {
        if(ControllerUtils.isDpadDevice(event)){
            eventManager.sendDpadEvent(ControllerUtils.getDpadDirection(event))
        }
        if(ControllerUtils.isJoystick(event)){
            processJoystickInput(event)
            return true
        }
        return false
    }

    private fun processJoystickInput(event: MotionEvent){
        val axisRTrigger = event.getAxisValue(MotionEvent.AXIS_RTRIGGER)
        val axisLTrigger = event.getAxisValue(MotionEvent.AXIS_LTRIGGER)
        // RT
        if(axisRTrigger != 0f){
            isRTriggerZero = false
            eventManager.sendAxisEvent(ControllerKeysData.AXIS_RTRIGGER, axisRTrigger)
        } else if(!isRTriggerZero && axisRTrigger == 0f){
            isRTriggerZero = true
            eventManager.sendAxisEvent(ControllerKeysData.AXIS_RTRIGGER)
        }
        // LT
        if(axisLTrigger != 0f){
            isLTriggerZero = false
            eventManager.sendAxisEvent(ControllerKeysData.AXIS_LTRIGGER, axisLTrigger)
        } else if(!isLTriggerZero && axisLTrigger == 0f){
            isLTriggerZero = true
            eventManager.sendAxisEvent(ControllerKeysData.AXIS_LTRIGGER)
        }
        // stick (left)
        val x = ControllerUtils.getCenteredAxis(event, MotionEvent.AXIS_X)
        val y = ControllerUtils.getCenteredAxis(event, MotionEvent.AXIS_Y)
        if((x != 0f) or (y != 0f)){
            isStickLeftZero = false
            eventManager.sendAxisEvent(ControllerKeysData.STICK_LEFT, x, y)
        } else if(!isStickLeftZero && x == 0f && y == 0f){
            isStickLeftZero = true
            eventManager.sendAxisEvent(ControllerKeysData.STICK_LEFT)
        }
        // stick (right)
        val z = ControllerUtils.getCenteredAxis(event, MotionEvent.AXIS_Z)
        val rz = ControllerUtils.getCenteredAxis(event, MotionEvent.AXIS_RZ)
        if((z != 0f) or (rz != 0f)){
            isStickRightZero = false
            eventManager.sendAxisEvent(ControllerKeysData.STICK_RIGHT, z, rz)
        } else if(!isStickRightZero && z == 0f && rz == 0f){
            isStickRightZero = true
            eventManager.sendAxisEvent(ControllerKeysData.STICK_RIGHT)
        }
    }

    fun handleKeyEvent(keyCode: Int, event: KeyEvent, action: String): Boolean {
        eventManager.sendButtonEvent(action, keyCode)
        val keyCodeBool = (keyCode == KeyEvent.KEYCODE_BACK) or (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) or (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
        val keyboardTypeBool = event.device.keyboardType == InputDevice.KEYBOARD_TYPE_ALPHABETIC
        if (keyboardTypeBool && keyCodeBool && event.device.isVirtual) {
            return false
        }
        return true
    }
}
