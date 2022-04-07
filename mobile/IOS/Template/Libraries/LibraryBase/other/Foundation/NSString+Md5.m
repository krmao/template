#import "NSString+Md5.h"
#import <CommonCrypto/CommonCrypto.h>

#define FileHashDefaultChunkSizeForReadingData 1024*8

@implementation NSString (Md5)

- (NSString *)getFileMD5WithPath {

    NSFileManager *fileManager = [NSFileManager defaultManager];

    if ([fileManager fileExistsAtPath:self isDirectory:nil]) {
        NSData *data = [NSData dataWithContentsOfFile:self];
        unsigned char digest[CC_MD5_DIGEST_LENGTH];
        CC_MD5(data.bytes, (CC_LONG) data.length, digest);

        NSMutableString *output = [NSMutableString stringWithCapacity:CC_MD5_DIGEST_LENGTH * 2];

        for (int i = 0; i < CC_MD5_DIGEST_LENGTH; i++) {
            [output appendFormat:@"%02x", digest[i]];
        }

        return output;
    } else {
        return @"";
    }
}

@end

