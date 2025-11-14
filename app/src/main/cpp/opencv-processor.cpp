#include "include/edge-detection.h"
#include <vector>

void processFrameWithOpenCV(cv::Mat& input, cv::Mat& output) {
    cv::Mat gray, edges;

    if (input.channels() == 3) {
        cv::cvtColor(input, gray, cv::COLOR_RGB2GRAY);
    } else {
        gray = input.clone();
    }

    cv::GaussianBlur(gray, gray, cv::Size(5, 5), 1.5);
    cv::Canny(gray, edges, 50, 150);
    cv::cvtColor(edges, output, cv::COLOR_GRAY2RGB);
}

JNIEXPORT void JNICALL
Java_com_example_edgedetection_MainActivity_processFrame(
        JNIEnv *env,
        jobject thiz,
        jbyteArray image_data,
        jint width,
        jint height,
        jintArray output_data
) {
    jbyte* data = env->GetByteArrayElements(image_data, nullptr);
    jint* out_data = env->GetIntArrayElements(output_data, nullptr);

    if (data == nullptr || out_data == nullptr) {
        LOGE("Failed to get array elements");
        return;
    }

    cv::Mat input(height, width, CV_8UC1, data);
    cv::Mat output;

    processFrameWithOpenCV(input, output);

    if (!output.empty() && output.isContinuous()) {
        memcpy(out_data, output.data, output.total() * output.elemSize());
    }

    env->ReleaseByteArrayElements(image_data, data, JNI_ABORT);
    env->ReleaseIntArrayElements(output_data, out_data, 0);
}