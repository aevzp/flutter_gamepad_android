package com.example.flutter_gamepad_android.controller

import android.view.InputDevice
import android.view.InputEvent
import android.view.KeyEvent
import android.view.MotionEvent
import kotlin.math.abs

object ControllerUtils {
    const val UP = 0
    const val LEFT = 1
    const val RIGHT = 2
    const val DOWN = 3
    const val CENTER = 4

    fun isDpadDevice(inputEvent: InputEvent) : Boolean {
        return (inputEvent.source and InputDevice.SOURCE_DPAD) != InputDevice.SOURCE_DPAD
    }

    fun isJoystick(inputEvent: MotionEvent): Boolean{
        return ((inputEvent.source and InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK) && inputEvent.action == MotionEvent.ACTION_MOVE
    }

    fun getDpadDirection(inputEvent: InputEvent): Int{
        if(!isDpadDevice(inputEvent)) return -1
        return when(inputEvent){
            is MotionEvent -> {
                var direction = -1
                val xAxis = inputEvent.getAxisValue(MotionEvent.AXIS_HAT_X)
                val yAxis = inputEvent.getAxisValue(MotionEvent.AXIS_HAT_Y)
                when {
                    xAxis == -1f -> {
                        direction = LEFT
                    }
                    xAxis == 1f -> {
                        direction = RIGHT
                    }
                    yAxis == -1f -> {
                        direction = UP
                    }
                    yAxis == 1f -> {
                        direction = DOWN
                    }
                }
                direction
            }
            is KeyEvent -> {
                val direction = when (inputEvent.keyCode) {
                    KeyEvent.KEYCODE_DPAD_LEFT -> {
                        LEFT
                    }
                    KeyEvent.KEYCODE_DPAD_RIGHT -> {
                        RIGHT
                    }
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        UP
                    }
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        DOWN
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER -> {
                        CENTER
                    }
                    else -> {
                        -1
                    }
                }
                direction
            }
            else -> -1
        }
    }

    fun getCenteredAxis(event: MotionEvent, axis: Int): Float {
        val range = event.device.getMotionRange(axis, event.source)
        if(range != null){
            val flat = range.flat
            val value = event.getAxisValue(axis)
            if(abs(value) > flat){
                return value
            }
        }
        return 0f
    }
}
