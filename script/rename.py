#!/usr/bin/python
# -*- coding: UTF-8 -*-
# 图片转文字 && 字符串相似度
import csv
# https://www.jianshu.com/p/853d86e090a7
# https://www.cnblogs.com/573734817pc/p/10900379.html
import difflib
import os
import os.path
import shutil
import time

# https://www.jianshu.com/p/96b95a6c9210
# https://www.jianshu.com/p/2c9459059400
# https://github.com/tesseract-ocr/tessdata
# copy chi_sim.traineddata,chi_sim_vert.traineddata to /usr/local/Cellar/tesseract/4.1.1/share/tessdata
# https://blog.csdn.net/intenttao/article/details/84578571
import pytesseract
from PIL import Image
from selenium import webdriver

# 打开网页自动截屏
# http://chromedriver.storage.googleapis.com/index.html?path=84.0.4147.30/ 下载自己安装的 chrome 对应的版本

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

        open_url_and_screenshot_with_chrome(row[5])
        print("max_ratio=[", format(max_ratio, '.4f'), "] , max_origin_num=[", max_origin_row[1], "] , max_origin_string=", max_origin_row[0], ", max_image_string=", max_image_string)


def open_url_and_screenshot_with_chrome(url):
    print("open_url_and_screenshot_with_chrome->", url)

    options = webdriver.ChromeOptions()
    # 去除安全提示
    options.add_argument('disable-infobars')
    options.add_argument('--headless')
    options.add_argument('--disable-gpu')
    driver = webdriver.Chrome("./chromedriver", options=options)
    # 算上浏览器高度
    # web_width = 1920
    # web_height = 906
    # driver.set_window_size(web_width, web_height + 98)
    driver.maximize_window()
    # 返回网页的高度的js代码
    js_height = "return document.body.clientHeight"
    try:
        driver.get(url)
        k = 1
        height = driver.execute_script(js_height)
        while True:
            if k * 500 < height:
                js_move = "window.scrollTo(0,{})".format(k * 500)
                print(js_move)
                driver.execute_script(js_move)
                time.sleep(0.2)
                height = driver.execute_script(js_height)
                k += 1
            else:
                break
        scroll_width = driver.execute_script('return document.body.parentNode.scrollWidth')
        scroll_height = driver.execute_script('return document.body.parentNode.scrollHeight')
        # scroll_height = height
        driver.set_window_size(scroll_width, scroll_height)
        driver.get_screenshot_as_file(DIR_PATH_NEW + "a" + ".png")
        print("Process {} get one pic !!!".format(os.getpid()))
        time.sleep(0.1)
    except Exception as e:
        print(e)


def image_merge(images, output_dir, output_name='merge.jpg', restriction_max_width=None, restriction_max_height=None):
    """垂直合并多张图片
    images - 要合并的图片路径列表
    ouput_dir - 输出路径
    output_name - 输出文件名
    restriction_max_width - 限制合并后的图片最大宽度，如果超过将等比缩小
    restriction_max_height - 限制合并后的图片最大高度，如果超过将等比缩小
    """

    # def image_resize(img, size=(1500, 1100)):
    def image_resize(img, size=(1960, 1080)):
        """调整图片大小
        """
        try:
            if img.mode not in ('L', 'RGB'):
                img = img.convert('RGB')
            img = img.resize(size)
        except Exception as e:
            pass
        return img

    max_width = 0
    total_height = 0
    # 计算合成后图片的宽度（以最宽的为准）和高度
    for img_path in images:
        if os.path.exists(img_path):
            img = Image.open(img_path)
            width, height = img.size
            if width > max_width:
                max_width = width
            total_height += height

    # 产生一张空白图
    new_img = Image.new('RGB', (max_width, total_height), 255)
    # 合并
    x = y = 0
    for img_path in images:
        if os.path.exists(img_path):
            img = Image.open(img_path)
            width, height = img.size
            new_img.paste(img, (x, y))
            y += height

    if restriction_max_width and max_width >= restriction_max_width:
        # 如果宽带超过限制
        # 等比例缩小
        ratio = restriction_max_height / float(max_width)
        max_width = restriction_max_width
        total_height = int(total_height * ratio)
        new_img = image_resize(new_img, size=(max_width, total_height))

    if restriction_max_height and total_height >= restriction_max_height:
        # 如果高度超过限制
        # 等比例缩小
        ratio = restriction_max_height / float(total_height)
        max_width = int(max_width * ratio)
        total_height = restriction_max_height
        new_img = image_resize(new_img, size=(max_width, total_height))

    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    save_path = '%s/%s' % (output_dir, output_name)
    new_img.save(save_path)
    for img_path in images:
        os.remove(img_path)
    return save_path


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

    # ps -ef | grep chrome
    # pool = mp.Pool()
    # pool.map_async(func=open_url_and_screenshot_with_chrome, iterable=["https://chejiahao.autohome.com.cn/info/6073373"])
    # pool.close()
    # pool.join()

    # open_url_and_screenshot_with_chrome("https://chejiahao.autohome.com.cn/info/6073373")
    # open_url_and_screenshot_with_chrome("https://www.ixigua.com/i6824676721900388871/")
    # open_url_and_screenshot_with_chrome("https://chejiahao.autohome.com.cn/info/6090351")

    print("processing end --->")
