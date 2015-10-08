LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_SRC_FILES := fast_int.c
LOCAL_MODULE    := com.echopen.asso.echopen
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)
