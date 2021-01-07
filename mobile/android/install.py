#!/usr/bin/python
# -*- coding: UTF-8 -*-

import getopt
import os.path
import sys
import shutil

if __name__ == '__main__':
    opts, args = getopt.getopt(sys.argv[1:], 'hf:fv:c:r:i:', ['f=', 'fv=', 'c=', 'r=', 'i=', 'help'])

    flutter = True
    clean = False
    release = False
    info = False
    flutter_version = "0.0.1-SNAPSHOT"

    for key, value in opts:
        if key in ['-h', '--help']:
            print ''
            print 'arguments:'
            print '-h \t show help'
            print '-f \t rebuild flutter aar'
            print '-fv \t flutter aar version'
            print '-c \t clean'
            print '-r \t install release'
            print '-i \t show log'
            sys.exit(0)
        if key in ['-f', '--flutter']:
            flutter = value == "true" or value == "True" or value == "1"
        if key in ['-fv', '--flutter-version']:
            flutter_version = value
        if key in ['-c', '--clean']:
            clean = value == "true" or value == "True" or value == "1"
        if key in ['-r', '--release']:
            release = value == "true" or value == "True" or value == "1"
        if key in ['-i', '--info']:
            info = value == "true" or value == "True" or value == "1"

    print "==========>>>>>>>>>> install start"
    print 'arguments -> clean:', clean, ',flutter:', flutter, 'flutter-version:', flutter_version, 'release:', release

    if flutter:
        os.chdir("../flutter/flutter_modules/flutter_module_template")
        if not os.path.exists(".fvm/flutter_sdk/bin/flutter"):
            print "==========>>>>>>>>>> install flutter sdk start"
            print "current path ->", os.getcwd()
            shell_install_flutter_sdk = "fvm install"
            print shell_install_flutter_sdk
            os.system(shell_install_flutter_sdk)
            print "==========>>>>>>>>>> install flutter sdk end"
        print "==========>>>>>>>>>> build flutter lib aar start"
        print "current path ->", os.getcwd()

        dest_host_path = os.path.abspath('../../../android/gradle/')
        if not os.path.exists(dest_host_path):
            os.makedirs(dest_host_path)
        else:
            if os.path.exists(os.path.abspath('../../../android/gradle/host')):
                shutil.rmtree(os.path.join(root))
            os.makedirs(dest_host_path)

        shell_build_flutter_aar = ".fvm/flutter_sdk/bin/flutter build aar --" + ("release --no-profile --no-debug" if release else "debug --no-profile --no-release") + " --build-number " + flutter_version + " --pub " + ("--verbose" if info else "") + "--output-dir " + dest_host_path
        print shell_build_flutter_aar
        os.system(shell_build_flutter_aar)
        print "==========>>>>>>>>>> build flutter lib aar end"
        os.chdir("../../../android")
    print "==========>>>>>>>>>> install android app start"
    print "current path ->", os.getcwd()
    if clean:
        shell_install = "./gradlew clean install" + ("Release" if release else "Debug") + (" --info" if info else " ") + " --stacktrace"
        print shell_install
        os.system(shell_install)
    else:
        shell_install = "./gradlew install" + ("Release" if release else "Debug") + (" --info" if info else " ") + " --stacktrace"
        print shell_install
        os.system(shell_install)
    print "==========>>>>>>>>>> install android app end"
    print "==========>>>>>>>>>> auto open android app start"
    shell_install = """adb shell am start -n "com.smart.template/com.smart.template.FinalLaunchActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"""
    # shell_install = """adb shell am start -n "com.smart.template/com.smart.library.flutter.STFlutterBoostLaunchActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"""
    print shell_install
    os.system(shell_install)
    print "==========>>>>>>>>>> auto open android app end"
    print("==========>>>>>>>>>> install end")
