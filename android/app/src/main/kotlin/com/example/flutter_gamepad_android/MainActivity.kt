package com.example.flutter_gamepad_android

import android.view.KeyEvent
import android.view.MotionEvent
import com.example.flutter_gamepad_android.controller.ExternalController
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity: FlutterActivity() {
    private val externalController = ExternalController()

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        externalController.initialize(flutterEngine)
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        return externalController.handleOnGenericMotionEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val handle = externalController.handleKeyEvent(keyCode, event, "ACTION_DOWN")
        return if(handle) handle else super.onKeyDown(keyCode, event)
    }
    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        val handle = externalController.handleKeyEvent(keyCode, event, "ACTION_UP")
        return if(handle) handle else super.onKeyUp(keyCode, event)
    }
}
