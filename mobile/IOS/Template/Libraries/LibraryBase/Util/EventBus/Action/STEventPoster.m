#import "STEventPoster.h"
#import "STEventBus.h"

@interface STEventPoster ()

@end

@implementation STEventPoster
- (void)dealloc {
}

+ (void)postEventName:(NSString *)name object:(id)object {
    [self postEventName:name object:object forceMain:NO];
}

+ (void)postEventName:(NSString *)name object:(id)object forceMain:(BOOL)actionInMain {
    STEventUserInfo *userInfo = nil;
    if (object) {
        userInfo = [STEventUserInfo new];
        if ([object isKindOfClass:[NSDictionary class]]) {
            userInfo.userInfo = object;
        } else {
            userInfo.extObj = object;
        }
    }
    [[STEventBus sharedInstance] publishEvent:name delivery:userInfo isFromMainTread:actionInMain];
}

@end
