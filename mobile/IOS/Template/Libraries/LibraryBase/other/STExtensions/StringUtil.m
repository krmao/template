#import "StringUtil.h"
#import "NSString+STExtension.h"
#import "NSArray+STExtension.h"
#import "STMacros.h"

@implementation StringUtil

/**
 * 判断字串是否为空
 */
+ (bool)emptyOrNull:(NSString *)str {
    return (str == NULL) ||
            ([str isEqual:[NSNull null]]) ||
            ([str isKindOfClass:[NSString class]] && str.length == 0);
}

/**
 * 将string转为int ，异常时返回-1
 */

+ (NSInteger)toInt:(NSString *)s {
    int i = 0;

    bool bo = [StringUtil isNumString:s];

    if (bo == NO) {
        i = -1;
        //        CTDigitExceptionLog(s);
        NSString *str = @"";

        if (s != nil) {
            str = [NSString stringWithFormat:@"%@", s];
        }

        //        CTDigitExceptionLog(str);
        //      SLog(str);
        SLog(@"%@", str);
    } else {
        i = [s intValue];
    }

    return i;
}


/**
 * 将string 转为double,异常时返回-1
 */
+ (double)toDouble:(NSString *)s {
    double i = 0;

    bool valid = [StringUtil isValidDigitString:s];

    if (valid == NO) {
        i = -1;

        NSString *str = @"";

        if (s != nil) {
            str = [NSString stringWithFormat:@"%@", s];
        }

        //      SLog(str);
        SLog(@"%@", str);
    } else {
        i = [s doubleValue];
    }

    return i;
}

/**
 * 校验字符串是否是浮点类型的字符串
 * @return 是否成功
 */
+ (bool)isValidDigitString:(NSString *)str {
    NSString *match = @"[0-9]+|[0-9]+[.]|[.]{1}[0-9]+|[0-9]+|[0-9]+[.]{1}[0-9]+";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];
    bool valid = [predicate evaluateWithObject:str];

    return valid;
}

/**
 * 将null转为""
 *
 */

+ (NSString *)changeNullStr:(NSString *)str {
    if (str == nil) {
        return @"";
    } else {
        return str;
    }
}

/**
 * 判断字串是否是数字
 */
+ (bool)isNumString:(NSString *)str {
    NSString *match = @"[0-9]+";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];
    bool valid = [predicate evaluateWithObject:str];
    return valid;
}

/**
 * 判断字串是否是数字
 *
 */
+ (bool)isPassword:(NSString *)str {
    NSString *match = @"[A-Za-z0-9\\!\\#\\@\\$\\%\\^\\&\\*\\.\\~\\`\\(\\)\\-\\_\\+\\=\\[\\]\\{\\}\\|\\;\\:\\'\\,\\.\\<\\>\\?\\/]{6,20}$";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];

    return [predicate evaluateWithObject:str];
}

/**
 * 是否是英文
 *
 */
+ (bool)isEnString:(NSString *)str {
    NSString *match = @"[\\s]*[A-Za-z]+[\\s]*";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];

    return [predicate evaluateWithObject:str];
}

/**
 * 是否只有英文或者数字
 */
+ (BOOL)isStringOnlyEnOrNum:(NSString *)str {
    NSString *match = @"[A-Za-z0-9]+";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];

    return [predicate evaluateWithObject:str];
}


/**
 * 是否只有英文
 */
+ (BOOL)isStringOnlyEn:(NSString *)str {
    NSString *match = @"[A-Za-z]+";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];

    return [predicate evaluateWithObject:str];
}

/**
 * 是否中文
 */
+ (bool)isValidCN:(NSString *)name {
    NSString *match = @"(^[\u4e00-\u9fa5]+$)";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];

    return [predicate evaluateWithObject:name];
}

/**
 * email是否合法
 */
+ (bool)isValidEMail:(NSString *)email {
    NSString *match = @"\\S+@(\\S+\\.)+[\\S]{1,6}";
    //  NSString *match=@"[a-zA-Z0-9_.-]+@([a-zA-Z0-9]+\\.)+[a-zA-Z]{1,6}";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];
    bool valid = [predicate evaluateWithObject:email];

    return valid;
}

///**
// * 输入 的字串不包含特殊字符
// *
// * @param string
// * @return 不包则返回true 否则 返回 false
// */
+ (bool)isConSpeCharacters:(NSString *)str {
    NSString *match = @"[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*";
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"SELF matches %@", match];

    return [predicate evaluateWithObject:str];
}

/**
 * 邮编是否正确，只验证长度以及是否全为数字
 *
 */
