#import "NSNotificationCenter+STExtension.h"

@implementation NSNotificationCenter (STExtension)

- (void)postNotificationOnMainThread:(NSNotification *)aNote {
    [self performSelectorOnMainThread:@selector(postNotification:) withObject:aNote waitUntilDone:YES];
}

- (void)postNotificationOnMainThreadWithName:(NSString *)aName object:(id)anObject {
    NSNotification *note = [NSNotification notificationWithName:aName object:anObject];
    [self postNotificationOnMainThread:note];
}

- (void)postNotificationOnMainThreadWithName:(NSString *)aName object:(id)anObject userInfo:(NSDictionary *)aUserInfo {
    NSNotification *note = [NSNotification notificationWithName:aName object:anObject userInfo:aUserInfo];
    [self postNotificationOnMainThread:note];
}

@end

