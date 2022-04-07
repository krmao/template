#ifndef STMacros_h
#define STMacros_h

#ifdef DEBUG
#define SLog(format, ...) NSLog((@"%s@%d: " format), __PRETTY_FUNCTION__, __LINE__, ##__VA_ARGS__)
#else
#define SLog(format, ...)
#endif

/** 字体 */
#define STFontSysteSize(value)      [UIFont systemFontOfSize:value]     //系统字体
#define STFontBoldSysteSize(value)  [UIFont boldSystemFontOfSize:value] //系统字体，粗体

/** 位置 */
#define STScreenHeight              [[UIScreen mainScreen] bounds].size.height     //屏幕的高
#define STScreenWidth              [[UIScreen mainScreen] bounds].size.width     //屏幕的宽

/** RGB颜色 */
#define STColorRGB(r, g, b) [UIColor colorWithRed:(r)/255.0f green:(g)/255.0f blue:(b)/255.0f alpha:1.0f]
#define STColorRGBA(r, g, b, a) [UIColor colorWithRed:(r)/255.0f green:(g)/255.0f blue:(b)/255.0f alpha:(a)]

/** HEX颜色 */
#define STColorHex(c) [UIColor colorWithRed:((c>>16)&0xFF)/255.0 green:((c>>8)&0xFF)/255.0 blue:((c)&0xFF)/255.0 alpha:1.0]
#define STColorHexA(c, a) [UIColor colorWithRed:((c>>16)&0xFF)/255.0 green:((c>>8)&0xFF)/255.0 blue:((c)&0xFF)/255.0 alpha:(a)]

#define HexRGBClear         [UIColor clearColor]

#define WS(weakSelf)  __weak __typeof(&*self)weakSelf = self;
#define SS(strongSelf, weakSelf)  __strong __typeof(&*weakSelf)strongSelf = weakSelf;

#endif /* STMacros_h */
