#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STRNFileManager : NSObject

/**
 首次解压缩位置
 
 @return path
 */
+ (NSString *)fileNameBaseUnzip;

/**
 文件是否存在

 @return return value description
 */
+ (BOOL)isFileNameBasePathExit;

/**
 已经成功应用的目录
 
 @return path
 */
+ (NSString *)dirNameApply;

/**
 增量包路径

 @return return value description
 */
+ (NSString *)patchFilePath;

/**
 合成包路径

 @return return value description
 */
+ (NSString *)newFilePath;

/**
 移动文件

 @param srcPath srcPath description
 @param dstPath dstPath description
 @return return value description
 */
+ (BOOL)moveItemAtPath:(NSString *)srcPath toPath:(NSString *)dstPath;

/**
 copy文件

 @param srcPath srcPath description
 @param dstPath dstPath description
 @return return value description
 */
+ (BOOL)copyItemAtPath:(NSString *)srcPath toPath:(NSString *)dstPath;

/**
 本地压缩包md5与远端对比

 @param path 本地压缩包路径
 @param md5 服务器MD5值
 @return bool
 */
+ (BOOL)localMd5Path:(NSString *)path CompareToNetMd5:(NSString *)md5;

@end

NS_ASSUME_NONNULL_END
