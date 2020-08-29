#!/usr/bin/python
# -*- coding: UTF-8 -*-
import csv
import os
import os.path
import shutil

# ======================================================================================================================
# 批处理文件
#
# 用途与前提条件:
# >1. 当 DIR_PATH_OLD 文件夹中的所有文件名称中的第一个数字(支持中横线分割)与 CSV文件第一列的数字一一对应的时候 (否则肯定出错!!!)
# >2. 将 旧的文件按照 CSV文件本身的递增索引 拷贝并重新命名 到 DIR_PATH_NEW 文件夹
#
# 案例:
# --------------------------------------------------------------------------------------------------------------------
# | CSV表格递增索引   |    CSV 第一列数字  |     原始文件名称                       |   目标文件名称                        |
# | 1               |    3              |     3.txt/3-1.txt/3-1-2.txt/3-2.txt  |   1.txt/1-1.txt/1-1-2.txt/1-2.txt   |
# | 2               |    6              |     6.txt                            |   2.txt                             |
# | 3               |    7              |     7.txt                            |   3.txt                             |
# | 4               |    8              |     8.txt                            |   4.txt                             |
# | 5               |    11             |     11.txt                           |   5.txt                             |
# | ...             |    ...            |     ...                              |   ...                               |
# | 370             |    469            |     469-1.jpg                        |   370-1.jpg                         |
# | 371             |    470            |     470.jpg                          |   371.jpg                           |
# | 372             |    471            |     471.jpg/471-1.jpg                |   372.jpg/372-1.jpg                 |
# | 373             |    472            |     472.jpg/472-1.jpg                |   373.jpg/373-1.jpg                 |
# --------------------------------------------------------------------------------------------------------------------
#                                                                               ---- python 3.7
#                                                                               ---- by smart template
# ======================================================================================================================

DIR_PATH_OLD = './files_old/'
DIR_PATH_NEW = './files_new/'


def read_old_files():
    old_file_names = []
    print("read old files start --->")
    for fileName in os.listdir(DIR_PATH_OLD):
        old_file_names.append(fileName)
    print("read old files end --->")
    return old_file_names


def read_origin_csv(csv_file_name):
    print("read csv start --->")
    order_row_list = []

    with open(csv_file_name, 'r', encoding='utf-8') as f:
        reader = csv.reader(f)

        # print(list(reader)[1]) 直接获取某一行

        for index, row in enumerate(reader):
            first_column_string = row[0]
            first_column_is_digit = first_column_string.isdigit()

            if first_column_is_digit:
                # print((index, row))  # 获取每一行的第一列
                order_row_list.append((index, row))
                # if int(first_column_string) > 16:
                #     break
    print("read csv end --->")
    return order_row_list


def rename_file_to(oldpath, newpath):
    shutil.copyfile(oldpath, newpath)
    print("rename", oldpath, "to", newpath)


def renames(csv_rows, old_file_names):
    print("renames start --->")
    for index, data in enumerate(csv_rows):
        target_order_num = int(data[0]) + 1  # 从 1 开始
        row = data[1]
        current_order_num = int(row[0])

        for old_file_index, old_file_name in enumerate(old_file_names):
            old_num = int(old_file_name.split(".")[0].split("-")[0])
            if old_num == current_order_num:
                new_file_name = old_file_name.replace(str(old_num), str(target_order_num))
                print(">>>> (newFileName:" + str(target_order_num), "newFileFullName:" + str(new_file_name) + ")", "(oldFileName=" + str(old_num), "oldFileFullName=" + str(old_file_name) + ")")
                rename_file_to(os.path.join(DIR_PATH_OLD, old_file_name), os.path.join(DIR_PATH_NEW, new_file_name))
                print("----")
    print("renames end --->")


if __name__ == '__main__':
    print("processing start --->")

    if not os.path.exists(DIR_PATH_OLD):
        os.makedirs(DIR_PATH_OLD)
    if os.path.exists(DIR_PATH_NEW):
        shutil.rmtree(DIR_PATH_NEW)
    os.makedirs(DIR_PATH_NEW)

    renames(read_origin_csv('origin.csv'), read_old_files())

    print("processing end --->")
