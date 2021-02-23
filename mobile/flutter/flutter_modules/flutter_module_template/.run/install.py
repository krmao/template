#!/usr/bin/python
# -*- coding: UTF-8 -*-
# python 2.7

import getopt
import os.path
import shutil
import sys
import json
from os.path import expanduser

git_android_url = "https://github.com/codesdancing/app_mixpanel_android.git"
git_android_branch = ""
git_ios_url = "https://github.com/codesdancing/app_mixpanel_ios.git"
git_ios_branch = "withoutbase"

# xxx/xxx/.run
root_path = os.path.dirname(os.path.abspath(__file__))
app_path = os.path.abspath(root_path + '/.app')
app_android_path = os.path.abspath(app_path + '/android')
app_ios_path = os.path.abspath(app_path + '/ios')
flutter_project_path = os.path.abspath(root_path + '/../')
dest_host_path_android = os.path.abspath(root_path + '/.app/android/gradle')
dest_host_path_android_repo = os.path.abspath(dest_host_path_android + '/host')
dest_host_path_ios = os.path.abspath(root_path + '/.app/ios/Template/Pods/mixpanelFlutter/mixpanelFlutter/Classes')
dest_host_path_ios_repo = os.path.abspath(dest_host_path_android + '/host')
dest_ios_symroot = os.path.abspath(root_path + '/.app/build/')
home = expanduser("~")
ios_mixpod_path = home + "/.cocoapods/repos/mixinpod"

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
            print "checkout branch ->", git_android_branch
            os.system("git checkout " + git_android_branch)
        os.system("git pull && git checkout . && git log -1")
    elif platform == "ios":
        if not os.path.exists(app_ios_path):
            os.system("git clone " + git_ios_url + " " + platform)
        os.chdir(app_ios_path)
        print "current path ->", os.getcwd()
        if not git_ios_branch:
            print "checkout branch ->", git_ios_branch
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

    os.system(".fvm/flutter_sdk/bin/flutter packages get")
    change_android_code()
    shell_build_flutter_aar = ".fvm/flutter_sdk/bin/flutter build aar --" + ("release --no-profile" if release else "debug --no-profile") + " --build-number 0.0.1-SNAPSHOT " + ("--verbose " if info else "") + "--output-dir " + dest_host_path_android
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

def build_flutter_module_framework():
    install_flutter_sdk()

    os.chdir(flutter_project_path)
    print "==========>>>>>>>>>> build flutter lib framework start"
    # 参考
    # https://github.com/codesdancing/app_mixpanel_business_flutter/blob/master/flutter_modules/flutter_module_template/README_FLUTTER.md

    if not os.path.exists(dest_host_path_ios):
        os.makedirs(dest_host_path_ios)
    else:
        if os.path.exists(dest_host_path_ios):
            shutil.rmtree(dest_host_path_ios)
    os.chdir(flutter_project_path)
    print "current path ->", os.getcwd()
    os.system(".fvm/flutter_sdk/bin/flutter packages get")
    change_ios_code()
    shell_build_flutter_framework = ".fvm/flutter_sdk/bin/flutter build ios-framework --" + ("release --no-profile --no-debug" if release else "debug --no-profile --no-release") + (" --verbose" if info else "") + " --output=" + dest_host_path_ios
    os.system(shell_build_flutter_framework)
    print "==========>>>>>>>>>> build flutter lib framework end"

def build_app_ios():
    print "==========>>>>>>>>>> install ios app start"
    os.chdir(app_ios_path)
    print "current path ->", os.getcwd()
    # 参考
    # https://github.com/codesdancing/app_mixpanel_business_flutter/blob/master/flutter_modules/flutter_module_template/README_FLUTTER.md
    if not os.path.exists(dest_host_path_ios):
        build_flutter_module_framework()
    install_ios_cocoapods_mixinrepo()
    os.chdir(app_ios_path + "/Template")
    os.system("pod install")
    device = getiossimulator()
    shell_install_iosapp = "xcodebuild -workspace Template.xcworkspace -scheme Template -configuration Debug -destination "  + "'platform=iOS Simulator,name=" + device["name"]  + "'"  + " SYMROOT=" + dest_ios_symroot
    # os.chdir(app_ios_path + "/Template")
    if not os.path.exists(dest_ios_symroot):
        os.makedirs(dest_ios_symroot)
    os.system(shell_install_iosapp)
    iosapp_realpath = dest_ios_symroot + '/Debug-iphonesimulator/Template.app'
    deviceudid = device["udid"]
    os.system("xcrun instruments -w " + deviceudid)
    os.chdir(flutter_project_path)
    os.system("flutter run --use-application-binary " + iosapp_realpath + " -d " + deviceudid)

    print "==========>>>>>>>>>> auto open ios app end"
    print("==========>>>>>>>>>> install end")

