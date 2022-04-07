#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSNotificationCenter (STExtension)

/**
 *  在主线程发送通知对象
 *
 *  @param aNote 通知对象
 */
- (void)postNotificationOnMainThread:(NSNotification *)aNote;

/**
 *  在主线程发送通知（带通知名称和对象）
 *
 *  @param aName    通知名称
 *  @param anObject 通知对象
 */
- (void)postNotificationOnMainThreadWithName:(NSString *)aName object:(id)anObject;

/**
 *  在主线程发送通知（带通知名称、对象和额外信息）
 *
 *  @param aName     通知名称
 *  @param anObject  通知对象
 *  @param aUserInfo 通知额外信息
 */
- (void)postNotificationOnMainThreadWithName:(NSString *)aName object:(id)anObject userInfo:(NSDictionary *)aUserInfo;


@end

NS_ASSUME_NONNULL_END
