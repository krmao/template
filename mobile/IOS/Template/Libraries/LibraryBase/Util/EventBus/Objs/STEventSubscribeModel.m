#import "STEventSubscribeModel.h"
#import "STEventUserInfo.h"

@interface STEventSubscribeModel()
@end

@implementation STEventSubscribeModel
- (void)dealloc{
}
#pragma mark ======  public  ======
- (void)actionWIthInfo:(STEventUserInfo *)info forceMainThread:(BOOL)isMain{
    if(isMain){
        [STEventSubscribeModel mainTreadAction:^{
            self.actionBlock(info);
        }];
    }else{
        if(self.isInMainThread){
            [STEventSubscribeModel mainTreadAction:^{
                self.actionBlock(info);
            }];
        }else{
            self.actionBlock(info);
        }
    }
}
#pragma mark ======  life cycle  ======
- (instancetype)init{
    self = [super init];
    if (self) {
    }
    return self;
}
#pragma mark ======  delegate  ======

#pragma mark ======  event  ======

#pragma mark ======  private  ======
+ (void)mainTreadAction:(void(^)(void))action{
    if([NSThread isMainThread]){
        if(action){
            action();
        }
    }else{
        dispatch_async(dispatch_get_main_queue(), ^{
            if(action){
                action();
            }
        });
    }
}
#pragma mark ======  getter & setter  ======
@end
