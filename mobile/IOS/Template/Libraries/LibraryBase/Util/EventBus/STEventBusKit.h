/**
 * https://github.com/PanZhow/STEventBus
 *
 * // 引入头文件
 * #import "STEventBusKit.h"
 *
 * // 订阅事件
 * [STEventSubscriber addTarget:self name:@"login_eventName" priority:STEventSubscriberPriorityDefault inMainTread:YES action:^(STEventUserInfo *info) {
 *      NSLog(@"callback info:%@    thread:%@",[info description],[NSThread currentThread]);
 * }];
 *
 * // 发送事件
 * [STEventPoster postEventName:@"login_eventName" object:nil forceMain:YES];
 *
 */

#ifndef STEventBusKit_h
#define STEventBusKit_h

#import "STEventSubscriber.h"
#import "STEventPoster.h"
#import "STEventDebug.h"

#endif
