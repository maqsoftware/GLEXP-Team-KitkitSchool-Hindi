## Pehla School

Pehla School consists of 2 apps as following:

1. Pehla School
* Main learning app with tailored curriculum.

2. Pehla School Library
* A collection of videos and books for children to enjoy.


**Both the apps are developed using cocos2d-x engine**

## Build Environment

Android Studio and Android NDK are necessary to build the project.
Make sure to use the specified versions mentioned below, to avoid errors. 
Android Studio will also ask to install the relevant SDKs and build tools that are missing - please install them along the way. 


1. Download and install [Android Studio](https://developer.android.com/Studio/).

2. Download Android [NDK 14](https://dl.google.com/android/repository/android-ndk-r14b-windows-x86_64.zip).
3. Unzip the NDK file and set its path in the pehlaschool and pehlaschoollibrary code.

## Copy Resource files

Download correct version of file from [release assets](https://github.com/XPRIZE/GLEXP-Team-KitkitSchool/releases/download/v1.0/cocos2d.zip).

* to build pehlaschool, place the 3rd party files in the following folders:
  * ROOT/pehlaschool/cocos2d
  * ROOT/pehlaschoollibrary/cocos2d

## Build the APKs

Build with Android Studio

- set the appropriate 'Build Variant' 
   - pehlaschool: hindiDebug/hindiRelease
   - pehlaschoollibrary: englishDebug
- build variant of pehlaschoollogger will be set automatically to release, if not, then set it manually
- in the Build menu click on Build APK option

The resulting debug and release APKs will be generated in _app/build/outputs/apk/_ and _app/_ folders respectively.

**Note: Please make sure there is enough storage available on the device**

## Install the APKs

1. Download the OBB file and follow the instructions which are present in the latest [GitHub release](https://github.com/maqsoftware/Pehla-School/releases/).