def getiossimulator():

    simulators = execCmd(' xcrun simctl list --json devices available  -v iPhone')
    devices = json.loads(simulators)
    # print devices
    devicesDic = devices["devices"]
    devicesList = list(devicesDic.values())
    realDevices = []
    for device in devicesList:
        if len(device) > 0:
            for subDevice in device:
                if subDevice["isAvailable"] == True and subDevice["deviceTypeIdentifier"].find("com.apple.CoreSimulator.SimDeviceType.iPhone") != -1:
                    realDevices.append({"udid": subDevice["udid"],"name":subDevice["name"]})
                    continue

    #优先取iPhone 12
    userDevice = {}
    for device in realDevices:
        if device["name"] == "iPhone 12":
            userDevice = device
            break
    if len(userDevice["name"]) <= 0:
        userDevice = realDevices[0]

    return userDevice

def install_ios_cocoapods_mixinrepo():
    if not os.path.exists(ios_mixpod_path):
        shellCmd =  "pod repo add mixinpod https://github.com/codesdancing/org_cocoapods.git"
        os.system(shellCmd)

def execCmd(cmd):
    r = os.popen(cmd)
    text = r.read()
    r.close()
    return text

def flutter_attach():
    os.chdir(flutter_project_path)
    os.system("flutter attach")

# flutter pub get will revert .android code
def change_android_code():
    android_build_gradle_file_path = os.path.abspath(flutter_project_path + '/.android/build.gradle')

    # https://www.zhihu.com/question/50986375/answer/154758405
    lines = []
    with open(android_build_gradle_file_path, 'r') as f:
        lines = f.readlines()
    length = len(lines)
    if length > 0 :
        if lines[length-1].startswith('apply') :
            print 'ignore'
        else:
            print 'add source'
            lines.append('\napply from: "../.run/tools/build_aar_extra.gradle"')

            with open(android_build_gradle_file_path, 'w') as n:
                n.writelines(lines)

# flutter pub get will revert .ios code
def change_ios_code():
    ios_pod_file_path = os.path.abspath(flutter_project_path + '/.ios/Podfile')

    # https://www.zhihu.com/question/50986375/answer/154758405
    lines = []
    with open(ios_pod_file_path, 'r') as f:
        lines = f.readlines()
    if len(lines) > 0 :
        if lines[0].startswith('source') :
            print 'ignore'
        else:
            print 'add source'
            lines.insert(0, "source 'https://github.com/krmao/libsforios.git'\n\n")
            lines.insert(0, "source 'https://cdn.cocoapods.org/'\n")

            with open(ios_pod_file_path, 'w') as n:
                n.writelines(lines)

if __name__ == '__main__':
    opts, args = getopt.getopt(sys.argv[1:], 'hp:c:r:i:', ['p=', 'c=', 'r=', 'i=', 'help'])

    platform = 'android'
    clean = False
    release = False
    info = False

    for key, value in opts:
        print 'key=', key, 'value=', value
        if key in ['-h', '--help']:
            print ''
            print 'arguments:'
            print '-h \t\t show help'
            print '-p \t\t run android/ios'
            print '-c \t\t clean'
            print '-r \t install release'
            print '-i \t\t show log'
            sys.exit(0)
        if key in ['-p']:
            platform = value
        if key in ['-c', '--clean']:
            clean = value == "true" or value == "True" or value == "1"
        if key in ['-release']:
            release = value == "true" or value == "True" or value == "1"
        if key in ['-i', '--info']:
            info = value == "true" or value == "True" or value == "1"

    print "root_path=", root_path
    print "==========>>>>>>>>>> install start"
    print 'arguments -> clean:', clean, 'release:', release, 'platform:', platform


    if platform == "android" or platform == "ios":
        # install environment
        install_brew()
        install_dart()
        install_fvm()

        # mkdir .app
        clone_pure_app()
        # build flutter module(aar/framework)
        if platform == "android":
            print 'android'
            build_flutter_module_aar()
            # build and run app
            build_app_android()
        elif platform == "ios":
            print 'ios'
            build_flutter_module_framework()
            build_app_ios()
        # flutter attach
        flutter_attach()

