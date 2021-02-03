#!/usr/bin/python
# -*- coding: UTF-8 -*-
# python 2.7

import getopt
import os.path
import shutil
import sys
from os.path import expanduser

git_android_url = "https://gitee.com/krmao/android_app_template.git"
git_android_branch = ""
git_ios_url = ""
git_ios_branch = ""

# xxx/xxx/.run
root_path = os.path.dirname(os.path.abspath(__file__))
app_path = os.path.abspath(root_path + '/.app')
app_android_path = os.path.abspath(app_path + '/android')
app_ios_path = os.path.abspath(app_path + '/ios')
flutter_project_path = os.path.abspath(root_path + '/../')
dest_host_path_android = os.path.abspath(root_path + '/.app/android/gradle')
dest_host_path_android_repo = os.path.abspath(dest_host_path_android + '/host')
home = expanduser("~")


def clone_pure_app():
    if not os.path.exists(app_path):
        os.makedirs(app_path)

        # clone app by git url
    os.chdir(app_path)
    print "current path ->", os.getcwd()

    if platform == "android":
        if not os.path.exists(app_android_path):
            os.system("git clone " + git_android_url + " " + platform)
        os.chdir(app_android_path)
        print "current path ->", os.getcwd()
        if not git_android_branch:
            os.system("git checkout " + git_android_branch)
        os.system("git pull && git checkout . && git log -1")
    elif platform == "ios":
        os.system("git clone " + git_ios_url + " " + platform)
        if not git_ios_branch:
            os.system("git checkout " + git_ios_branch)
        os.system("git pull && git checkout . && git log -1")


def install_brew():
    os.chdir(home)
    if not os.path.exists("/usr/local/bin/brew"):
        print "==========>>>>>>>>>> install brew start"
        print "current path ->", os.getcwd()
        os.system('/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"')
        print "==========>>>>>>>>>> install brew end"
    os.system("which brew")


def install_dart():
    os.chdir(home)
    if not os.path.exists("/usr/local/bin/dart"):
        print "==========>>>>>>>>>> install dart start"
        print "current path ->", os.getcwd()
        os.system("brew tap dart-lang/dart")
        os.system("brew install dart")
        os.system("brew switch dart 2.10.4")
        os.system("brew info dart")
        os.system('export PATH="$PATH":"~/.pub-cache/bin"')
        print "==========>>>>>>>>>> install dart end"
    os.system("which dart")


def install_fvm():
    os.chdir(home)
    if not os.path.exists(".pub-cache/bin/fvm"):
        print "==========>>>>>>>>>> install fvm start"
        print "current path ->", os.getcwd()
        os.system("pub global activate fvm")
        os.system('export PATH="$PATH":"~/fvm/default/bin"')
        os.system("fvm install")
        print "==========>>>>>>>>>> install fvm end"
    os.system("which fvm")


def install_flutter_sdk():
    os.chdir(flutter_project_path)
    if not os.path.exists(".fvm/flutter_sdk/bin/flutter"):
        print "==========>>>>>>>>>> install flutter sdk start"
        print "current path ->", os.getcwd()
        os.system("fvm install")
        print "==========>>>>>>>>>> install flutter sdk end"
    os.system("which flutter")


def build_flutter_module_aar():
    install_flutter_sdk()

    os.chdir(flutter_project_path)
    print "==========>>>>>>>>>> build flutter lib aar start"
    print "current path ->", os.getcwd()

    if not os.path.exists(dest_host_path_android):
        os.makedirs(dest_host_path_android)
    else:
        if os.path.exists(dest_host_path_android_repo):
            shutil.rmtree(dest_host_path_android_repo)

    shell_build_flutter_aar = ".fvm/flutter_sdk/bin/flutter build aar --" + ("release --no-profile" if release else "debug --no-profile") + " --build-number 0.0.1-SNAPSHOT --pub " + ("--verbose " if info else "") + "--output-dir " + dest_host_path_android
    print shell_build_flutter_aar
    os.system(shell_build_flutter_aar)
    print "==========>>>>>>>>>> build flutter lib aar end"
    os.chdir(root_path)


def build_app_android():
    print "==========>>>>>>>>>> install android app start"
    os.chdir(app_android_path)
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
    shell_install = """adb shell am start -n "com.codesdancing.mixpure/com.smart.template.FinalLaunchActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"""
    # shell_install = """adb shell am start -n "com.smart.template/com.smart.library.flutter.STFlutterBoostLaunchActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER"""
    print shell_install
    os.system(shell_install)
    print "==========>>>>>>>>>> auto open android app end"
    print("==========>>>>>>>>>> install end")


def flutter_attach():
    os.chdir(flutter_project_path)
    os.system("flutter attach")


if __name__ == '__main__':
    opts, args = getopt.getopt(sys.argv[1:], 'hrun:c:release:i:', ['run=', 'c=', 'release=', 'i=', 'help'])

    platform = 'android'
    clean = False
    release = False
    info = False

    for key, value in opts:
        if key in ['-h', '--help']:
            print ''
            print 'arguments:'
            print '-h \t\t show help'
            print '-run \t\t run android/ios'
            print '-c \t\t clean'
            print '-release \t install release'
            print '-i \t\t show log'
            sys.exit(0)
        if key in ['-run']:
            platform = value
        if key in ['-c', '--clean']:
            clean = value == "true" or value == "True" or value == "1"
        if key in ['-release']:
            release = value == "true" or value == "True" or value == "1"
        if key in ['-i', '--info']:
            info = value == "true" or value == "True" or value == "1"

    print "root_path=", root_path
    print "==========>>>>>>>>>> install start"
    print 'arguments -> clean:', clean, 'release:', release

    if platform == "android" or platform == "ios":
        # install environment
        install_brew()
        install_dart()
        install_fvm()

        # mkdir .app
        clone_pure_app()

        # build flutter module(aar/framework)
        build_flutter_module_aar()
        # build and run app
        build_app_android()
        # flutter attach
        flutter_attach()
