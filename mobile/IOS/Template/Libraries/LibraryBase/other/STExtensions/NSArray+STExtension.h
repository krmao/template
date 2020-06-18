#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSArray (STExtension)

+ (id)arrayWithObjectForTravel:(id)anObject;

- (id)objectAtIndexForTravel:(NSUInteger)index;

- (NSArray *)subarrayWithRangeForTravel:(NSRange)range;

- (NSArray *)objectsAtIndexesForTravel:(NSIndexSet *)indexes;

@end


@interface NSMutableArray (muSTExtension)

- (void)addObjectForTravel:(id)anObject;

- (void)addObjectForTravel:(id)anObject withClass:(Class)aClass;

- (void)insertObjectForTravel:(id)anObject atIndex:(NSUInteger)index;

- (void)removeObjectAtIndexForTravel:(NSUInteger)index;

- (void)replaceObjectAtIndexForTravel:(NSUInteger)index withObject:(id)anObject;

- (void)exchangeObjectAtIndexForTravel:(NSUInteger)idx1 withObjectAtIndex:(NSUInteger)idx2;

- (void)removeObjectForTravel:(id)anObject inRange:(NSRange)range;

- (void)removeObjectIdenticalToForTravel:(id)anObject inRange:(NSRange)range;

- (void)removeObjectsInRangeForTravel:(NSRange)range;

- (void)replaceObjectsInRangeForTravel:(NSRange)range withObjectsFromArray:(NSArray *)otherArray;

- (void)replaceObjectsInRangeForTravel:(NSRange)range withObjectsFromArray:(NSArray *)otherArray range:(NSRange)otherRange;

- (void)insertObjectsForTravel:(NSArray *)objects atIndexes:(NSIndexSet *)indexes;

- (void)removeObjectsAtIndexesForTravel:(NSIndexSet *)indexes;

- (void)replaceObjectsAtIndexesForTravel:(NSIndexSet *)indexes withObjects:(NSArray *)objects;

- (void)addObjectsFromArrayForTravel:(NSArray *)objects;

@end

NS_ASSUME_NONNULL_END
