#import <Foundation/Foundation.h>

#define STEventDebug_multiple_registration_err @"!multiple_registration!\nName:%@\nTarget:%@"
#define STEventDebug_post_to_no_exist_err @"!post_to_no_exist_event!\nName:%@"
typedef NS_ENUM(NSInteger, STEventDebugType) {
    STEventDebugTypeLog,
    STEventDebugTypeAlert,
};
#define STEventDebug_share [STEventDebug sharedInstance]
@interface STEventDebug : NSObject
@property (nonatomic,assign)STEventDebugType debugType;

+ (STEventDebug *)sharedInstance;
- (void)enableDebug:(BOOL)isEnableDebug;
- (void)showDebugMsg:(NSString *)msg;

- (void)showAllRegistEventDetail;
- (void)sizeOfEventBus;
@end
