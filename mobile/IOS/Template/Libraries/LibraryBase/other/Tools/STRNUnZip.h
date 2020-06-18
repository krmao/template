#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STRNUnZip : NSObject

+ (BOOL)unzipFileAtPath:(NSString *)path toDestination:(NSString *)destination;

@end

NS_ASSUME_NONNULL_END
