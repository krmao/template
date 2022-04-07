#import "STRNURL.h"

@implementation STRNURL

- (STRNURL *)initWithPath:(NSString *)urlPath {
    if (self = [super init]) {
    }
    return self;
}

+ (BOOL)isSTRNURL:(NSString *)url {
    return YES;
}

@end
