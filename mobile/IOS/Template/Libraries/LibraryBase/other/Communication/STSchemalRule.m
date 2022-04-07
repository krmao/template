#import "STSchemalRule.h"

@implementation STSchemalRule

+ (STSchemalType)schemalType:(NSString *)url {
    
    NSString *url_ = [url lowercaseString];

    if ([url_ hasPrefix:@"smart://smarttravel/rn"]) {
        return STSchemalRN;
    } else if ([url_ hasPrefix:@"smart://smarttravel/h5"] || [url_ hasPrefix:@"http"] || [url_ hasPrefix:@"https"]) {
        return STSchemalH5;
    } else {
        return STSchemalNative;
    }
    
}

+ (NSDictionary *)queryParameters:(NSString *)queryStr {
    
    NSMutableDictionary *mutDic = [[NSMutableDictionary alloc] init];
    
    [[queryStr componentsSeparatedByString:@"&"] enumerateObjectsUsingBlock:^(NSString * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        NSRange range = [obj rangeOfString:@"="];
        [mutDic setValue:[obj substringFromIndex:range.location + 1] forKey:[obj substringToIndex:range.location]];
        
    }];
    
    return mutDic;
    
}

@end
