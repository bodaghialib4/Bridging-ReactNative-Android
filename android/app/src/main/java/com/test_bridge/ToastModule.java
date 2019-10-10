// ToastModule.java

package com.test_bridge;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.IllegalViewOperationException;

import java.util.Map;
import java.util.HashMap;

import javax.annotation.Nullable;

public class ToastModule extends ReactContextBaseJavaModule {
  private static ReactApplicationContext reactContext;

  //for show
  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  //for promise
  private static final String ERROR = "error";

  //for startActivityForResult
  private static final int IMAGE_PICKER_REQUEST = 1010;
  private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
  private static final String E_PICKER_CANCELLED = "E_PICKER_CANCELLED";
  private static final String E_FAILED_TO_SHOW_PICKER = "E_FAILED_TO_SHOW_PICKER";
  private static final String E_NO_IMAGE_DATA_FOUND = "E_NO_IMAGE_DATA_FOUND";
  private Promise mPickerPromise;

  ToastModule(ReactApplicationContext context) {
    super(context);

    //for startActivityForResult
    // Add the listener for `onActivityResult`
    context.addActivityEventListener(mActivityEventListener);
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

  //for event
  private void sendEvent(ReactContext reactContext,
                         String eventName,
                         @Nullable WritableMap params) {
    reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, params);
  }

  //for startActivityForResult
  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
      if (requestCode == IMAGE_PICKER_REQUEST) {
        if (mPickerPromise != null) {
          if (resultCode == Activity.RESULT_CANCELED) {
            mPickerPromise.reject(E_PICKER_CANCELLED, "Image picker was cancelled");
          } else if (resultCode == Activity.RESULT_OK) {
            Uri uri = intent.getData();

            if (uri == null) {
              mPickerPromise.reject(E_NO_IMAGE_DATA_FOUND, "No image data found");
            } else {
              mPickerPromise.resolve(uri.toString());
            }
          }

          mPickerPromise = null;
        }
      }
    }
  };


  // bridge method were started from here
  // ************************************


  // input arguments mapping
  /*
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
      paintedMessage = paintedMessage.replaceAll(" ", " ** ");
      paintedMessage = paintedMessage.replaceAll("_", " * ");
      paintedMessage = "***  " + paintedMessage + "  ****";
      successCallback.invoke(paintedMessage);
    } catch (IllegalViewOperationException e) {
      errorCallback.invoke(e.getMessage());
    }
  }

  @ReactMethod
  public void paintMessagePromise(
          String message,
          Promise promise) {
    try {
      WritableMap map = Arguments.createMap();
      String paintedMessage = message;
      paintedMessage = paintedMessage.replaceAll(" ", " ** ");
      paintedMessage = paintedMessage.replaceAll("_", " * ");
      paintedMessage = "***  " + paintedMessage + "  ****";
      map.putString("paintedMessage", paintedMessage);
      promise.resolve(map);
    } catch (IllegalViewOperationException e) {
      promise.reject(ERROR, e);
    }
  }

  @ReactMethod
  public void paintMessageEvent(String message) {
    WritableMap params = Arguments.createMap();
    try {
      String paintedMessage = message;
      paintedMessage = paintedMessage.replaceAll(" ", " ** ");
      paintedMessage = paintedMessage.replaceAll("_", " * ");
      paintedMessage = "***  " + paintedMessage + "  ****";
      params.putString("paintedMessage", paintedMessage);
      sendEvent(reactContext, "MessagePaintingEvent", params);

    } catch (IllegalViewOperationException e) {
      params.putBoolean("error", true);
      sendEvent(reactContext, "MessagePaintingEvent", params);
    }
  }

  @ReactMethod
  public void pickImage(final Promise promise) {
    Activity currentActivity = getCurrentActivity();

    if (currentActivity == null) {
      promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
      return;
    }

    // Store the promise to resolve/reject when picker returns data
    mPickerPromise = promise;

    try {
      final Intent galleryIntent = new Intent(Intent.ACTION_PICK);

      galleryIntent.setType("image/*");

      final Intent chooserIntent = Intent.createChooser(galleryIntent, "Pick an image");

      currentActivity.startActivityForResult(chooserIntent, IMAGE_PICKER_REQUEST);
    } catch (Exception e) {
      mPickerPromise.reject(E_FAILED_TO_SHOW_PICKER, e);
      mPickerPromise = null;
    }
  }

}
