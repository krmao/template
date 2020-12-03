#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os
import os.path
import shutil
import sys, getopt

if __name__ == '__main__':
    opts, args = getopt.getopt(sys.argv[1:], 'hf:c:', ['f=','fv=', 'c=', 'help'])

    flutter = True
    clean = True
    flutter_version = "0.0.1-SNAPSHOT"

    for key, value in opts:

        if key in ['-h', '--help']:
            print ''
            print '参数:'
            print '-h \t 显示帮助'
            print '-f \t .fvm/flutter_sdk/bin/flutter build aar --debug --build-number 0.0.1-SNAPSHOT --pub --verbose'
            print '-c \t ./gradlew clean installDebug --info --stacktrace'
            sys.exit(0)
        if key in ['-f', '--flutter']:
            flutter = value == "Trues"
        if key in ['-fv', '--flutter-version']:
            flutter_version = value
        if key in ['-c', '--clean']:
            clean = value == "True"

    print 'clean:', clean, ',flutter:', flutter, 'flutter-version:', flutter_version

    print("---- install start ----")

    print os.getcwd()

    if flutter:
        os.chdir("../flutter_module")
        print os.getcwd()
        os.system("fvm install")
        os.system(".fvm/flutter_sdk/bin/flutter build aar --debug --build-number " + flutter_version +" --pub --verbose")
        os.chdir("../android")
        print os.getcwd()
    if clean:
        os.system("./gradlew clean installDebug --info --stacktrace")
    else :
        os.system("./gradlew installDebug --info --stacktrace")

    print("---- install end ----")