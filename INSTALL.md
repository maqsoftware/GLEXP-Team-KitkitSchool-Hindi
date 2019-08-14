## Installation steps for all the apps
1. Download and install [Android Studio](https://developer.android.com/studio/).
2. Download Android [NDK 14](https://dl.google.com/android/repository/android-ndk-r14b-windows-x86_64.zip).
3. Download the [_cocos2d_](https://github.com/XPRIZE/GLEXP-Team-KitkitSchool/releases/download/v1.0/cocos2d.zip) folder of the project.
4. Replace _cocos2d_ folder present in both the _pehlaschool_ and _pehlaschoollibrary_ project with the downloaded _cocos2d_ folder.
5. Open _proj.android-studio_ folder in Android Studio and select the Build option under Build menu.
6. Android Studio might show a prompt to install various dependencies related to Android SDK and Build Tool during the build process. Make sure to install all of them.
7. Do not update _Gradle_ version used in the project, in case you are prompted to update it.
8. Download the OBB file from the latest GitHub release and follow the instructions provided there.
9. To change the app locale just select the desired language from the build variant and then, build the project.

#### Note:
1. There is no need to install _cocos_ application separately for building the project.
2. If any project throws APK signing error during the build process, then either comment the signing configuration from the app's gradle file or create your own _keystore_ file with the help of the steps mentioned [here](https://developer.android.com/studio/publish/app-signing#generate-key).

## FAQs
1. How to fix missing google-services.json file error?
	> Put `google-services.json` file in [app](https://github.com/maqsoftware/Pehla-School/tree/newmaster/pehlaschool/proj.android-studio/app) folder. The JSON file is present in the [Firebase console](https://docs.kii.com/en/samples/push-notifications/push-notifications-android-fcm/create-project/) after the app project has been created.

1. There is a cross mark on the app cofiguration button. How do I fix that?

	> The default/main activity of the application is not set for the project. Either go to _app/build/outputs/apk_ folder and install the APK which is generated right after the build or, set the start activity in the app configuration settings of the project.

2. How to enable the _Build variant_ option in the _Build_ menu?
	> The _Build variant_ option is enabled only when the module's gradle file is opened.

3. The application throws _keystore_ file not found error. How do I fix this?

	>_keystore_ file is used to digitally sign an Android application and hence, it is not provided with the project. One must create their own _keystore_ file using the [Android KeyTool](https://developer.android.com/studio/publish/app-signing) which comes with the Android Studio itself in order to sign the APK.

4. How to fix the the pehlaschool application which throws a run time error related to the package name _com.maq.pehlaschool_?
	> This error is caused by incorrect build variant setting for the application. Kindly, refer to the below table which has the build variant settings.
	> 
	> |App name| Build variant|
	> |--------|--------------|
	> |PehlaSchool|hindiDebug/hindiRelease|
	> |pehlaschoollogger|release|
	> |libcocos2dx|debug|
	> |models|debug|

5. How to fix the error for NDK not found?

	> 1. Unzip the NDK folder.
	> 2. Rename the unzipped folder to _ndk-bundle_ and place it in Android SDK's root directory.
	> 3. Ensure that the _ndk-bundle_ folder contains its children files and folders.