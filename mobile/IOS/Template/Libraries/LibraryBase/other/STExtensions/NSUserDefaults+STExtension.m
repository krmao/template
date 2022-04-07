#import "NSUserDefaults+STExtension.h"

@implementation NSUserDefaults (STExtension)

+ (BOOL)boolValueForKey:(NSString *)key {
    if (key.length == 0) {
        return NO;
    }

    NSUserDefaults *dfts = [NSUserDefaults standardUserDefaults];
    BOOL ret = [dfts boolForKey:key];
    return ret;
}

+ (void)setBoolValue:(BOOL)value forKey:(NSString *)key {
    if (key.length == 0) {
        return;
    }

    NSUserDefaults *dfts = [NSUserDefaults standardUserDefaults];
    [dfts setBool:value forKey:key];
    [dfts synchronize];
}

- (void)setSafeObject:(id)anObject forKey:(id)aKey {
    if ([aKey isKindOfClass:[NSString class]]) {
        [self setObject:anObject forKey:aKey];
    } else {
        NSAssert(false, @"NSUserDefaults aKey is NULL, crashed");
    }
}

@end
