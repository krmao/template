#import <UIKit/UIKit.h>
#import "STRNURL.h"

NS_ASSUME_NONNULL_BEGIN

@interface STRNViewController : UIViewController

@property(nonatomic, readonly) STRNURL *rnRUL;

- (instancetype)initWithURL:(STRNURL *)url
       andInitialProperties:(NSDictionary *)initialProperties;

@end

NS_ASSUME_NONNULL_END
