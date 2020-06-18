#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, STSchemalType) {
    STSchemalUrl,
    STSchemalRN,
    STSchemalH5,
    STSchemalNative,
    STSchemalExternal
};

NS_ASSUME_NONNULL_BEGIN

@interface STSchemalRule : NSObject

+ (STSchemalType)schemalType:(NSString *)url;

+ (NSDictionary *)queryParameters:(NSString *)queryStr;

@end

NS_ASSUME_NONNULL_END
