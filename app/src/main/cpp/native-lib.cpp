#include <jni.h>
#include <string>
#include "include/edge-detection.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_edgedetection_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject thiz) {
    std::string hello = "Edge Detection Native Library Loaded";
    return env->NewStringUTF(hello.c_str());
}