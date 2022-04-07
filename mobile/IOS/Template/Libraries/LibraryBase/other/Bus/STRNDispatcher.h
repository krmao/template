#import <Foundation/Foundation.h>
#import "STRNURL.h"
NS_ASSUME_NONNULL_BEGIN

@interface STRNDispatcher : NSObject

+ (BOOL)dispatcherURLString:(NSString *)urlString;

+ (BOOL)dispatcherURL:(STRNURL *)url;

@end

NS_ASSUME_NONNULL_END
