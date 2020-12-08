#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_smart_library_util_STCppTestUtil_stringFromJNI2(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hi from Cpp";
    return env->NewStringUTF(hello.c_str());
}
