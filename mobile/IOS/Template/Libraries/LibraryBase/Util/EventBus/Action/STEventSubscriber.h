#import <Foundation/Foundation.h>
#import "STEventDefinition.h"

@interface STEventSubscriber : NSObject
/**
 * Register an event
 * default value:
 *      priority:STEventSubscriberPriorityDefault
 *      isMainThread:NO
 *
 * @param target key
 * @param name eventName
 * @param action action
 */
+ (void)addTarget:(id _Nonnull)target name:(NSString *_Nonnull)name action:(STEventSubscriberActionBlock _Nonnull)action;

/**
 * Register an event
 *
 * @param target key
 * @param name eventName
 * @param priority priority
 * @param isMain action in main thread or not
 * @param action action
 */
+ (void)addTarget:(id _Nonnull)target name:(NSString *_Nonnull)name priority:(STEventSubscriberPriority)priority inMainTread:(BOOL)isMain action:(STEventSubscriberActionBlock _Nonnull)action;

/**
 * remove target subscribers
 *
 * @param target target
 */
+ (void)removeTarget:(id _Nonnull)target;

/**
 * remove an event
 *
 * @param eventName eventName
 */
+ (void)removeEvent:(NSString *_Nonnull)eventName;
@end
