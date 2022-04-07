#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSMutableDictionary (Safe)

/**
 *  扩展系统的set方法，如果传人nil或者NULL则为删除相应key值的对象，并且检测了key值必须遵循NSCopy协议
 *
 *  @param anObject 传人对象
 *  @param aKey     key
 */
- (void)setSafeObject:(id)anObject forKey:(id)aKey;

/**
 *  扩展系统的 removeSafeObjectForKey: 方法，如果传人 nil 则不做任何操作，并且检测了 key 值必须遵循 NSCopy 协议
 */
- (void)removeSafeObjectForKey:(id)aKey;

@end

NS_ASSUME_NONNULL_END
