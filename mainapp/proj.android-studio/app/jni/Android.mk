LOCAL_PATH := $(call my-dir)
LOCAL_SHORT_COMMANDS := true

# The path to the Firebase C++ SDK, in the project's root directory.
# FIREBASE_CPP_SDK_DIR := ../../../firebase_cpp_sdk

ifeq ($(FIREBASE_CPP_SDK_DIR),)
    $(error FIREBASE_CPP_SDK_DIR must specify the Firebase package location.)
endif

STL := $(firstword $(subst _, ,$(APP_STL)))
FIREBASE_LIBRARY_PATH := $(FIREBASE_CPP_SDK_DIR)/libs/android/$(TARGET_ARCH_ABI)/$(STL)

include $(CLEAR_VARS)
LOCAL_MODULE := firebase_app
LOCAL_SRC_FILES := $(FIREBASE_LIBRARY_PATH)/libfirebase_app.a
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/$(FIREBASE_CPP_SDK_DIR)/include
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE:=firebase_analytics
LOCAL_SRC_FILES:=$(FIREBASE_LIBRARY_PATH)/libfirebase_analytics.a
LOCAL_EXPORT_C_INCLUDES:=$(FIREBASE_CPP_SDK_DIR)/include
include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
$(call import-add-path,$(LOCAL_PATH)/../../../cocos2d)
$(call import-add-path,$(LOCAL_PATH)/../../../cocos2d/external)
$(call import-add-path,$(LOCAL_PATH)/../../../cocos2d/cocos)
$(call import-add-path,$(LOCAL_PATH)/../../../cocos2d/cocos/audio/include)
#$(call import-add-path,$(LOCAL_PATH)/../../../cocos2d/cocos/prebuilt-mk)

LOCAL_MODULE := MyGame_shared

LOCAL_MODULE_FILENAME := libMyGame

CLASSES := $(wildcard $(LOCAL_PATH)/../../../Classes/*.cpp)
CLASSES += $(wildcard $(LOCAL_PATH)/../../../Classes/**/*.cpp)
CLASSES += $(wildcard $(LOCAL_PATH)/../../../Classes/**/*.c)
CLASSES += $(wildcard $(LOCAL_PATH)/../../../Classes/**/**/*.cpp)
CLASSES += $(wildcard $(LOCAL_PATH)/../../../Classes/**/**/**/*.cpp)
CLASSES += $(wildcard $(LOCAL_PATH)/../../../Classes/**/**/**/**/*.cpp)
CLASSES += $(wildcard $(LOCAL_PATH)/../../../Classes/**/**/**/**/**/*.cpp)
CLASSES += $(wildcard $(LOCAL_PATH)/../../../Classes/**/**/**/**/**/**/*.cpp)
CLASSES := $(CLASSES:$(LOCAL_PATH)/%=%)

LOCAL_SRC_FILES := hellocpp/main.cpp \
LOCAL_SRC_FILES += $(CLASSES)

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../../Classes

# _COCOS_HEADER_ANDROID_BEGIN
# _COCOS_HEADER_ANDROID_END


LOCAL_STATIC_LIBRARIES := cocos2dx_static
LOCAL_STATIC_LIBRARIES += firebase_analytics
LOCAL_STATIC_LIBRARIES += firebase_app

# _COCOS_LIB_ANDROID_BEGIN
# _COCOS_LIB_ANDROID_END

include $(BUILD_SHARED_LIBRARY)

$(call import-module,.)

# _COCOS_LIB_IMPORT_ANDROID_BEGIN
# _COCOS_LIB_IMPORT_ANDROID_END
