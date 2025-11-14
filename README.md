Edge Detection Viewer
A real-time edge detection Android application that captures camera frames, processes them using OpenCV C++ via JNI, and displays the output using OpenGL ES. Includes a TypeScript web viewer for displaying processed frames.

🚀 Features
Android App
Real-time Camera Feed: Live camera preview using CameraX API

Edge Detection: Canny edge detection using OpenCV C++

OpenGL Rendering: Hardware-accelerated rendering with OpenGL ES 2.0+

Performance Monitoring: Real-time FPS counter

Toggle Modes: Switch between raw camera feed and edge-detected output

Native Performance: C++ implementation with JNI bridge

Web Viewer
TypeScript Implementation: Modern, type-safe web application

Image Processing: Simulated edge detection in browser

Frame Statistics: FPS and resolution display

File Upload: Support for custom image processing

Responsive Design: Works on desktop and mobile browsers

🛠️ Tech Stack
Android
Language: Kotlin

Native: C++ with JNI

Computer Vision: OpenCV 4.5.5+

Graphics: OpenGL ES 2.0+

Camera: CameraX API

Build: CMake, Android NDK

Web
Language: TypeScript

Rendering: HTML5 Canvas

Build: TypeScript Compiler

📁 Project Structure
text
EdgeDetectionViewer/
├── app/                                  # Android Application
│   ├── src/main/
│   │   ├── java/com/example/edgedetection/
│   │   │   ├── MainActivity.kt          # Main activity with camera setup
│   │   │   ├── EdgeDetectionView.kt     # OpenGL ES view for rendering
│   │   │   └── EdgeDetectionAnalyzer.kt # Image analysis pipeline
│   │   ├── cpp/                         # Native C++ code
│   │   │   ├── CMakeLists.txt           # CMake build configuration
│   │   │   ├── native-lib.cpp           # JNI bridge functions
│   │   │   ├── opencv-processor.cpp     # OpenCV image processing
│   │   │   ├── gl-renderer.cpp          # OpenGL rendering utilities
│   │   │   └── include/
│   │   │       └── edge-detection.h     # Native header file
│   │   └── res/
│   │       └── layout/
│   │           └── activity_main.xml    # Main activity layout
│   └── build.gradle.kts                 # Android build configuration
├── web/                                 # TypeScript Web Viewer
│   ├── src/
│   │   └── viewer.ts                    # Main web application logic
│   ├── index.html                       # Web viewer interface
│   ├── package.json                     # Node.js dependencies
│   └── tsconfig.json                    # TypeScript configuration
└── README.md
⚙️ Setup Instructions
Prerequisites
Android Studio Arctic Fox or later

Android SDK API 26+

Android NDK 23+

OpenCV Android SDK 4.5.5+

Node.js (for web viewer)

Android Setup
Clone the repository

bash
git clone https://github.com/thund3rSpark/EdgeDetectionViewer.git
cd EdgeDetectionViewer
Install OpenCV Android SDK

Download OpenCV Android SDK from OpenCV.org

Extract to C:\Users\murug\OneDrive\Desktop\Noth\OpenCV-android-sdk\

Update the OpenCV path in app/src/main/cpp/CMakeLists.txt if needed

Build and Run

Open project in Android Studio

Build the project (Ctrl+F9)

Run on device or emulator (Shift+F10)

Web Viewer Setup
Navigate to web directory

bash
cd web
Install dependencies

bash
npm install
Build TypeScript

bash
npm run build
Serve the application

bash
npm run serve
# or
python -m http.server 8000
Open in browser

text
http://localhost:8000
