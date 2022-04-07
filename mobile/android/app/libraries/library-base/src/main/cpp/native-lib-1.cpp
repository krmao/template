#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_smart_library_util_STCppTestUtil_stringFromJNI1(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