+ (bool)isValidPostCode:(NSString *)code {
    if (code == nil || [code isEqualToString:@""] || code.length != 6 || ![StringUtil isNumString:code]) {
        return false;
    }

    return true;
}

/**
 第一个数字可以不是1
 * 验证输入的手机号是否正确 长度为11位，第一个数字是1
 */
+ (bool)isMobileNumber:(NSString *)mobile {
    bool isMobile = false;

    if (mobile != nil && mobile.length == 11) {
        //        NSString *firstStr = [mobile substringToIndexForSmart:1];
        //        if([StringUtil isNumString:firstStr] && [StringUtil isNumString:mobile] && [firstStr integerValue] == 1)
        //        {
        //            isMobile = true;
        //        }
        // 马惠修改，第一个数字可以不是1
        if ([StringUtil isNumString:mobile]) {
            isMobile = true;
        }
    }

    return isMobile;
}

/**
 * 验证输入的手机号是否正确 长度为11位，第一个数字是1
 */
+ (bool)isMobileNumber2:(NSString *)mobile {
    bool isMobile = false;

    if (mobile != nil && mobile.length == 11) {
        NSString *firstStr = [mobile substringToIndexForSmart:1];

        if ([StringUtil isNumString:firstStr]
                && [StringUtil isNumString:mobile]
                && [firstStr integerValue] == 1) {
            isMobile = true;
        }
    }
    return isMobile;
}

/**
 * 验证输入的手机号是否正确 长度为11位，第一个数字是1
 手机格式验证     移动：134X(0-8)、135-9、150-1、157X(0-79)(TD)、158-9、182 -4、187（3G4G)、188(3G)、147(数据卡）、178（4G）
 联通：130-2、152、155-6、185-6(3G)、145(数据卡）、176（4G）
 电信：180-1(3G)、189(3G)、133、153、（1349卫通） 、177（4G）
 虚拟运营商：170
 新增 166、198、199
 */
+ (bool)isMobileNumberTrue:(NSString *)mobile {
    bool isMobile = false;

    if (mobile != nil && mobile.length == 11) {
        NSString *numberStr = @"^((13[0-9])|(15[^4,\\D])|(17[0-9])|(147)|(145)|(166)|(198)|(199)|(18[0-9]))\\d{8}$";

        NSPredicate *pred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", numberStr];

        isMobile = [pred evaluateWithObject:mobile];
    }
    return isMobile;
}

/**
 * 身份证合法性验证，15位身份证只验证长度
 */
+ (bool)verifyID:(NSString *)idcard {
    if (idcard == nil || [idcard isEqualToString:@""]) {
        return false;
    }

    idcard = [idcard stringByReplacingOccurrencesOfString:@" " withString:@""];

    if (idcard.length == 15) {
        // 15位时只做长度验证
        return true;
    }

    if (idcard.length != 18) {
        return false;
    }

    NSString *verify = [idcard substringFromIndex:17];//idcard.substring(17, 18);
    //  if (verify.equalsIgnoreCase(getVerify(idcard)))
    verify = [verify uppercaseString];
    if ([verify isEqualToString:[StringUtil getVerify:idcard]]) {
        return true;
    }

    return false;
}

/**
 * 计算18位身份证明后一位
 *
 * @return 计算出来的最后一位字串
 */
+ (NSString *)getVerify:(NSString *)eighteen {
    int remain = 0;
    int ai[18];
    int wi[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    char vi[] = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};

    if (eighteen == nil || [eighteen isEqualToString:@""]) {
        return @"";
    }

    if (eighteen.length == 18) {
        eighteen = [eighteen substringToIndexForSmart:17];//eighteen.substring(0, 17);
    }

    if (eighteen.length == 17) {
        int sum = 0;
        for (unsigned int i = 0; i < 17; i++) {
            //            NSString k = eighteen.substring(i, i + 1);
            //            NSRange range = NSMakeRange(i, 1);
            NSString *k = [eighteen substringWithRangeForSmart:NSMakeRange(i, 1)];
            //            if (@"-1".equalsIgnoreCase(k))
            if (k == nil || [k isEqualToString:@""]) {
                return @"";
            }

            ai[i] = [k intValue];//toInt(k);
        }

        for (unsigned int i = 0; i < 17; i++) {
            sum += wi[i] * ai[i];
        }

        remain = sum % 11;
    }

    if (remain >= 0) {
        //      return remain == 2 ? @"x" : vi[remain];//String.valueOf(vi[remain]);
        return [NSString stringWithFormat:@"%c", vi[remain]];
    } else {
        return @"";
    }
}

