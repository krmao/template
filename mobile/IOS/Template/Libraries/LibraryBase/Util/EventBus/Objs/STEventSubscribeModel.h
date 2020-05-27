#import <Foundation/Foundation.h>
#import "STEventDefinition.h"
/**
 事件订阅者Model
 */
@class STEventUserInfo;
@interface STEventSubscribeModel : NSObject
@property (nonatomic,copy)STEventSubscriberActionBlock actionBlock;
@property (nonatomic,assign)STEventSubscriberPriority priority;
@property (nonatomic,assign)BOOL isInMainThread;
@property (nonatomic,weak)id target;


- (void)actionWIthInfo:(STEventUserInfo *)info forceMainThread:(BOOL)isMain;
@end
