#ifndef EDGE_DETECTION_H
#define EDGE_DETECTION_H

#include <jni.h>
#include <android/log.h>
#include <android/bitmap.h>
#include <opencv2/opencv.hpp>
#include <GLES2/gl2.h>

#define LOG_TAG "EdgeDetectionNative"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL
Java_com_example_edgedetection_MainActivity_processFrame(
        JNIEnv *env,
        jobject thiz,
        jbyteArray image_data,
        jint width,
        jint height,
        jintArray output_data
);

JNIEXPORT jstring JNICALL
Java_com_example_edgedetection_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject thiz
);

#ifdef __cplusplus
}
#endif

#endif