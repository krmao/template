#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STBusObject : NSObject

@property(nonatomic, readonly) NSString *businessNamePrefixAndURLHost;


- (id)initWithHost:(NSString *)host;


/**
 *  数据总线，同步处理任务，需要子类重载
 *
 *  @param businessName 业务名
 *  @param params       参数列表，列表中参数类型为对象
 *
 *  @return 任务执行结果
 */
- (id)doDataJob:(NSString *)businessName
         params:(NSArray *)params;

@end

NS_ASSUME_NONNULL_END
