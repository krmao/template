#import <Foundation/Foundation.h>

@interface STEventPoster : NSObject

/**
 * post an event
 * actionInMain:NO
 *
 * @param name eventName
 * @param object delivery data
 */
+ (void)postEventName:(NSString *)name object:(id)object;

/**
 * post an event
 *
 * @param name eventName
 * @param object delivery data
 * @param actionInMain all blocks action in main thread or not
 */
+ (void)postEventName:(NSString *)name object:(id)object forceMain:(BOOL)actionInMain;
@end
