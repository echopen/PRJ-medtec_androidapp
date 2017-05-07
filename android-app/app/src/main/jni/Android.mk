LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := com.echopen.asso.echopen

LOCAL_MODULE := fftw3
LOCAL_SRC_FILES := /Users/mehdibenchoufi/forked_project/android-app/app/src/main/jniLibs/fftw/android/armv6/lib/libfftw3.a
LOCAL_EXPORT_C_INCLUDES := /Users/mehdibenchoufi/forked_project/android-app/app/src/main/jniLibs/fftw/android/armv6/include/
include $(PREBUILT_STATIC_LIBRARY)

LOCAL_MODULE    := fftw_jni
LOCAL_C_INCLUDES := /Users/mehdibenchoufi/forked_project/android-app/app/src/main/jniLibs/fftw/android/androideabi.4.9/include
LOCAL_SRC_FILES := /Users/mehdibenchoufi/forked_project/android-app/app/src/main/jniLibs/fftw_jni.c
LOCAL_STATIC_LIBRARIES := fftw3
LOCAL_LDLIBS    := -llog -lz -lm /Users/mehdibenchoufi/forked_project/android-app/app/src/main/jniLibs/fftw/android/androideabi.4.9/libfftw3.a
include $(BUILD_SHARED_LIBRARY)



