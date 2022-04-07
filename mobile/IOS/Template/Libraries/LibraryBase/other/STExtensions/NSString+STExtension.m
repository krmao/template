#import "NSString+STExtension.h"
#import "NSData+STExtension.h"
#import "STMacros.h"

#pragma mark - String Utils

@implementation NSString (Utils)

/**
 *  @brief 判断当前字符串中是否包含子串
 *
 *  @param string 需要检查的子串
 *
 *  @return 如果当前字符串中包含子串，返回true，否则false
 */
- (BOOL)containsString:(NSString *)string {
    if (string.length == 0) {
        return NO;
    }
    return !NSEqualRanges([self rangeOfString:string], NSMakeRange(NSNotFound, 0));
}

- (BOOL)containsStringIgnoreCase:(NSString *)string {
    return [[self uppercaseString] containsString:[string uppercaseString]];
}

- (BOOL)match:(NSString *)regexp {
    if (regexp.length == 0) {
        return NO;
    }


    NSPredicate *regExPredicate = [NSPredicate predicateWithFormat:@"SELF matches %@", regexp];
    BOOL isMatch = [regExPredicate evaluateWithObject:self];
    return isMatch;
}

/**
 *  生成UUID
 *
 *  @return 生成的UUID字符串
 */
+ (NSString *)UUIDString {
    CFUUIDRef uuid = CFUUIDCreate(NULL);
    CFStringRef string = CFUUIDCreateString(NULL, uuid);
    CFRelease(uuid);
    return (__bridge_transfer NSString *) string;
}

