# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# C 的编译选项是 CMAKE_C_FLAGS
# 指定编译参数，可选
# SET(CMAKE_CXX_FLAGS "-Wno-error=format-security -Wno-error=pointer-sign")
# 设置头文件搜索路径（和此txt同个路径的头文件无需设置），可选
# INCLUDE_DIRECTORIES(${CMAKE_CURRENT_SOURCE_DIR}/common)
# 指定用到的系统库或者NDK库或者第三方库的搜索路径，可选。
# LINK_DIRECTORIES(/usr/local/lib)

# openCV-android-sdk路径
# set(pathToOpenCv C:/Developer/OpenCV-android-sdk)
# 引入libopencv_java3.so文件
#[[set_target_properties(
        lib_opencv
        PROPERTIES
        IMPORTED_LOCATION
        src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)]]

# 设置生成的so动态库最后输出的路径
# set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})

# 设置依赖的 .h头文件 目录
# include_directories(src/main/cpp/headers)
# 设置依赖的 so库 的目录
# link_directories(src/main/jniLibs/armeabi)
# 设置 src源文件 的目录
# aux_source_directory(src/main/cpp src_list)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        native-lib-1 # 设置生成的 so 的名称, 此处生成的名称为 libnative-lib-1.so

        # STATIC：静态库, 是目标文件的归档文件, 在链接其它目标的时候使用, 对应 .a 文件
        # SHARED：动态库, 会被动态链接，在运行时被加载
        # MODULE：模块库, 是不会被链接到其它目标中的插件, 但是可能会在运行时使用dlopen-系列的函数动态链接
        SHARED

        # Provides a relative path to your source file(s).
        native-lib-1.cpp) # 包含需要被编译的 C/C++ 源文件, 可以包含多个, 源文件路径是相对路径，相对于本CMakeLists.txt所在目录

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.
# 从系统查找依赖库，这里添加log日志库
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.
# 配置库的链接
target_link_libraries( # Specifies the target library.
        native-lib-1 # 目标库

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}) # 依赖于库

# 生成多个 so 文件 方式一
add_library(
        native-lib-2
        SHARED
        native-lib-2.cpp
)
target_link_libraries(
        native-lib-2
        ${log-lib}
)
# 生成多个 so 文件 方式二
# ADD_SUBDIRECTORY(src/main/cpp/second) # 添加子目录, 将会调用子目录中的CMakeLists.txt