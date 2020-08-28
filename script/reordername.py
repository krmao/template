#!/usr/bin/python
# -*- coding: UTF-8 -*-
import csv
import os
import os.path
import shutil

# ======================================================================================================================
# 按 csv 索引顺序批处批量处理文件名
#                             ---- by smart template
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
        print("target_order_num=", target_order_num, "current_order_num=", current_order_num)

        for old_file_index, old_file_name in enumerate(old_file_names):
            # 469-1.jpg ->  370-1.jpg
            # 470.jpg   ->  371.jpg
            # 471.jpg   ->  372.jpg
            # 471-1.jpg ->  372-1.jpg
            # 472.jpg   ->  373.jpg
            # 472-1.jpg ->  373-1.jpg

            old_num = int(old_file_name.split(".")[0].split("-")[0])
            if old_num == current_order_num:
                print("current_order_num=", current_order_num, "old_num=", old_num, "old_file_index=", old_file_index, "old_file_name=", old_file_name, "target_order_num=", target_order_num)
                new_file_name = old_file_name.replace(str(old_num), str(target_order_num))
                print(old_file_name, "->", new_file_name)
                rename_file_to(os.path.join(DIR_PATH_OLD, old_file_name), os.path.join(DIR_PATH_NEW, new_file_name))
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