- (NSString *)trimBlank {
    return [self stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

- (BOOL)isNumber {
    int dotCount = 0;
    BOOL isLeagalNum = YES;

    for (NSUInteger i = 0; i < self.length; i++) {
        unichar cha = [self characterAtIndex:i];
        if (cha == '.') {
            dotCount++;
            if (dotCount >= 2) {
                isLeagalNum = NO;
                break;
            }
        } else if (cha >= '0' && cha <= '9') {;
        } else {
            isLeagalNum = NO;
            break;
        }

    }

    return isLeagalNum;
}

/**
 *  包装SDK的subStringFromIndex:
 *
 *  @param from
 *
 *  @return 返回sub的字符串
 */
- (NSString *)subStringFromIndex:(NSUInteger)from {
    if (from <= self.length) {
        return [self substringFromIndex:from];
    } else {
        NSAssert(YES, @"Error:length:%lu from:%lu", (unsigned long) self.length, (unsigned long) from);
        return nil;
    }
}

/**
 *  包装SDK的substringToIndex:
 *
 *  @param to
 *
 *  @return 返回sub的字符串
 */
- (NSString *)subStringToIndex:(NSUInteger)to {
    if (to <= self.length) {
        return [self substringToIndex:to];
    } else {
        NSAssert(YES, @"Error:length:%lu to:%ld", (unsigned long) self.length, (unsigned long) to);
        return nil;
    }
}

/**
 *  包装SDK的subStringWithRange:
 *
 *  @param range
 *
 *  @return 返回sub的字符串
 */
- (NSString *)subStringWithRange:(NSRange)range {
    if (range.location + range.length <= self.length) {
        return [self substringWithRange:range];
    } else {
        NSAssert(YES, @"Error:length:%lu Range(%ld,%ld)", (unsigned long) self.length, (unsigned long) range.location, (unsigned long) range.length);
        return nil;
    }
}

- (BOOL)equalIgnoreCase:(NSString *)cmpString {
    return [[self lowercaseString] isEqualToString:[cmpString lowercaseString]];
}

- (NSString *)trimmedString {
    return [self stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

- (BOOL)isEmpty {
    return self == nil || self.length == 0 || [self trimmedString].length == 0;
}

/**
 * jsonString 转换 NSDictionary
 */
- (NSDictionary *)dictionaryValue {
    NSError *errorJson;
    NSDictionary *jsonDict = [NSJSONSerialization JSONObjectWithData:[self dataUsingEncoding:NSUTF8StringEncoding] options:kNilOptions error:&errorJson];
    if (errorJson != nil) {
#ifdef DEBUG
        NSLog(@"fail to get dictioanry from JSON: %@, error: %@", self, errorJson);
#endif
    }
    return jsonDict;
}

- (NSString *)escapeBySql {
    if (self.length == 0) {
        return @"";
    }
    NSString *str = nil;
    // ' --> ''
    str = [self stringByReplacingOccurrencesOfString:@"'" withString:@"''"];

    // " --> ""
    str = [str stringByReplacingOccurrencesOfString:@"\"" withString:@"\"\""];

    // \ -->  (remove backslashes)
    str = [str stringByReplacingOccurrencesOfString:@"\\\\" withString:@""];

    return str;
}

@end


#pragma mark - String URL相关

@implementation NSString (URL)

/**
 *  URL参数转义，空格会换成+
 *
 *  @return 转义之后的URL字符串
 */
- (NSString *)escapingForURLQuery {
    NSString *result = self;

    static CFStringRef leaveAlone = CFSTR(" ");
    static CFStringRef toEscape = CFSTR("\n\r:/=,!$&'()*+;[]@#?%");

    CFStringRef escapedStr = CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault, (__bridge CFStringRef) self, leaveAlone,
            toEscape, kCFStringEncodingUTF8);

    if (escapedStr) {
        NSMutableString *mutable = [NSMutableString stringWithString:(__bridge NSString *) escapedStr];
        CFRelease(escapedStr);

        [mutable replaceOccurrencesOfString:@" " withString:@"+" options:0 range:NSMakeRange(0, [mutable length])];
        result = mutable;
    }
    return result;
}

/**
 *  转义之后的URL参数还原，+会被还原成空格
 *
 *  @return 还原之后URL参数
 */
- (NSString *)unEscapingFromURLQuery {
    NSString *deplussed = [self stringByReplacingOccurrencesOfString:@"+" withString:@" "];
    return [deplussed stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
}

/**
 *  字符串转换成URL对象，#会在内部处理
 *
 *  @return 转换成的URL对象
 */
- (NSURL *)toURL {
    if (self.length == 0) {
        return NULL;
    }

    NSString *tmpUrl = self;

    tmpUrl = [tmpUrl stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSRange sRange = [tmpUrl rangeOfString:@"#"];

    NSString *retUrlStr = tmpUrl;

    if (sRange.location != NSNotFound) {
        NSString *prefix = [[tmpUrl substringToIndex:sRange.location] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        NSURL *url = [NSURL URLWithString:prefix];

        NSString *sufix = [[tmpUrl substringFromIndex:sRange.location + 1] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        retUrlStr = [[url absoluteString] stringByAppendingFormat:@"#%@", sufix];
    } else {
        retUrlStr = [retUrlStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    }

    return [NSURL URLWithString:retUrlStr];

}

/**
 *  对URL做UTF8编码
 *
 *  @return UTF8编码之后的URL字符串
 */
- (NSString *)URLEncode {
    return [self stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
}

/**
 *  对URL做UTF8解码
 *
 *  @return UTF8解码之后的URL字符串
 */
- (NSString *)URLDecode {
    return [self stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
}


/**
 *  对NSString做URLEncode编码
 *
 *  @return NSString的URLEncode字符串
 */
- (NSString *)stringURLEncoded {
    NSString *result = (NSString *) CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault,
            (CFStringRef) self,
            NULL,
            CFSTR("!*'();:@&=+$,/?%#[]"),
            kCFStringEncodingUTF8));
    return result;
}

/**
 *  对NSString做URLDecoded编码
 *
 *  @return NSString的URLDecoded字符串
 */
- (NSString *)stringURLDecode {
    NSString *result = (NSString *) CFBridgingRelease(CFURLCreateStringByReplacingPercentEscapesUsingEncoding(kCFAllocatorDefault,
            (CFStringRef) self,
            CFSTR(""),
            kCFStringEncodingUTF8));
    return result;

}

/**
 *  对NSString做URIEncoded编码
 *
 *  @return NSString的URIEncoded字符串
 */
- (NSString *)encodeAsURIComponent {
    const char *p = [self UTF8String];
    NSMutableString *result = [NSMutableString string];
    if (p != NULL) {
        for (; *p != 0; p++) {
            unsigned char c = *p;
            if (('0' <= c && c <= '9') || ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z') || c == '-' || c == '_') {
                [result appendFormat:@"%c", c];
            } else {
                [result appendFormat:@"%%%02X", c];
            }

        }
    }
    return result;
}

@end

#pragma mark - String Hash相关

@implementation NSString (Hash)

/**
 * @brief 计算NSString的MD5哈希函数
 *
 * @return 返回NSString的MD5哈希值，长度32，大写
 */
- (NSString *)MD5 {
    const char *cstr = [self cStringUsingEncoding:NSUTF8StringEncoding];
    NSData *data = [NSData dataWithBytes:cstr length:strlen(cstr)];
    return [data MD5];
}

/**
 * @brief 计算NSString的SHA1哈希函数
 *
 * @return 返回NSString的SHA1哈希值，长度40，大写
 */
- (NSString *)SHA1 {
    const char *cstr = [self cStringUsingEncoding:NSUTF8StringEncoding];
    NSData *data = [NSData dataWithBytes:cstr length:strlen(cstr)];
    return [data SHA1];
}

/**
 * @brief 计算NSString的SHA256哈希函数
 *
 * @return 返回NSString的SHA256哈希值，长度64，大写
 */
- (NSString *)SHA256 {
    const char *cstr = [self cStringUsingEncoding:NSUTF8StringEncoding];
    NSData *data = [NSData dataWithBytes:cstr length:strlen(cstr)];
    return [data SHA256];
}


@end

#pragma mark - String Base64 Encode/Decode

@implementation NSString (Base64)

/**
 *  将字符串进行base64 编码
 *
 *  @return base64编码之后的字符串
 */
- (NSString *)Base64EncodeToString {
    if ([self length] == 0) {
        return nil;
    }

    return [[self dataUsingEncoding:NSUTF8StringEncoding] base64EncodeToString];
}

/**
 *  字符串base64解码成字符串
 *
 *  @param base64String 需要解码的base64字符串
 *
 *  @return 解码之后的base64字符串
 */
- (NSString *)Base64DecodeToString {
    NSData *decodeData = [NSData base64DecodeToData:self];
    return [[NSString alloc] initWithData:decodeData encoding:NSUTF8StringEncoding];
}

/**
 *  将base64字符串解码成Data
 *
 *  @return base64字符串解码之后的Data
 */
- (NSData *)Base64DecodeToData {
    return [NSData base64DecodeToData:self];
}

@end

#pragma mark - String File

@implementation NSString (File_UTF8)
- (BOOL)appendToFile:(NSString *)path {
    if (path.length == 0 || self.length == 0) {
        return NO;
    }

    FILE *fp = fopen([path UTF8String], "a");
    if (fp) {
        fwrite([self UTF8String], self.length, 1, fp);
        BOOL retFlag = (0 == ferror(fp));
        fclose(fp);
        return retFlag;
    }

    return NO;
}

- (BOOL)writeToFile:(NSString *)path {
    if (path.length == 0) {
        return NO;
    }

    NSError *error = NULL;

    BOOL ret = [self writeToFile:path atomically:YES encoding:NSUTF8StringEncoding error:&error];
    if (error) {
        ret = NO;
    }

    return ret;
}

@end

@implementation NSString (SmartForException)

- (NSString *)substringFromIndexForSmart:(NSUInteger)from {
    if (from <= self.length) {
        return [self substringFromIndex:from];
    } else {
        SLog(@"length:%lu from:%lu", (unsigned long) self.length, (unsigned long) from);
        return nil;
    }
}

- (NSString *)substringToIndexForSmart:(NSUInteger)to {
    if (to <= self.length) {
        return [self substringToIndex:to];
    } else {
        SLog(@"length:%lu to:%lu", (unsigned long) self.length, (unsigned long) to);
        return nil;
    }
}

- (NSString *)substringWithRangeForSmart:(NSRange)range {
    if (range.location + range.length <= self.length) {
        return [self substringWithRange:range];
    } else {
        SLog(@"length:%lu Range(%lu,%lu)", (unsigned long) self.length, (unsigned long) range.location, (unsigned long) range.length);
        return nil;
    }
}

- (NSString *)stringByAppendingStringForSmart:(NSString *)aString {
    if (nil != aString) {
        return [self stringByAppendingString:aString];
    } else {
        SLog(@"string:%@", aString);
        return nil;
    }
}

- (NSString *)stringByPaddingToLengthForIndexForSmart:(NSUInteger)newLength withString:(NSString *)padString startingAtIndex:(NSUInteger)padIndex {
    if (padIndex == 0 && nil != padString && ![padString isEqualToString:@""]) {
        return [self stringByPaddingToLength:newLength withString:padString startingAtIndex:padIndex];
    } else {
        SLog(@"newLength:%lu padString:%@ padIndex:%lu", (unsigned long) newLength, padString, (unsigned long) padIndex);
        return nil;
    }
}

- (NSString *)stringByReplacingOccurrencesOfStringForSmart:(NSString *)target withString:(NSString *)replacement options:(NSStringCompareOptions)options range:(NSRange)searchRange {
    if (searchRange.location + searchRange.length <= self.length) {
        return [self stringByReplacingOccurrencesOfString:target withString:replacement options:options range:searchRange];
    } else {
        SLog(@"length:%lu target:%@ replacement:%@ searchRange(%lu,%lu)", (unsigned long) self.length, target, replacement, (unsigned long) searchRange.location, (unsigned long) searchRange.length);
        return nil;
    }
}

- (NSString *)stringByReplacingCharactersInRangeForSmart:(NSRange)range withString:(NSString *)replacement {
    if (range.location + range.length <= self.length) {
        return [self stringByReplacingCharactersInRange:range withString:replacement];
    } else {
        SLog(@"length:%lu replacement:%@ range(%lu,%lu)", (unsigned long) self.length, replacement, (unsigned long) range.location, (unsigned long) range.length);
        return nil;
    }
}
@end

@implementation NSMutableString (SmartForException)

- (void)replaceCharactersInRangeForSmart:(NSRange)range withString:(NSString *)aString {
    if (nil != aString && range.location + range.length <= self.length) {
        [self replaceCharactersInRange:range withString:aString];
    } else {
        SLog(@"length:%lu aString:%@ range(%lu,%lu)", (unsigned long) self.length, aString, (unsigned long) range.location, (unsigned long) range.length);
    }
}

- (void)insertStringForSmart:(NSString *)aString atIndex:(NSUInteger)loc {
    if (nil != aString && loc <= self.length) {
        [self insertString:aString atIndex:loc];
    } else {
        SLog(@"length:%lu aString:%@ loc:%lu", (unsigned long) self.length, aString, (unsigned long) loc);
    }
}

- (void)deleteCharactersInRangeForSmart:(NSRange)range {
    if (range.location + range.length <= self.length) {
        [self deleteCharactersInRange:range];
    } else {
        SLog(@"length:%lu range(%lu,%lu)", (unsigned long) (unsigned long) self.length, (unsigned long) range.location, (unsigned long) range.length);
    }
}

- (void)appendStringForSmart:(NSString *)aString {
    if (nil != aString) {
        [self appendString:aString];
    } else {
        SLog(@"aString:%@", aString);
    }
}

- (void)setStringForSmart:(NSString *)aString {
    if (nil != aString) {
        [self setString:aString];
    } else {
        SLog(@"aString:%@", aString);
    }
}

- (NSUInteger)replaceOccurrencesOfStringForSmart:(NSString *)target withString:(NSString *)replacement options:(NSStringCompareOptions)options range:(NSRange)searchRange {
    if (nil != target && nil != replacement && searchRange.location + searchRange.length <= self.length) {
        return [self replaceOccurrencesOfString:target withString:replacement options:options range:searchRange];
    } else {
        SLog(@"length:%lu target:%@ replacement:%@ searchRage(%lu,%lu)", (unsigned long) self.length, target, replacement, (unsigned long) searchRange.location, (unsigned long) searchRange.length);
        return 0;
    }
}
@end


@implementation NSObject (CTClass)
- (BOOL)isKindOfClassByClassName:(NSString *)name {
    Class c = NSClassFromString(name);
    return c && [self isKindOfClass:c];
}

+ (BOOL)className:(NSString *)a kindOfClassName:(NSString *)b {
    Class aClass = NSClassFromString(a);
    if (!aClass) {
        return NO;
    }
    Class bClass = NSClassFromString(b);
    if (!bClass) {
        return NO;
    }
    id aInstance = [[aClass alloc] init];
    return aInstance && [aInstance isKindOfClass:bClass];
}

/** 当前实例是否为指定Class */
- (BOOL)isMemberOfClassByClassName:(NSString *)name {
    Class c = NSClassFromString(name);
    return c && [self isMemberOfClass:c];
}

/* a isMemberOfClass b */
+ (BOOL)className:(NSString *)a memberOfClassName:(NSString *)b {

    Class aClass = NSClassFromString(a);
    if (!aClass) {
        return NO;
    }
    Class bClass = NSClassFromString(b);
    if (!bClass) {
        return NO;
    }
    id aInstance = [[aClass alloc] init];

    return aInstance && [aInstance isMemberOfClass:bClass];
}

- (NSString *)className {
    return NSStringFromClass(self.class);
}

- (BOOL)respondsToSelectorByName:(NSString *)name {
    SEL sel = NSSelectorFromString(name);
    return [self respondsToSelector:sel];
}
@end
