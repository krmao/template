#import "STThredUtil.h"

BOOL STisMainQueue() {
    static void *mainQueueKey = &mainQueueKey;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        dispatch_queue_set_specific(dispatch_get_main_queue(),
                mainQueueKey, mainQueueKey, NULL);
    });
    return dispatch_get_specific(mainQueueKey) == mainQueueKey;
}

void STExecuteOnMainQueue(dispatch_block_t block) {
    if (STisMainQueue()) {
        block();
    } else {
        dispatch_async(dispatch_get_main_queue(), ^{
            block();
        });
    }
}


