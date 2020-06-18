#import "STJSONSerialization.h"

@implementation STJSONSerialization

// -字典转换成json串
+ (NSString *)dictionaryToJsonString:(NSDictionary *)dict {

    NSError *parseError = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict options:0 error:&parseError];

    if (parseError) {
        return @"";
    }

    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

}

// -数组转换成json串
+ (NSString *)arrayToJsonString:(NSArray *)array {

    NSError *error = nil;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:array options:NSJSONWritingPrettyPrinted error:&error];

    if (error) {
        return @"";
    }

    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];;
}


// -json串转换成字典
+ (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString {

    if (jsonString == nil) {
        return nil;
    }
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                        options:NSJSONReadingMutableContainers
                                                          error:&err];

    if (err) {
        return nil;
    }
    return dic;
}

// -json串转换成数组
+ (id)arrayWithJsonString:(NSString *)jsonString {

    if (jsonString == nil) {
        return nil;
    }

    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    NSArray *arr = [NSJSONSerialization JSONObjectWithData:jsonData
                                                   options:NSJSONReadingMutableContainers
                                                     error:&err];

    if (err) {
        return nil;
    }
    return arr;

}

@end
