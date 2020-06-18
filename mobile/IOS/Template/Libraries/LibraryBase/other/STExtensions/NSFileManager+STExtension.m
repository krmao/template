#import "NSFileManager+STExtension.h"
#import "STMacros.h"
#include <sys/stat.h>

void printError(const char *func, NSError *error) {
    if (error != NULL) {
        SLog(@"func:%s, error:%@", func, error);
    }
}

@implementation NSFileManager (STExtension)

#pragma mark - ----

BOOL internal_createDirectoryForFilePath(NSString *path, BOOL isOverwrite) {

    NSString *lastComponent = [path lastPathComponent];
    NSString *toFolder = path;
    if (lastComponent.length > 0) {
        toFolder = [path substringWithRange:NSMakeRange(0, [path length] - [lastComponent length])];
    }

    return internal_createDirectory(toFolder, isOverwrite);
}

BOOL internal_createDirectory(NSString *dirPath, BOOL isOverwrite) {
    if (dirPath.length == 0) {
        return NO;
    }

    NSFileManager *fileMng = [NSFileManager defaultManager];

    BOOL ret = YES;
    NSError *error = NULL;
    BOOL isDir = YES;
    if ([fileMng fileExistsAtPath:dirPath isDirectory:&isDir]) {
        if (!isDir) {
            remove([dirPath UTF8String]);
        }
        if (isOverwrite) {
            ret = [fileMng removeItemAtPath:dirPath error:&error];
            ret &= (error == NULL);
            printError(__FUNCTION__, error);
        }
    }

    if (ret) {
        ret = [fileMng createDirectoryAtPath:dirPath
                 withIntermediateDirectories:YES
                                  attributes:nil
                                       error:&error];
        ret &= (error == NULL);
        printError(__FUNCTION__, error);
    }

    return ret;

}

+ (BOOL)createDirectory:(NSString *)path {
    return internal_createDirectory(path, NO);
}

#pragma mark - ----

BOOL internal_copyFile(NSString *from, NSString *to, BOOL isRemove) {
    if (access([from UTF8String], 0) != 0 || to.length == 0) {
        return NO;
    }

    BOOL ret = internal_createDirectoryForFilePath(to, NO);
    if (ret) {
        NSError *error = NULL;
        remove([to UTF8String]);

        if (isRemove) {
            remove([to UTF8String]);
            ret = [[NSFileManager defaultManager] moveItemAtPath:from toPath:to error:&error];
        } else {
            ret = [[NSFileManager defaultManager] copyItemAtPath:from toPath:to error:&error];
        }
        ret &= (error == NULL);
        printError(__FUNCTION__, error);
    }

    return ret;
}

+ (BOOL)copyFileFrom:(NSString *)from to:(NSString *)to {
    return internal_copyFile(from, to, NO);
}

+ (BOOL)moveFileFrom:(NSString *)from to:(NSString *)to {
    return internal_copyFile(from, to, YES);
}

#pragma mark - ----

BOOL internal_copyDirectory(NSString *from, NSString *to, BOOL isRemove) {
    if (access([from UTF8String], 0) != 0 || to.length == 0) {
        return NO;
    }

    NSFileManager *fileMng = [NSFileManager defaultManager];
    NSString *fromDirName = [from lastPathComponent];
    to = [to stringByAppendingPathComponent:fromDirName];

    BOOL ret = [NSFileManager createDirectory:to];
    if (ret) {
        NSError *error = NULL;
        NSArray *allFiles = [fileMng contentsOfDirectoryAtPath:from error:&error];
        if (error) {
            ret = NO;
        } else {

            for (NSString *fileName in allFiles) {
                NSString *fromPath = [from stringByAppendingPathComponent:fileName];
                NSString *toPath = [to stringByAppendingPathComponent:fileName];
                BOOL tmpRet = NO;

                if (isRemove) {
                    remove([toPath UTF8String]);
                    tmpRet = [fileMng moveItemAtPath:fromPath toPath:toPath error:&error];
                } else {
                    tmpRet = [fileMng copyItemAtPath:fromPath toPath:toPath error:&error];
                }

                if (error && !tmpRet) {
                    ret = NO;
                    break;
                }
            }
        }
    }

    return ret;
}

