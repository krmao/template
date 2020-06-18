#import "STRNFileManager.h"
#import "NSString+Md5.h"

//均在cache目录下
static NSString *const DIR_NAME_APPLY = @"apply"; //已经成功应用的目录
static NSString *const DIR_NAME_APPLY_UNZIP = @"apply-%d"; //i解压目录名称,%d为版本号
static NSString *const DIR_NAME_TEMP = @"temp"; //合并差分包临时目录
static NSString *const FILE_NAME_PATCH = @"patch-%d-%d.pacth"; //patch文件,基础版本-目标版本
static NSString *const FILE_NAME_BASE_ZIP = @"base-%d.zip"; //基础压缩包,%d为版本号
static NSString *const FILE_NAME_TEMP_ZIP = @"temp-%d.zip"; //合并压缩包,%d为版本号
static NSString *const FILE_NAME_APPLY_ZIP = @"apply-%d.zip"; //目标压缩包,%d为版本号
static NSString *const FILE_NAME_BASE_UNZIP = @"base-%d"; //基础解压缩目录,%d为版本号

@implementation STRNFileManager

+ (NSString *)fileNameBaseUnzip {
    NSLog(@"%@", [[STRNFileManager cachePath] stringByAppendingPathComponent:@"files/hot-patch/react-native/"]);
    return [[STRNFileManager cachePath] stringByAppendingPathComponent:@"files/hot-patch/react-native/"];
}

+ (BOOL)isFileNameBasePathExit {
    return [[NSFileManager defaultManager] fileExistsAtPath:[STRNFileManager fileNameBaseUnzip]];
}

+ (NSString *)dirNameApply {
    return [[STRNFileManager cachePath] stringByAppendingPathComponent:@"files/hot-patch/react-native-apply/old-bundle.zip"];
}

+ (NSString *)patchFilePath {
    return [[STRNFileManager cachePath] stringByAppendingPathComponent:@"files/hot-patch/react-native-download/"];
}

+ (NSString *)newFilePath {
    return [[STRNFileManager cachePath] stringByAppendingPathComponent:@"files/hot-patch/react-native-apply/"];
}

+ (NSString *)cachePath {
    return [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) lastObject];
}

+ (BOOL)moveItemAtPath:(NSString *)srcPath toPath:(NSString *)dstPath {

    if (![[NSFileManager defaultManager] fileExistsAtPath:dstPath]) {

        [[NSFileManager defaultManager] createDirectoryAtPath:[STRNFileManager dirNameApply] withIntermediateDirectories:YES attributes:nil error:nil];
    }

    return [[NSFileManager defaultManager] moveItemAtPath:srcPath toPath:dstPath error:nil];
}

+ (BOOL)copyItemAtPath:(NSString *)srcPath toPath:(NSString *)dstPath {

    if (![[NSFileManager defaultManager] fileExistsAtPath:dstPath]) {

        [[NSFileManager defaultManager] createDirectoryAtPath:[STRNFileManager dirNameApply] withIntermediateDirectories:YES attributes:nil error:nil];
    }

    return [[NSFileManager defaultManager] copyItemAtPath:srcPath toPath:dstPath error:nil];
}

+ (BOOL)localMd5Path:(NSString *)path CompareToNetMd5:(NSString *)md5 {
    NSString *localMd5 = [path getFileMD5WithPath];

    return [[localMd5 lowercaseString] isEqualToString:[md5 lowercaseString]];
}

@end
