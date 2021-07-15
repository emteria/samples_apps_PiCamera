LOCAL_PATH := $(call my-dir)

##################################################

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional
LOCAL_CERTIFICATE := platform
LOCAL_PRIVATE_PLATFORM_APIS := true
LOCAL_PRIVILEGED_MODULE := true

LOCAL_PACKAGE_NAME := PiCamera
LOCAL_SRC_FILES := $(call all-java-files-under, app/src)
LOCAL_MANIFEST_FILE := app/src/main/AndroidManifest.xml

LOCAL_RESOURCE_DIR := $(addprefix $(LOCAL_PATH)/, app/src/main/res)

LOCAL_PROGUARD_ENABLED := full
LOCAL_PROGUARD_FLAG_FILES := app/proguard-rules.pro

LOCAL_JNI_SHARED_LIBRARIES := libnative-lib
LOCAL_JAVA_LIBRARIES := emteria.api

include $(BUILD_PACKAGE)

##################################################

include $(CLEAR_VARS)

LOCAL_MODULE := libnative-lib
LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := app/src/main/cpp/native-lib.cpp
LOCAL_CFLAGS := -Wno-unused-parameter
LOCAL_SHARED_LIBRARIES := liblog 

include $(BUILD_SHARED_LIBRARY)

########################################