+ (BOOL)copyDirectoryFrom:(NSString *)from to:(NSString *)to {
    return internal_copyDirectory(from, to, NO);
}

+ (BOOL)moveDirectoryFrom:(NSString *)from to:(NSString *)to {
    return internal_copyDirectory(from, to, YES);
}

#pragma mark - ----

+ (long long)fileSize:(NSString *)path {
    NSFileManager *fileMng = [NSFileManager defaultManager];
    NSError *error = NULL;
    NSDictionary *attribute = [fileMng attributesOfItemAtPath:path error:&error];
    long long fileLen = 0;
    if (error) {
        printError(__FUNCTION__, error);
    } else {
        fileLen = [[attribute objectForKey:NSFileSize] longLongValue];
    }

    return fileLen;
}

+ (NSDate *)fileModifyTimestamp:(NSString *)path {
    struct stat st;
    if (lstat([path cStringUsingEncoding:NSUTF8StringEncoding], &st) == 0) {
        return [NSDate dateWithTimeIntervalSince1970:st.st_mtimespec.tv_sec];
    }
    return 0;
}

long long internal_fileSize(NSString *filePath) {
    struct stat st;
    if (lstat([filePath cStringUsingEncoding:NSUTF8StringEncoding], &st) == 0) {
        return st.st_size;
    }
    return 0;
}

+ (long long)directorySize:(NSString *)dirPath {
    NSFileManager *manager = [NSFileManager defaultManager];
    if (![manager fileExistsAtPath:dirPath]) {
        return 0;
    }

    NSEnumerator *childFilesEnumerator = [[manager subpathsAtPath:dirPath] objectEnumerator];
    NSString *fileName = NULL;

    long long dirFileSize = 0;
    while ((fileName = [childFilesEnumerator nextObject]) != nil) {
        NSString *fileAbsolutePath = [dirPath stringByAppendingPathComponent:fileName];
        dirFileSize += internal_fileSize(fileAbsolutePath);
    }

    return dirFileSize;
}

+ (NSDate *)directoryModifyTimestamp:(NSString *)path {
    return [NSFileManager fileModifyTimestamp:path];
}

#pragma mark - ----

+ (BOOL)removeDirectory:(NSString *)dir {
    if (dir.length == 0) {
        return NO;
    }

    NSFileManager *fileMng = [NSFileManager defaultManager];
    BOOL isDirectory;
    BOOL isExist = [fileMng fileExistsAtPath:dir isDirectory:&isDirectory];
    if (isExist && isDirectory) {
        NSError *error = NULL;
        BOOL ret = [fileMng removeItemAtPath:dir error:&error];
        ret &= (error == NULL);
        return ret;
    }

    return YES;
}


+ (BOOL)createDirectoryByFilePathIfNeed:(NSString *)path {
    if (path.length == 0) {
        return NO;
    }

    BOOL isCreateSuccess = NO;

    NSFileManager *fm = [NSFileManager defaultManager];
    BOOL isDir = NO;
    BOOL isExist = [fm fileExistsAtPath:path isDirectory:&isDir];
    if (isExist) {
        isCreateSuccess = isDir;
    } else {
        NSString *directory = nil;

        if ([path hasSuffix:@"/"]) {
            directory = path;
        } else {
            directory = [path stringByDeletingLastPathComponent];
        }

        isCreateSuccess = [fm createDirectoryAtPath:directory
                        withIntermediateDirectories:YES
                                         attributes:nil
                                              error:nil];
        SLog(@"directory not exist create ret==%d", isCreateSuccess);
    }

    return isCreateSuccess;
}

@end
