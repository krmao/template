#import <Foundation/Foundation.h>

#ifndef SYNTHESIZE_SINGLETON_FOR_CLASS

#import <objc/runtime.h>


#pragma mark -
#pragma mark Singleton

/* Synthesize Singleton For Class
 *
 * Creates a singleton interface for the specified class with the following methods:
 *
 * + (MyClass*) sharedInstance;
 * + (void) purgeSharedInstance;
 *
 * Calling sharedInstance will instantiate the class and swizzle some methods to ensure
 * that only a single instance ever exists.
 * Calling purgeSharedInstance will destroy the shared instance and return the swizzled
 * methods to their former selves.
 *
 *
 * Usage:
 *
 * MyClass.h:
 * ========================================
 *    #import "Sigleton.h"
 *
 *    @interface MyClass: SomeSuperclass
 *    {
 *        ...
 *    }
 *    SYNTHESIZE_SINGLETON_FOR_CLASS_HEADER(MyClass);
 *
 *    @end
 * ========================================
 *
 *
 *    MyClass.m:
 * ========================================
 *    #import "MyClass.h"
 *
 *    @implementation MyClass
 *
 *    SYNTHESIZE_SINGLETON_FOR_CLASS(MyClass);
 *
 *    ...
 *
 *    @end
 * ========================================
 *
 *
 * Note: Calling alloc manually will also initialize the singleton, so you
 * can call a more complex init routine to initialize the singleton like so:
 *
 * [[MyClass alloc] initWithParam:firstParam secondParam:secondParam];
 *
 * Just be sure to make such a call BEFORE you call "sharedInstance" in
 * your program.
 */


#pragma mark - ---- ARC SYNTHESIZE_SINGLETON

#define SYNTHESIZE_SINGLETON_FOR_HEADER(className) \
\
+ (className *)shared##className;

#define SYNTHESIZE_SINGLETON_FOR_CLASS(className) \
\
+ (className *)shared##className { \
static className *shared##className = nil; \
static dispatch_once_t onceToken; \
dispatch_once(&onceToken, ^{ \
shared##className = [[self alloc] init]; \
}); \
return shared##className; \
}

#endif //end of undefine SYNTHESIZE_SINGLETON_FOR_CLASS
