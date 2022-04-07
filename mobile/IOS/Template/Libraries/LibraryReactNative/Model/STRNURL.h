#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, STRNURLType) {
    STRNURLNetURL,
    STRNURLBundleURL,
};

NS_ASSUME_NONNULL_BEGIN

@interface STRNURL : NSObject

- (id)initWithPath:(NSString *)urlPath;

+ (BOOL)isSTRNURL:(NSString *)url;

@property(nonatomic) NSString *rnFilePath;
@property(nonatomic) NSString *rnModuleName; //ModuleName
@property(nonatomic) NSURL *rnBundleURL; //本地BundleURL
@property(nonatomic) NSURL *rnNetURL; //直接URL链接远端URL
@property(nonatomic, assign) STRNURLType urlType; //urlType

@end


NS_ASSUME_NONNULL_END
