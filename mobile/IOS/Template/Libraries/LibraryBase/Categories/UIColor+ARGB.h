#import <Foundation/Foundation.h>
#import "UIColor.h"

#pragma clang diagnostic push
#pragma ide diagnostic ignored "UnusedMethodInspection"

@interface UIColor (ARGB)

+ (UIColor *)randomColor;

- (instancetype)initWithRGB:(UInt32)rgb;

- (instancetype)initWithRRGGBB:(UInt32)rrggbb;

- (instancetype)initWithARGB:(UInt32)argb;

- (instancetype)initWithAARRGGBB:(UInt32)aarrggbb;

- (instancetype)initWithHex:(NSString *)hexString;

- (UIImage *)toImage:(CGSize)size;

@end

#pragma clang diagnostic pop