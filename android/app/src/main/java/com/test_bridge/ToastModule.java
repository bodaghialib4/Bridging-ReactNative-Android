// ToastModule.java

package com.test_bridge;

import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.IllegalViewOperationException;

import java.util.Map;
import java.util.HashMap;

public class ToastModule extends ReactContextBaseJavaModule {
  private static ReactApplicationContext reactContext;

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  ToastModule(ReactApplicationContext context) {
    super(context);
    reactContext = context;
  }

  @Override
  public String getName() {
    return "ToastExample";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }


  /* input arguments mapping
  Boolean -> Bool
  Integer -> Number
  Double -> Number
  Float -> Number
  String -> String
  Callback -> function
  ReadableMap -> Object
  ReadableArray -> Array
  */

  @ReactMethod
  public void show(String message, int duration) {
    Toast.makeText(getReactApplicationContext(), message, duration).show();
  }

  @ReactMethod
  public void paintMessageCallback(
          String message,
          Callback errorCallback,
          Callback successCallback) {
    try {
      String paintedMessage = message;
      paintedMessage = paintedMessage.replaceAll(" "," ** ");
      paintedMessage = paintedMessage.replaceAll("_"," * ");
      paintedMessage = "***  " + paintedMessage+"  ****";
      successCallback.invoke(paintedMessage);
    } catch (IllegalViewOperationException e) {
      errorCallback.invoke(e.getMessage());
    }
  }

  private static final String ERROR = "error";
  @ReactMethod
  public void paintMessagePromise(
          String message,
          Promise promise) {
    try {
      WritableMap map = Arguments.createMap();
      String paintedMessage = message;
      paintedMessage = paintedMessage.replaceAll(" "," ** ");
      paintedMessage = paintedMessage.replaceAll("_"," * ");
      paintedMessage = "***  " + paintedMessage+"  ****";
      map.putString("paintedMessage", paintedMessage);
      promise.resolve(map);
    } catch (IllegalViewOperationException e) {
      promise.reject(ERROR, e);
    }
  }

}
