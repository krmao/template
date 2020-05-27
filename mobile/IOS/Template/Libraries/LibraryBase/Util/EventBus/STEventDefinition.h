#ifndef STEventDefinition_h
#define STEventDefinition_h

#import "STEventUserInfo.h"

typedef NS_ENUM(NSInteger, STEventSubscriberPriority) {
    STEventSubscriberPriorityHigh,
    STEventSubscriberPriorityDefault,
    STEventSubscriberPriorityLow
};

typedef void(^STEventSubscriberActionBlock)(STEventUserInfo *info);

#endif
