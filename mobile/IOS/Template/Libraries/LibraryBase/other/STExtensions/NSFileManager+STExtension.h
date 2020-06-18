#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

/**
 *  打印错误信息
 *
 *  @param func  函数名，可以使用宏 __FUNCTION__
 *  @param error 错误信息
 */
void printError(const char *func, NSError *error);

@interface NSFileManager (STExtension)

#pragma mark - ---- 文件操作

/**
 *  复制文件，目的地址不存在的时候，会自动创建文件夹
 *
 *  @param from 文件源
 *  @param to   目的地址
 *
 *  @return copy文件是否成功
 */
+ (BOOL)copyFileFrom:(NSString *)from to:(NSString *)to;

/**
 *  剪切文件，目的地址不存在的时候，会自动创建文件夹
 *
 *  @param from 文件源
 *  @param to   目的地址
 *
 *  @return 剪切文件是否成功
 */
+ (BOOL)moveFileFrom:(NSString *)from to:(NSString *)to;

/**
 *  获取文件大小，单位为Byte
 *
 *  @param path 需要计算大小的文件地址
 *
 *  @return 文件大小
 */
+ (long long)fileSize:(NSString *)path;

/**
 *  获取文件上次修改时间
 *
 *  @param path 文件路径
 *
 *  @return 文件上次修改时间
 */
+ (NSDate *)fileModifyTimestamp:(NSString *)path;

#pragma mark - ---- 文件夹操作

/**
 *  创建目录，当改路径所表示的目录存在的时候，直接返回ture
 *
 *  @param path 创建目录所在的目录，支持文件路径，自动忽略文件名，创建其上一级目录
 *
 *  @return 创建目录是否成功
 */
+ (BOOL)createDirectory:(NSString *)path;

/**
 *  文件夹拷贝，会自动创建目的文件夹路径
 *  将from的整个文件夹copy到to目录
 *
 *  @param from 源文件夹地址
 *  @param to   目的文件夹地址
 *
 *  @return 拷贝文件夹是否成功
 */
+ (BOOL)copyDirectoryFrom:(NSString *)from to:(NSString *)to;

/**
 *  剪切文件夹，会自动创建目的文件夹路径
 *  将from的整个文件夹move到to目录
 *
 *  @param from 源文件夹地址
 *  @param to   目的文件夹地址
 *
 *  @return 剪切文件夹是否成功
 */
+ (BOOL)moveDirectoryFrom:(NSString *)from to:(NSString *)to;

/**
 *  获取文件夹中文件总大小，同步计算，文件夹中目录过多时候，请在线程中调用 ，单位为Byte
 *
 *  @param path 需要计算文件总大小的目录
 *
 *  @return 返回计算总文件的大小
 */
+ (long long)directorySize:(NSString *)path;

/**
 *  删除文件夹，如果传入的目录不是文件夹，将不予删除
 *
 *  @param dir 需要删除的目录地址
 *
 *  @return 删除文件夹是否成功
 */
+ (BOOL)removeDirectory:(NSString *)dir;

/**
 *  获取文件夹上次修改时间
 *
 *  @param path 文件夹路径
 *
 *  @return 文件上次修改时间
 */
+ (NSDate *)directoryModifyTimestamp:(NSString *)path;

/**
 *  根据文件名，创建其对应的文件夹
 *
 *  @param path 完整文件名
 *
 *  @return YES|NO 创建成功或者文件夹已经存在|创建失败
 */
+ (BOOL)createDirectoryByFilePathIfNeed:(NSString *)path;

@end

NS_ASSUME_NONNULL_END
