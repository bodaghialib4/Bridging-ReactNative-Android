# React Native Bridging (on Android)

This is a **React Native** application.\
I implemented this application only to learn **bridging** between **native android** codes and **React Native** codes.

This project implemented according to below tutorial:
* [React Native (61) > Docs > GUIDES(ANDROID) > Native Modules\
(React Native Original document)](https://facebook.github.io/react-native/docs/native-modules-android/) 

-----------
react & react-native versions:
```$xslt
react: 16.9.0,
react-native: 0.61.2,
```

Tested on : only **Android**


-----------

#### steps:

 * Implement a **native module** (The Toast Module) that trigger form JS (React Native) codes.
 * Implement a native **callback** function and called from JS part.
 * Implement a native **promise** function and called from JS part.
 * Implement a native **event** sender & trigger and triggered event from JS part and then listen and handle event in JS part (JS triggered native event sender --> native code send event to JS --> JS lister and handle sent event ).
 * working with **startActivityForResult** in native side (an image picker implemented in native side by using startActivityForResult and called from JS part by a promise function).
 * add listener for listening to **LifeCycle Events** in native side.