+ (NSString *)clipString:(NSString *)inputStr length:(NSInteger)length {
    if (inputStr.length == 0) return inputStr;
    if ([self byteLengthOfString:inputStr] <= length) return inputStr;

    int curLength = 0;
    NSMutableString *result = [NSMutableString stringWithCapacity:inputStr.length];
    for (unsigned int i = 0; i < inputStr.length; i++) {
        NSString *string = [inputStr substringWithRange:NSMakeRange(i, 1)];
        NSInteger temp = [self byteLengthOfString:string];
        if (curLength + temp > length) {
            return result;
        }

        curLength += temp;
        [result appendString:string];
    }

    return result;
}

/**
 * 校验yyyyMMdd日期是否合法
 *
 */

+ (bool)isDateRight:(NSString *)date {
    if (date != nil && date.length == 8) {
        int year = -1;
        int month = -1;
        int day = -1;
        bool isLeapYear = false;//闰年
        year = [[date substringToIndexForSmart:4] intValue];
        month = [[date substringWithRangeForSmart:NSMakeRange(4, 2)] intValue];
        day = [[date substringWithRangeForSmart:NSMakeRange(6, 2)] intValue];

        if (year / 4 == 0 && year / 100 != 0) {
            isLeapYear = true;//闰年
        }

        if (year / 400 == 0) {
            isLeapYear = true;//闰年
        }

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                if (day <= 31 && day >= 1) {
                    return true;
                }
                break;
            case 2:
                if (isLeapYear) {
                    if (day <= 29 && day >= 1) {
                        return true;
                    }
                } else {
                    if (day <= 28 && day >= 1) {
                        return true;
                    }
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                if (day <= 30 && day >= 1) {
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    return false;
}

/**
 * 字符串的字节数，一个汉字是2个字节
 * @string 被检测的字符串
 * @return 字节数
 */
+ (NSInteger)byteLengthOfString:(NSString *)string {
    if (string.length == 0) return 0;
    char *problem_char = (char *) [string cStringUsingEncoding:CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000)];
    if (problem_char == NULL || problem_char == nil) {
        return 0;
    }
    return strlen(problem_char);
}

#pragma mark 按符号分割字符串

+ (NSMutableArray *)separatedByDot:(NSString *)str Dot:(NSString *)dot {
    NSMutableArray *separatedList = [[NSMutableArray alloc] init];
    if ([StringUtil emptyOrNull:str] || [StringUtil emptyOrNull:dot]) {
        return separatedList;
    }

    NSArray *arrayByDot = [str componentsSeparatedByString:dot];

    for (unsigned int i = 0; i < [arrayByDot count]; i++) {
        NSString *stringItem = [arrayByDot objectAtIndexForTravel:i];
        stringItem = [stringItem stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        if (![StringUtil emptyOrNull:stringItem]) {
            [separatedList addObject:stringItem];
        }
    }
    return separatedList;
}

+ (bool)isContainUrlStr:(NSString *)url array:(NSArray *)array {
    bool isContain = NO;

    for (unsigned int i = 0; i < [array count]; i++) {
        NSString *urlstr = [array objectAtIndexForTravel:i];

        if (urlstr != nil && [urlstr isEqualToString:url]) {
            isContain = YES;
            break;
        }
    }

    return isContain;
}

+ (NSArray *)stingToArray:(NSString *)string {
    NSMutableArray *array = [NSMutableArray array];
    for (NSInteger i = 0; i < string.length; i++) {
        NSString *s = [string substringWithRangeForSmart:NSMakeRange(i, 1)];
        [array addObject:s];
    }
    return [NSArray arrayWithArray:array];
}

#pragma mark
#pragma mark 添加处理 double, float类型数据到字符串的方法

+ (NSString *)convertRMBDisplayString:(long long)rmbCents {
    return [StringUtil convertRMBDisplayStringByLong:rmbCents];
}

+ (NSString *)convertRMBDisplayStringByLong:(long long)rmbCents {
    if (rmbCents == 0) return @"0";
    if (rmbCents < 0) {
        SLog(@"金额不能为负数。这个提示只是在开发环境提示，给你带来的困扰，敬请谅解！");
        return @"";
    }
    NSDecimalNumber *priceNumber = nil;
    if (rmbCents % 100 == 0) {
        priceNumber = [[NSDecimalNumber alloc] initWithLongLong:rmbCents / 100];
    } else {
        priceNumber = [[NSDecimalNumber alloc] initWithMantissa:rmbCents exponent:-2 isNegative:NO];
    }
    return [priceNumber stringValue];
}

@end
