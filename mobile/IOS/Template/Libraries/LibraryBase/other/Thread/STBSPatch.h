#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STBSPatch : NSObject

+ (BOOL)patchOldFile:(NSString *)oldFilePath newFile:(NSString *)newFilePath withPatchFile:(NSString *)patchFilePath;

@end

NS_ASSUME_NONNULL_END
