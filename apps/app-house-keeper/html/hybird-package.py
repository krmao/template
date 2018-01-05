#!/usr/bin/python
# -*- coding: UTF-8 -*-
import getopt
import os
import shutil
import sys

root_project_path = sys.path[0]
root_build_output_dir = root_project_path + "/build-output"
commander = "npm"


def copy(from_dir, to_dir):
    if os.path.exists(from_dir):
        file_list = os.listdir(from_dir)
        for file_name in file_list:
            file_path = os.path.join(from_dir, file_name)
            dest_file_path = os.path.join(to_dir, file_name)
            if os.path.exists(dest_file_path):
                print "删除旧的文件: ", dest_file_path
                os.remove(dest_file_path)
            if os.path.exists(file_path):
                print "拷贝新的文件: ", file_path
                shutil.copyfile(file_path, dest_file_path)
    else:
        print "拷贝失败 资源目录不存在: ", from_dir


# .idea
def package(project_name, env):
    sub_project_full_path = os.path.join(root_project_path, project_name)
    if os.path.isdir(sub_project_full_path) \
            and not project_name.__contains__(".idea") \
            and not project_name.__contains__("build") \
            and not project_name.__contains__("node_modules") \
            and not project_name.__contains__("utils"):

        package_json_file_path = sub_project_full_path + "/package.json"
        project_build_output_dir = sub_project_full_path + "/build-output"
        project_build_output_dir_in_root = root_build_output_dir + "/" + project_name

        if os.path.exists(package_json_file_path):
            print "----打包子模块 开始: ", sub_project_full_path
            command = "cd " + sub_project_full_path + " && " + commander + " install && npm run build " + env
            print "----执行 command: ", command
            os.system(command)

            print "copy ", project_build_output_dir, " ----> ", project_build_output_dir_in_root

            # if os.path.isdir(project_build_output_dir_in_root):
            #     shutil.rmtree(project_build_output_dir_in_root)
            # if not os.path.exists(project_build_output_dir_in_root) \
            #         and not os.path.isdir(project_build_output_dir_in_root):
            # os.makedirs(project_build_output_dir_in_root)
            # shutil.copytree(project_build_output_dir, root_build_output_dir)

            copy(project_build_output_dir, root_build_output_dir)

            print "----打包子模块 结束: ", sub_project_full_path
        else:
            print "project by path is not exists !!! ", package_json_file_path


def init():
    print "初始化根项目依赖 开始"
    command = "cd " + root_project_path + " && " + commander + " install"
    print "执行 command: ", command
    os.system(command)


def package_all(env):
    print "打包所有的模块 开始"
    print "root_project_path: ", root_project_path
    print "env: ", env
    print "----------"

    if os.path.isdir(root_build_output_dir):
        shutil.rmtree(root_build_output_dir)
    os.makedirs(root_build_output_dir)

    sub_project_list = os.listdir(root_project_path)
    for module_name in sub_project_list:
        package(module_name, env)

    print "打包所有的模块 结束"


if __name__ == '__main__':
    opts, args = getopt.getopt(sys.argv[1:], "he:m:c")

    env = "prd"
    sub_project_name = "null"

    for op, value in opts:
        if op == '-h':
            print """
================================================================================
    python hybird-package.py -e prd -m buyMealCard -c
    
    -e 指定编译的环境, prd/pre/sit, 默认为 prd
    -m 指定编译的模块, 默认不加此参数则打包所有模块,并输出到 项目根目录/build-output
    -c 使用 cnpm install
    
    案例: 打包所有模块 prd 环境                           -> python hybird-package.py
    案例: 打包所有模块 pre 环境                           -> python hybird-package.py -e pre
    案例: 打包buyMealCard模块 prd 环境                    -> python hybird-package.py -m buyMealCard
    案例: 打包buyMealCard模块 pre 环境                    -> python hybird-package.py -e pre -m buyMealCard
    案例: 打包buyMealCard模块 pre 环境, 用 cnpm 安装依赖  -> python hybird-package.py -e pre -m buyMealCard -c
    
    注意: 重复打包会删除已经存在的老版本同名文件
================================================================================
            """
            sys.exit()
        elif op == "-c":
            commander = "cnpm"
            print "将使用 ", commander, " install 安装依赖"
        elif op == "-e":
            env = value
        elif op == "-m":
            sub_project_name = value

    print "commander:", commander

    init()

    if sub_project_name != "null":
        package(sub_project_name, env)
    else:
        package_all(env)
