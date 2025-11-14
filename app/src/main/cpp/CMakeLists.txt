cmake_minimum_required(VERSION 3.18.1)

project("edgedetection")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Set OpenCV paths - UPDATE THIS PATH TO YOUR OPENCV LOCATION
set(OPENCV_ROOT "C:/Users/murug/OneDrive/Desktop/Noth/OpenCV-android-sdk/sdk")
set(OPENCV_INCLUDE_DIR "${OPENCV_ROOT}/native/jni/include")
set(OPENCV_LIB_DIR "${OPENCV_ROOT}/native/libs/${ANDROID_ABI}")

# Find OpenCV libraries manually
find_library(OPENCV_CORE_LIB
        NAMES opencv_core4 opencv_core
        PATHS ${OPENCV_LIB_DIR}
        NO_DEFAULT_PATH
)

find_library(OPENCV_IMGPROC_LIB
        NAMES opencv_imgproc4 opencv_imgproc
        PATHS ${OPENCV_LIB_DIR}
        NO_DEFAULT_PATH
)

find_library(OPENCV_IMGCODEC_LIB
        NAMES opencv_imgcodecs4 opencv_imgcodecs
        PATHS ${OPENCV_LIB_DIR}
        NO_DEFAULT_PATH
)

# Add native source files
add_library(
        edgedetection
        SHARED
        native-lib.cpp
        opencv-processor.cpp
        gl-renderer.cpp
)

# Include directories
include_directories(
        ${OPENCV_INCLUDE_DIR}
        ${CMAKE_CURRENT_SOURCE_DIR}/include
)

# Check if OpenCV libraries were found
if(NOT OPENCV_CORE_LIB)
    message(WARNING "OpenCV core library not found at ${OPENCV_LIB_DIR}")
endif()

if(NOT OPENCV_IMGPROC_LIB)
    message(WARNING "OpenCV imgproc library not found at ${OPENCV_LIB_DIR}")
endif()

# Link libraries
target_link_libraries(
        edgedetection
        android
        log
        EGL
        GLESv2
        jnigraphics
)

# Only link OpenCV libraries if they were found
if(OPENCV_CORE_LIB)
    target_link_libraries(edgedetection ${OPENCV_CORE_LIB})
endif()

if(OPENCV_IMGPROC_LIB)
    target_link_libraries(edgedetection ${OPENCV_IMGPROC_LIB})
endif()

if(OPENCV_IMGCODEC_LIB)
    target_link_libraries(edgedetection ${OPENCV_IMGCODEC_LIB})
endif()