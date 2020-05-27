#import "STEventBus.h"
#import "STEvent.h"
#import "STEventSubscribeModel.h"
#import "STEventUserInfo.h"
#import "STEventDebug.h"
@interface STEventBus()
@property (nonatomic,strong)NSMutableDictionary *events;
@end

@implementation STEventBus

- (void)dealloc{
}
#pragma mark ======  public  ======
+ (STEventBus *)sharedInstance
{
    static STEventBus *instance=nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[STEventBus alloc] init];
    });
    return instance;
}
- (void)registSubscribModel:(id _Nonnull)target markEvent:(NSString * _Nonnull)eventName priority:(STEventSubscriberPriority)priority inMainTread:(BOOL)isMain action:(STEventSubscriberActionBlock _Nonnull)action{
    if(!target) {return;}
    if(!eventName) {return;}
    if(!action) {return;}

    dispatch_async(event_bus_dispatcher_serialQueue(), ^{
        STEvent *event = self.events[eventName];
        if(!event){
            event = [[STEvent alloc] init];
            event.eventName = eventName;
            self.events[eventName] = event;
        }else{
            if([event hasContainedSubscribModelForKey:target]){
                NSString *msg = [NSString stringWithFormat:STEventDebug_multiple_registration_err,eventName,target];
                [STEventDebug_share showDebugMsg:msg];
                return;
            }
        }
        STEventSubscribeModel *subscribModel = [[STEventSubscribeModel alloc] init];
        subscribModel.priority = priority;
        subscribModel.isInMainThread = isMain;
        subscribModel.actionBlock = action;
        subscribModel.target = target;
        [event registSubscribModel:subscribModel forKey:target];
    });
}
- (void)publishEvent:(NSString * _Nonnull)eventName delivery:(STEventUserInfo *)info isFromMainTread:(BOOL)isMain{
    if(!eventName) {return;}

    dispatch_async(event_bus_dispatcher_serialQueue(), ^{
        STEvent *event = self.events[eventName];
        if(!event){
            NSString *msg = [NSString stringWithFormat:STEventDebug_post_to_no_exist_err,eventName];
            [STEventDebug_share showDebugMsg:msg];
            return;
        }
        if([event isEmptyMap]){
            [self removeEvent:eventName];
            NSString *msg = [NSString stringWithFormat:STEventDebug_post_to_no_exist_err,eventName];
            [STEventDebug_share showDebugMsg:msg];
            return;
        }
        [event postEventWithDeliveryData:info isInMain:isMain];
    });
}
- (void)unregistSubscribModelFromTarget:(id _Nonnull)target{
    if(!target) {return;}

    dispatch_async(event_bus_dispatcher_serialQueue(), ^{
        NSMutableArray *deleteEventNames = [NSMutableArray array];
        [self.events enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, id  _Nonnull obj, BOOL * _Nonnull stop) {
            STEvent *event = obj;
            NSString *eventName = key;
            BOOL hasNoSubscribers = [event deleteEntryForTarget:target];
            if(hasNoSubscribers){
                [deleteEventNames addObject:eventName];
            }
        }];
        [self.events removeObjectsForKeys:deleteEventNames];
    });
}
- (void)removeEvent:(NSString *_Nonnull)eventName{
    if(!eventName) {return;}
    dispatch_async(event_bus_dispatcher_serialQueue(), ^{
        [self.events removeObjectForKey:eventName];
    });
}
#pragma mark ======  life cycle  ======

#pragma mark ======  delegate  ======

#pragma mark ======  event  ======

#pragma mark ======  private  ======

#pragma mark ======  c  ======
static dispatch_queue_t event_bus_dispatcher_serialQueue() {
    static dispatch_queue_t st_event_bus_dispatcher_serialQueue;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        st_event_bus_dispatcher_serialQueue = dispatch_queue_create("COM.ST_EVENT.BUS.SERIAL_QUEUE", DISPATCH_QUEUE_SERIAL);

        dispatch_queue_t referQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
        dispatch_set_target_queue(st_event_bus_dispatcher_serialQueue, referQueue);

    });
    return st_event_bus_dispatcher_serialQueue;
}
#pragma mark ======  getter & setter  ======
- (NSMutableDictionary *)events{
    if(!_events){
        _events = [[NSMutableDictionary alloc] init];
    }
    return _events;
}

@end
