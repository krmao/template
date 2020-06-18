#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSString (Utils)

/**
 *  @brief 判断当前字符串中是否包含子串
 *
 *  @param string 需要检查的子串
 *
 *  @return 如果当前字符串中包含子串，返回true，否则false
 */
- (BOOL)containsString:(NSString *)string;

/**
 *  判断当前字符串中是否包含子串,不区分大小写
 *
 *  @param string  需要检查的子串
 *
 *  @return 如果当前字符串中包含子串，返回true，否则false
 */
- (BOOL)containsStringIgnoreCase:(NSString *)string;

/**
 *  正则表达式匹配
 *
 *  @param regexp  需要匹配的正则表达式
 *
 *  @return 匹配成功，返回true，否则false
 */
- (BOOL)match:(NSString *)regexp;


/**
 *  生成UUID
 *
 *  @return 生成的UUID字符串
 */
+ (NSString *)UUIDString;

/**
 *  删除首尾的空白字符串
 *
 *  @return 首尾字符串被删除的字符串
 */
- (NSString *)trimBlank;


- (BOOL)isNumber;

/**
 *  包装SDK的substringFromIndex:
 */
- (NSString *)subStringFromIndex:(NSUInteger)from;

/**
 *  包装SDK的substringToIndex:
 */
- (NSString *)subStringToIndex:(NSUInteger)to;

/**
 *  包装SDK的substringWithRange:
 */
- (NSString *)subStringWithRange:(NSRange)range;

/**
 *  判断字符串是否相等，不区分大小写
 *
 *  @param cmpString 需要比较的字符串
 *
 *  @return 相等返回true，否则返回false
 */
- (BOOL)equalIgnoreCase:(NSString *)cmpString;

/**
 *  消除字符串中的空格和换行
 *
 *  @return 消除了的空格和换行的字符串
 */
- (NSString *)trimmedString;

- (BOOL)isEmpty;

/**
 *  jsonString to NSDictionary
 */
- (NSDictionary *)dictionaryValue;

/**
 *  按照sql规则转义字符串
 *
 *  @return 按照sql规则转义字符串
 */
- (NSString *)escapeBySql;

@end

#pragma mark - String URL相关

@interface NSString (URL)

/**
 *  字符串转换成URL对象，#会在内部处理
 *
 *  @return 转换成的URL对象
 */
- (NSURL *)toURL;

/**
 *  对URL做UTF8编码
 *
 *  @return UTF8编码之后的URL字符串
 */
- (NSString *)URLEncode;

/**
 *  对NSString做URLEncode编码
 *
 *  @return NSString的URLEncode字符串
 */
- (NSString *)stringURLEncoded;

/**
 *  对NSString做URLDecoded编码
 *
 *  @return NSString的URLDecoded字符串
 */
- (NSString *)stringURLDecode;

/**
 *  对NSString做URIEncoded编码
 *
 *  @return NSString的URIEncoded字符串
 */
- (NSString *)encodeAsURIComponent;

/**
 *  对URL做UTF8解码
 *
 *  @return UTF8解码之后的URL字符串
 */
- (NSString *)URLDecode;

@end

#pragma mark - String Hash相关

@interface NSString (Hash)


/**
 * @brief 计算NSString的MD5哈希函数
 *
 * @return 返回NSString的MD5哈希值，长度32，大写
 */
- (NSString *)MD5;

/**
 * @brief 计算NSString的SHA1哈希函数
 *
 * @return 返回NSString的SHA1哈希值，长度40，大写
 */
- (NSString *)SHA1;

/**
 * @brief 计算NSString的SHA256哈希函数
 *
 * @return 返回NSString的SHA256哈希值，长度64，大写
 */
- (NSString *)SHA256;

@end

#pragma mark - String Base64 Encode/Decode

@interface NSString (Base64)

/**
 *  将字符串进行base64 编码
 *
 *  @return base64编码之后的字符串
 */
- (NSString *)Base64EncodeToString;

/**
 *  字符串base64解码成字符串
 */
- (NSString *)Base64DecodeToString;

/**
 *  将base64字符串解码成Data
 *
 *  @return base64字符串解码之后的Data
 */
- (NSData *)Base64DecodeToData;

@end

@interface NSString (File_UTF8)

- (BOOL)appendToFile:(NSString *)path;

- (BOOL)writeToFile:(NSString *)path;

@end


@interface NSString (SmartForException)

- (NSString *)substringFromIndexForSmart:(NSUInteger)from;

- (NSString *)substringToIndexForSmart:(NSUInteger)to;

- (NSString *)substringWithRangeForSmart:(NSRange)range;

- (NSString *)stringByAppendingStringForSmart:(NSString *)aString;

- (NSString *)stringByReplacingOccurrencesOfStringForSmart:(NSString *)target
                                                withString:(NSString *)replacement
                                                   options:(NSStringCompareOptions)options
                                                     range:(NSRange)searchRange;

- (NSString *)stringByReplacingCharactersInRangeForSmart:(NSRange)range
                                              withString:(NSString *)replacement;

@end


@interface NSMutableString (SmartForException)

- (void)replaceCharactersInRangeForSmart:(NSRange)range withString:(NSString *)aString;

- (void)insertStringForSmart:(NSString *)aString atIndex:(NSUInteger)loc;

- (void)deleteCharactersInRangeForSmart:(NSRange)range;

- (void)appendStringForSmart:(NSString *)aString;

- (void)setStringForSmart:(NSString *)aString;

- (NSUInteger)replaceOccurrencesOfStringForSmart:(NSString *)target withString:(NSString *)replacement options:(NSStringCompareOptions)options range:(NSRange)searchRange;

@end


@interface NSObject (CTClass)
/** 当前实例是否为指定class或其子类 */
- (BOOL)isKindOfClassByClassName:(NSString *)name;

/** a isKindOf b */
+ (BOOL)className:(NSString *)a kindOfClassName:(NSString *)b;

/** 当前实例是否为指定Class */
- (BOOL)isMemberOfClassByClassName:(NSString *)name;

/* a isMemberOfClass b */
+ (BOOL)className:(NSString *)a memberOfClassName:(NSString *)b;

/* 获取当前实例的类名 */
- (NSString *)className;

/** 响应SEL */
- (BOOL)respondsToSelectorByName:(NSString *)name;

@end

NS_ASSUME_NONNULL_END
