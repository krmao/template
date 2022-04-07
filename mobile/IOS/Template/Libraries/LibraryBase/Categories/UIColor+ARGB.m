#import "UIColor+ARGB.h"

#pragma clang diagnostic push
#pragma ide diagnostic ignored "UnusedMethodInspection"

@implementation UIColor (ARGB)

+ (UIColor *)randomColor {
    return [[UIColor alloc] initWithRed:(CGFloat) arc4random_uniform(256) / 255.0
                                  green:(CGFloat) arc4random_uniform(256) / 255.0
                                   blue:(CGFloat) arc4random_uniform(256) / 255.0
                                  alpha:1.0
    ];
}

- (instancetype)initWithRGB:(UInt32)rgb {
    return [self initWithARGB:rgb + 0xF000];
}

- (instancetype)initWithRRGGBB:(UInt32)rrggbb {
    return [self initWithAARRGGBB:rrggbb + 0xFF000000];
}

- (instancetype)initWithARGB:(UInt32)argb {
    return [[UIColor alloc] initWithRed:((CGFloat) ((argb & 0x0F00) >> 8)) / 15.0
                                  green:((CGFloat) ((argb & 0x00F0) >> 4)) / 15.0
                                   blue:((CGFloat) (argb & 0x000F)) / 15.0
                                  alpha:((CGFloat) ((argb & 0xF000) >> 12)) / 15.0
    ];
}

- (instancetype)initWithAARRGGBB:(UInt32)aarrggbb {
    return [[UIColor alloc] initWithRed:((CGFloat) ((aarrggbb & 0x00FF0000) >> 16)) / 255.0
                                  green:((CGFloat) ((aarrggbb & 0x0000FF00) >> 8)) / 255.0
                                   blue:((CGFloat) (aarrggbb & 0x000000FF)) / 255.0
                                  alpha:((CGFloat) ((aarrggbb & 0xFF000000) >> 24)) / 255.0
    ];
}

- (instancetype)initWithHex:(NSString *)hexString {
    NSString *_hexStr = [[hexString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]] uppercaseString];
    if ([_hexStr hasPrefix:@"#"]) {
        _hexStr = [_hexStr substringFromIndex:1];
    }
    if ([_hexStr hasPrefix:@"0x"] || [_hexStr hasPrefix:@"0X"]) {
        _hexStr = [_hexStr substringFromIndex:2];
    }
    if (_hexStr.length == 3 || _hexStr.length == 4) {
        if (_hexStr.length == 3) {
            _hexStr = [NSString stringWithFormat:@"F%@", _hexStr];
        }
        UInt32 hexInt = 0;
        BOOL scan = [[[NSScanner alloc] initWithString:_hexStr] scanHexInt:&hexInt];
        self = [self initWithARGB:scan ? hexInt : 0x0000];
    } else if (_hexStr.length == 6 || _hexStr.length == 8) {
        if (_hexStr.length == 6) {
            _hexStr = [NSString stringWithFormat:@"FF%@", _hexStr];
        }
        UInt32 hexInt = 0;
        BOOL scan = [[[NSScanner alloc] initWithString:_hexStr] scanHexInt:&hexInt];
        self = [self initWithAARRGGBB:scan ? hexInt : 0x00000000];
    } else {
        self = [self initWithARGB:0x0000];
    }
    return self;
}

- (UIImage *)toImage:(CGSize)size {
    CGRect rect = CGRectMake(0, 0, size.width, size.height);
    UIGraphicsBeginImageContextWithOptions(size, false, 0);
    [self setFill];
    UIRectFill(rect);
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return image;
}

@end

#pragma clang diagnostic pop