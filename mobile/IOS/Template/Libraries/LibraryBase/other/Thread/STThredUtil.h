#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN


BOOL STisMainQueue(void);

void STExecuteOnMainQueue(dispatch_block_t block);

NS_ASSUME_NONNULL_END
