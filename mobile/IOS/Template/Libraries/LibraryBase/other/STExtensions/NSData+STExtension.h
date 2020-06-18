#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSData (STExtension)

/**
 * @brief NSData的MD5哈希函数
 *
 * @return 返回data的MD5哈希值，长度32，大写
 */
- (NSString *)MD5;

/**
 *  @brief NSData的SHA1哈希函数
 *
 *  @return 返回data的SHA1哈希值，长度40，大写
 */
- (NSString *)SHA1;

/**
 *  NSData的SHA256哈希函数
 *
 *  @return 返回data的SHA256哈希值，长度64，大写
 */
- (NSString *)SHA256;

/**
 *  Data转换成UTF8字符串
 *
 *  @return 转换成UTF8字符串的Data
 */
- (NSString *)UTF8String;

@end

#pragma mark - ---- NSData的base64扩展

@interface NSData (Base64)

/**
 *  Data进行base64 编码
 *
 *  @return 返回经过编码的base4字符串
 */
- (NSString *)base64EncodeToString;

/**
 *  base64字符串解码成Data
 *
 *  @param base64String 待解码的字符串
 *
 *  @return base64字符串经过解码之后的Data
 */
+ (NSData *)base64DecodeToData:(NSString *)base64String;

@end

@interface NSData (SmartForException)

- (NSData *)subdataFromIndex:(int)from;

- (NSData *)subdataToIndex:(int)to;

@end

NS_ASSUME_NONNULL_END
