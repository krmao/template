#import "STFlutterPlugin.h"

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterEventPlugin : STFlutterPlugin

+ (void)sendEventToDart:(UIViewController *)currentViewController eventKey:(NSString *) eventKey eventInfo:(NSDictionary *) eventInfo;

@end

NS_ASSUME_NONNULL_END
