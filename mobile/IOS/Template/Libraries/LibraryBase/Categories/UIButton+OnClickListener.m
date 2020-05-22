//
//  UIButton+OnClickListener.m
//  App
//
//  Created by krmao on 2019/9/30.
//  Copyright © 2019 smart. All rights reserved.
//

#import "UIButton+OnClickListener.h"
#import <objc/runtime.h>

@interface UIButton ()
@end

@implementation UIButton (OnClickListener)

static void *clickKey = &clickKey;

- (void) setOnClickListener:(OnClickListener)onClickListener{
    self.clickListener = onClickListener;
    [self addTarget:self action:@selector(onButtonClicked) forControlEvents:UIControlEventTouchUpInside];
}

- (void)onButtonClicked{
    if (self.clickListener){
        self.clickListener();
    }
}

- (void)setClickListener:(OnClickListener) clickListener{
     objc_setAssociatedObject(self, &clickKey, clickListener, OBJC_ASSOCIATION_COPY);
}

- (OnClickListener)clickListener{
   return objc_getAssociatedObject(self, &clickKey);
}

@end
