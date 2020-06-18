#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface StringUtil : NSObject

/**
 截取字符串
 @param inputStr 需要填充的字符串
 @param length 长度，一个汉字是2，一个字母是1
 @return NSString 填充完成的字符串
 */
+ (NSString *)clipString:(NSString *)inputStr length:(NSInteger)length;

/**
 判断字串是否为空
 */
+ (bool)emptyOrNull:(NSString *)str;

/**
 将string转为int ，异常时返回-1
 @param s 需要转换的字符串
 @return int 值
 */
+ (NSInteger)toInt:(NSString *)s;

/**
 将string 转为double,异常时返回-1
 @param s 需要转换的字符串
 @return 转换后的dobule 值
 */
+ (double)toDouble:(NSString *)s;

/**
 将null转为""
 @param str 需要处理的字符串
 @return 处理后的串
 */
+ (NSString *)changeNullStr:(NSString *)str;

/**
 判断字串是否是数字
 */
+ (bool)isNumString:(NSString *)str;

/**
 判断字串是否是数字
 @param str 校验是否 符合密码的格式
 @return 是 否
 */
+ (bool)isPassword:(NSString *)str;

/**
 是否是英文
 @param str 校验是否 英文字符串
 @return 是 否
 */
+ (bool)isEnString:(NSString *)str;


/**
 * 是否只有英文或者数字
 */
+ (BOOL)isStringOnlyEnOrNum:(NSString *)str;


/**
 * 是否只有英文
 *
 */

+ (BOOL)isStringOnlyEn:(NSString *)str;


/**
 是否中文
 */
+ (bool)isValidCN:(NSString *)name;


/**
 email是否合法
 */
+ (bool)isValidEMail:(NSString *)email;

/**
 输入 的字串不包含特殊字符
 */
+ (bool)isConSpeCharacters:(NSString *)str;


/**
 邮编是否正确，只验证长度以及是否全为数字
 */
+ (bool)isValidPostCode:(NSString *)code;

/**
 验证输入的手机号是否正确 只验证是否是11位
 */
+ (bool)isMobileNumber:(NSString *)mobile;

/**
 验证输入的手机号是否正确 长度为11位，第一个数字是1
 */
+ (bool)isMobileNumber2:(NSString *)mobile;


/**
 * 验证输入的手机号是否正确 长度为11位，第一个数字是1
 手机格式验证     移动：134X(0-8)、135-9、150-1、157X(0-79)(TD)、158-9、182 -4、187（3G4G)、188(3G)、147(数据卡）、178（4G）
 联通：130-2、152、155-6、185-6(3G)、145(数据卡）、176（4G）
 电信：180-1(3G)、189(3G)、133、153、（1349卫通） 、177（4G）
 虚拟运营商：170
 */
+ (bool)isMobileNumberTrue:(NSString *)mobile;


/**
 身份证合法性验证，15位身份证只验证长度
 */
+ (bool)verifyID:(NSString *)idcard;

/**
 * 校验yyyyMMdd日期是否合法
 *
 */

+ (bool)isDateRight:(NSString *)date;

/**
 字符串的字节数，一个汉字是2个字节
 @string 被检测的字符串
 @return 字节数
 */
+ (NSInteger)byteLengthOfString:(NSString *)string;

#pragma mark 按符号分割字符串

/**
 按符号分割字符串
 @param str 需要分割的字符串
 @param dot 分割的符号
 @return 分割后的数组
 */
+ (NSMutableArray *)separatedByDot:(NSString *)str Dot:(NSString *)dot;

#pragma mark
#pragma mark 添加处理 double, float类型数据到字符串的方法

/**
 价格换算,供页面开发人员调用
 人民币分，转换成界面显示的钱字符串
 @param  rmbCents 价格,int,单位: 分
 @return 价格,NSString,单位: 元
 
 rmbCents = 0, return = @"0"
 rmbCents < 0, return = @""
 rmbCents = 1111111111, return = @"11111111.11"
 rmbCents = 1111111110, return = @"11111111.1"
 rmbCents = 1111111101, return = @"11111111.01"
 */
+ (NSString *)convertRMBDisplayString:(long long)rmbCents;

+ (NSString *)convertRMBDisplayStringByLong:(long long)rmbCents;

@end

NS_ASSUME_NONNULL_END
