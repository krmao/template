#!/usr/bin/python
# -*- coding: UTF-8 -*-
# 图片转文字 && 字符串相似度
import csv
# https://www.jianshu.com/p/853d86e090a7
# https://www.cnblogs.com/573734817pc/p/10900379.html
import difflib
import os
import shutil

# https://www.jianshu.com/p/96b95a6c9210
# https://www.jianshu.com/p/2c9459059400
# https://github.com/tesseract-ocr/tessdata
# copy chi_sim.traineddata,chi_sim_vert.traineddata to /usr/local/Cellar/tesseract/4.1.1/share/tessdata
# https://blog.csdn.net/intenttao/article/details/84578571
import pytesseract
from PIL import Image

# ======================================================================================================================
# 批量处理文件名
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


def read_origin_csv():
    print("read csv start --->")
    order_row_list = []

    with open('origin.csv', 'r') as f:
        reader = csv.reader(f)

        # print(list(reader)[1]) 直接获取某一行

        for index, row in enumerate(reader):
            first_column_string = row[0]
            is_tou_tiao = row[2] == "今日头条"

            wrap_column_string = row[3] + row[2] + row[5]  # + row[4] + row[1]
            if is_tou_tiao:
                wrap_column_string = row[2] + row[3] + row[5]  # + row[4] + row[1]

            final_column_string = wrap_column_string \
                .replace(" ", "") \
                .replace("“", "") \
                .replace("”", "") \
                .replace(" ", "")

            first_column_is_digit = first_column_string.isdigit()
            if first_column_is_digit:
                # print(final_column_string)  # 获取每一行的第一列
                order_row_list.append((final_column_string, int(row[0]), row[1], row[2], row[3], row[4], row[5]))
                if int(first_column_string) > 16:
                    break

    order_row_list.append(("今日头条超宽体领潮轿跑第三代名爵6预计7月上市名爵", 20, "2020/5/7", "今日头条", "超宽体领潮轿跑第三代名爵6预计7月上市", "https://chejiahao.autohome.com.cn/info/6073373", "名爵"))
    order_row_list.append(("将于7月上市新款名爵6实拍图正式曝光懂车帝名爵", 20, "2020/5/7", "懂车帝", "超宽体领潮轿跑第三代名爵6预计7月上市", "https://chejiahao.autohome.com.cn/info/6073373", "名爵"))
    print("read csv end --->")
    return order_row_list


def rename_file_to(oldpath, newpath):
    shutil.copyfile(oldpath, newpath)
    print("rename", oldpath, "to", newpath)


def renames(old_file_names, order_list):
    print("renames start --->")
    for index, old_file_name in enumerate(old_file_names):
        new_file_name = order_list[index] + "-" + old_file_name
        rename_file_to(os.path.join(DIR_PATH_OLD, old_file_name), os.path.join(DIR_PATH_NEW, new_file_name))
    print("renames end --->")


# 比较两个字符串的相似度
def sequence_matcher_ratio(string1, string2):
    return difflib.SequenceMatcher(None, string1, string2).quick_ratio()


def get_image_string(image_name):
    image = Image.open('./files_old/' + image_name)
    image.resize((5000, 5000), Image.ANTIALIAS)
    string = pytesseract.image_to_string(image, lang='chi_sim') \
        .replace(" ", "") \
        .replace("“", "") \
        .replace("”", "") \
        .replace("\n", "") \
        .replace("0", "") \
        .replace("近", "") \
        .replace("渡", "") \
        .replace("孝", "") \
        .replace("/", "") \
        .replace("is", "") \
        .replace("勇", "") \
        .replace("EE", "") \
        .replace("沥", "") \
        .replace(",", "") \
        .replace("超家体", "超宽体") \
        .replace("ijss[46", "") \
        .replace("圃", "") \
        .replace("【", "") \
        .replace("上勺", "上市") \
        .replace("=北6RR", "") \
        .replace("&", "")
    return string


def get_image_string_list(image_names):
    image_strings = []
    for image_name in image_names:
        image_string = get_image_string(image_name)
        image_string = image_string[0:30]
        image_strings.append(image_string)
    return image_strings


def get_all_ratios(image_strings, origin_row):
    for row in origin_row:
        max_ratio = -1
        max_origin_row = row
        max_image_string = ""
        for image_string in image_strings:
            ratio = sequence_matcher_ratio(row[0], image_string)
            if ratio > max_ratio:
                max_ratio = ratio
                max_image_string = image_string

        print("max_ratio=[", format(max_ratio, '.4f'), "] , max_origin_num=[", max_origin_row[1], "] , max_origin_string=", max_origin_row[0], ", max_image_string=", max_image_string)


if __name__ == '__main__':
    print("processing start --->")

    if not os.path.exists(DIR_PATH_OLD):
        os.makedirs(DIR_PATH_OLD)
    if os.path.exists(DIR_PATH_NEW):
        shutil.rmtree(DIR_PATH_NEW)
    os.makedirs(DIR_PATH_NEW)

    old_names = read_old_files()
    get_all_ratios(get_image_string_list(old_names), read_origin_csv())
    # renames(old_names, orders)

    print("processing end --->")
