#Kii Android Push Plugin for Unity3D
This project is native plugin to receive push notification for the Unity3D.  
By adding code to this project, you can customize the behavior when it receives a push notification.  


## Overview about the push notification mechanism

      +------------------------+
      |                        |
      | Google Cloud Messaging |
      |                        |
      +------------------------+
                  |
                  | 1. Push Notification from Google server
                  |
    +-------------+---------------+---------------------------------------------------------------+
    |             |               |                                                               |
    |             V               |                                                               |
    | +------------------------+  |                        +------------------+                   |
    | |                        |  | 2. UnitySendMessage()  |                  |                   |
    | | androidpushplugin.jar  | -+----------------------> | KiiPushPlugin.cs |                   |
    | |     (This project)     |  |                        |                  |                   |
    | +------------------------+  |                        +------------------+                   |
    |                             |                                 |                             |
    |                             |                                 |                             |
    |                             |                                 | 3. OnPushMessageReceived()  |
    |                             |                                 |                             |
    |                             |                                 V                             |
    |                             |                        +------------------+                   |             
    |                             |                        |                  |                   |
    |                             |                        | YourUnityCode.cs |                   |
    |                             |                        |                  |                   |
    |                             |                        +------------------+                   |
    |                             |                                                               |
    +----[Native Plugin Layer]----+-----------------------[Unity Layer]---------------------------+




1. Push notification is received in the Native Plugin Layer from GCM
1. Native Plugin notifies KiiPushPlugin.cs using [UnitySendMessage](http://docs.unity3d.com/Manual/PluginsForAndroid.html "UnitySendMessage").  
1. KiiPushPlugin.cs notifies your code via OnPushMessageReceived event.

## How to integrate to your Unity project
1. Builds this project. (classes.jar will be generated in bin directory.)  
`$ ant clean release`

1. Renames classes.jar to androidpushplugin.jar
1. Overwrites Assets/Plugins/Android/androidpushplugin.jar by new androidpushplugin.jar.

