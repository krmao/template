#import <Foundation/Foundation.h>
#import "STBusObject.h"

NS_ASSUME_NONNULL_BEGIN

@interface STBus : NSObject

/**
 *  各业务BU注册busOjbect到总线，方便接收总线发起的调用
 *
 *  @param busObj     业务模块的busObject
 */
+ (void)register:(STBusObject *)busObj;

/**
 *  数据总线，跨业务模块同步调用
 *
 *  @param buinessName   业务名
 *  @param param         调用所带参数，可变长参数，注意参数均为Object类型
 *
 *  @return 同步调用返回的结果
 */
+ (id)callData:(NSString *)buinessName param:(NSObject *)param, ... NS_REQUIRES_NIL_TERMINATION;

@end

NS_ASSUME_NONNULL_END
