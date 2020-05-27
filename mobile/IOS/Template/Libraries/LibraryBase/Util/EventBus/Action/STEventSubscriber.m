#import "STEventSubscriber.h"
#import "STEventBus.h"

@interface STEventSubscriber ()
@end

@implementation STEventSubscriber
- (void)dealloc {
}

+ (void)addTarget:(id _Nonnull)target name:(NSString *_Nonnull)name action:(STEventSubscriberActionBlock _Nonnull)action {
    [self addTarget:target name:name priority:STEventSubscriberPriorityDefault inMainTread:NO action:action];
}

+ (void)addTarget:(id _Nonnull)target name:(NSString *_Nonnull)name priority:(STEventSubscriberPriority)priority inMainTread:(BOOL)isMain action:(STEventSubscriberActionBlock _Nonnull)action {
    [[STEventBus sharedInstance] registSubscribModel:target markEvent:name priority:priority inMainTread:isMain action:action];
}

+ (void)removeTarget:(id _Nonnull)target {
    [[STEventBus sharedInstance] unregistSubscribModelFromTarget:target];
}

+ (void)removeEvent:(NSString *_Nonnull)eventName {
    [[STEventBus sharedInstance] removeEvent:eventName];
}

@end
