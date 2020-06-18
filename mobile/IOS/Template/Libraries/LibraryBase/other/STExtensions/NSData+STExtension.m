#import "NSData+STExtension.h"
#include <CommonCrypto/CommonDigest.h>
#import "STMacros.h"

@implementation NSData (STExtension)

- (NSString *)MD5 {
    unsigned char digest[CC_MD5_DIGEST_LENGTH], i;
    CC_MD5(self.bytes, (unsigned int) self.length, digest);
    NSMutableString *ms = [NSMutableString string];
    for (i = 0; i < CC_MD5_DIGEST_LENGTH; i++) {
        [ms appendFormat:@"%02X", (int) (digest[i])];
    }
    return [ms copy];
}

- (NSString *)SHA1 {
    NSMutableString *output = [NSMutableString stringWithCapacity:CC_SHA1_DIGEST_LENGTH * 2];
    uint8_t digest[CC_SHA1_DIGEST_LENGTH];
    CC_SHA1(self.bytes, (unsigned int) self.length, digest);
    for (NSInteger i = 0; i < CC_SHA1_DIGEST_LENGTH; i++) {
        [output appendFormat:@"%02X", digest[i]];
    }
    return output;
}

- (NSString *)SHA256 {
    NSMutableString *output = [NSMutableString stringWithCapacity:CC_SHA256_DIGEST_LENGTH * 2];
    uint8_t digest[CC_SHA256_DIGEST_LENGTH];
    CC_SHA256([self bytes], (unsigned int) ([self length]), digest);
    for (NSInteger i = 0; i < CC_SHA256_DIGEST_LENGTH; i++) {
        [output appendFormat:@"%02X", digest[i]];
    }
    return output;
}

- (NSString *)UTF8String {
    NSString *outStr = [[NSString alloc] initWithData:self encoding:NSUTF8StringEncoding];
    return outStr;
}

@end

#pragma mark - ---- NSData的base64扩展

static const char _base64EncodingTable[64] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

static const short _base64DecodingTable[256] = {
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -1, -1, -2, -1, -1, -2, -2,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
        -1, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, 62, -2, -2, -2, 63,
        52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -2, -2, -2, -2, -2, -2,
        -2, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -2, -2, -2, -2, -2,
        -2, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -2, -2, -2, -2, -2,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
        -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2
};

@implementation NSData (Base64)

// Adapted from http://www.cocoadev.com/index.pl?BaseSixtyFour
- (NSString *)base64EncodeToString {
    const uint8_t *input = self.bytes;
    NSInteger length = self.length;

    NSMutableData *data = [NSMutableData dataWithLength:((length + 2) / 3) * 4];
    uint8_t *output = (uint8_t *) data.mutableBytes;

    for (NSInteger i = 0; i < length; i += 3) {
        NSInteger value = 0;
        for (NSInteger j = i; j < (i + 3); j++) {
            value <<= 8;

            if (j < length) {
                value |= (0xFF & input[j]);
            }
        }

        NSInteger index = (i / 3) * 4;
        output[index + 0] = _base64EncodingTable[(value >> 18) & 0x3F];
        output[index + 1] = _base64EncodingTable[(value >> 12) & 0x3F];
        output[index + 2] = (i + 1) < length ? _base64EncodingTable[(value >> 6) & 0x3F] : '=';
        output[index + 3] = (i + 2) < length ? _base64EncodingTable[(value >> 0) & 0x3F] : '=';
    }

    return [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];
}


// Adapted from http://www.cocoadev.com/index.pl?BaseSixtyFour
+ (NSData *)base64DecodeToData:(NSString *)base64String {
    const char *string = [base64String cStringUsingEncoding:NSASCIIStringEncoding];
    NSInteger inputLength = base64String.length;

    if (string == NULL/* || inputLength % 4 != 0*/) {
        return nil;
    }

    while (inputLength > 0 && string[inputLength - 1] == '=') {
        inputLength--;
    }

    NSInteger outputLength = inputLength * 3 / 4;
    NSMutableData *data = [NSMutableData dataWithLength:outputLength];
    uint8_t *output = data.mutableBytes;

    NSInteger inputPoint = 0;
    NSInteger outputPoint = 0;
    while (inputPoint < inputLength) {
        char i0 = string[inputPoint++];
        char i1 = string[inputPoint++];
        char i2 = inputPoint < inputLength ? string[inputPoint++] : 'A'; /* 'A' will decode to \0 */
        char i3 = inputPoint < inputLength ? string[inputPoint++] : 'A';

        output[outputPoint++] = (_base64DecodingTable[i0] << 2) | (_base64DecodingTable[i1] >> 4);
        if (outputPoint < outputLength) {
            output[outputPoint++] = ((_base64DecodingTable[i1] & 0xf) << 4) | (_base64DecodingTable[i2] >> 2);
        }
        if (outputPoint < outputLength) {
            output[outputPoint++] = ((_base64DecodingTable[i2] & 0x3) << 6) | _base64DecodingTable[i3];
        }
    }

    return data;
}

@end

@implementation NSData (SmartForException)

- (NSData *)subdataFromIndex:(int)from {
    if (from >= 0 && from <= self.length) {
        return [self subdataWithRange:NSMakeRange(from, self.length - from)];
    } else {
        SLog(@"from:%d subRage(%d,%ld)", from, from, (unsigned long) self.length - from);
        return nil;
    }
}

- (NSData *)subdataToIndex:(int)to {
    if (to >= 0 && to <= self.length) {
        return [self subdataWithRange:NSMakeRange(0, to)];
    } else {
        SLog(@"to:%d subRage(%d,%d)", to, 0, to);
        return nil;
    }
}
@end
