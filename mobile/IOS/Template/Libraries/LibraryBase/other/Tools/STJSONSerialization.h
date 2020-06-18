#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STJSONSerialization : NSObject

// -字典转换成json串
+ (NSString *)dictionaryToJsonString:(NSDictionary *)dict;

// -数组转换成json串
+ (NSString *)arrayToJsonString:(NSArray *)array;

// -json串转换成字典
+ (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString;

// -json串转换成数组
+ (id)arrayWithJsonString:(NSString *)jsonString;

@end

NS_ASSUME_NONNULL_END
