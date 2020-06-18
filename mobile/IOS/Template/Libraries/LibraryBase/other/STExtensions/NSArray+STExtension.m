#import "NSArray+STExtension.h"
#import "STMacros.h"

@implementation NSArray (STExtension)

+ (id)arrayWithObjectForTravel:(id)anObject {
    if (nil != anObject) {
        return [self arrayWithObject:anObject];
    } else {
        SLog(@"anObject:%@", anObject);
        return nil;
    }
}

- (id)objectAtIndexForTravel:(NSUInteger)index {
    if (index < self.count) {
        return [self objectAtIndex:index];
    } else {
        SLog(@"count:%lu index:%lu", (unsigned long) self.count, (unsigned long) index);
        return nil;
    }
}


- (NSArray *)subarrayWithRangeForTravel:(NSRange)range {
    if (range.location + range.length <= self.count) {
        return [self subarrayWithRange:range];
    } else {
        SLog(@"count:%lu MakeRage(%lu,%lu)", (unsigned long) self.count, (unsigned long) range.location, (unsigned long) range.length);
        return nil;
    }
}

- (NSArray *)objectsAtIndexesForTravel:(NSIndexSet *)indexes {
    if (indexes.firstIndex < self.count && indexes.lastIndex < self.count) {
        return [self objectsAtIndexes:indexes];
    } else {
        SLog(@"count:%lu indexes(%lu-%lu)", (unsigned long) self.count, (unsigned long) indexes.firstIndex, (unsigned long) indexes.lastIndex);
        return nil;
    }
}

@end


@implementation NSMutableArray (muSTExtension)

- (void)addObjectForTravel:(id)anObject {
    if (nil != anObject) {
        [self addObject:anObject];
    } else {
        SLog(@"anObject:%@", anObject);
    }
}

- (void)addObjectForTravel:(id)anObject withClass:(Class)aClass {
    if (nil != anObject && [anObject isKindOfClass:aClass]) {
        [self addObject:anObject];
    } else {
        SLog(@"anObject:%@", anObject);
    }
}

- (void)addObjectsFromArrayForTravel:(NSArray *)objects {
    if (objects && objects.count) {
        [self addObjectsFromArray:objects];
    } else {
        SLog(@"objects:%@", objects);
    }
}

- (void)insertObjectForTravel:(id)anObject atIndex:(NSUInteger)index {
    if (nil != anObject && index <= self.count) {
        [self insertObject:anObject atIndex:index];
    } else {
        SLog(@"count:%lu anObject:%@ index:%lu", (unsigned long) self.count, anObject, (unsigned long) index);
    }
}

- (void)removeObjectAtIndexForTravel:(NSUInteger)index {
    if (index < self.count) {
        [self removeObjectAtIndex:index];
    } else {
        SLog(@"count:%lu index:%lu", (unsigned long) self.count, (unsigned long) index);
    }
}

- (void)replaceObjectAtIndexForTravel:(NSUInteger)index withObject:(id)anObject {
    if (nil != anObject && index < self.count) {
        [self replaceObjectAtIndex:index withObject:anObject];
    } else {
        SLog(@"count:%lu anObject:%@ index:%lu", (unsigned long) self.count, anObject, (unsigned long) index);
    }
}

- (void)exchangeObjectAtIndexForTravel:(NSUInteger)idx1 withObjectAtIndex:(NSUInteger)idx2 {
    if (idx1 < self.count && idx2 < self.count) {
        [self exchangeObjectAtIndex:idx1 withObjectAtIndex:idx2];
    } else {
        SLog(@"count:%lu index1:%lu index2:%lu", (unsigned long) self.count, (unsigned long) idx1, (unsigned long) idx2);
    }
}

- (void)removeObjectForTravel:(id)anObject inRange:(NSRange)range {
    if (range.location + range.length <= self.count) {
        [self removeObject:anObject inRange:range];
    } else {
        SLog(@"count:%lu MakeRage(%lu,%lu)", (unsigned long) self.count, (unsigned long) range.location, (unsigned long) range.length);
    }
}

- (void)removeObjectIdenticalToForTravel:(id)anObject inRange:(NSRange)range {
    if (range.location + range.length <= self.count) {
        [self removeObjectIdenticalTo:anObject inRange:range];
    } else {
        SLog(@"count:%lu MakeRage(%lu,%lu)", (unsigned long) self.count, (unsigned long) range.location, (unsigned long) range.length);
    }
}

- (void)removeObjectsInRangeForTravel:(NSRange)range {
    if (range.location + range.length <= self.count) {
        [self removeObjectsInRange:range];
    } else {
        SLog(@"count:%lu MakeRage(%lu,%lu)", (unsigned long) self.count, (unsigned long) range.location, (unsigned long) range.length);
    }
}

- (void)replaceObjectsInRangeForTravel:(NSRange)range withObjectsFromArray:(NSArray *)otherArray {
    if (range.location + range.length <= self.count) {
        [self replaceObjectsInRange:range withObjectsFromArray:otherArray];
    } else {
        SLog(@"count:%lu MakeRage(%lu,%lu)", (unsigned long) self.count, (unsigned long) range.location, (unsigned long) range.length);
    }
}

- (void)replaceObjectsInRangeForTravel:(NSRange)range withObjectsFromArray:(NSArray *)otherArray range:(NSRange)otherRange {
    if (range.location + range.length <= self.count && otherRange.location + otherRange.length <= otherArray.count) {
        [self replaceObjectsInRange:range withObjectsFromArray:otherArray range:otherRange];
    } else {
        SLog(@"count:%lu MakeRage(%lu,%lu) otherCount:%lu OtherRange(%lu,%lu)", (unsigned long) self.count, (unsigned long) range.location, (unsigned long) range.length, (unsigned long) otherArray.count, (unsigned long) otherRange.location, (unsigned long) otherRange.length);
    }
}

- (void)insertObjectsForTravel:(NSArray *)objects atIndexes:(NSIndexSet *)indexes {
    if (nil != objects && indexes.count == objects.count && indexes.firstIndex <= self.count && indexes.lastIndex <= self.count) {
        [self insertObjects:objects atIndexes:indexes];
    } else {
        SLog(@"count:%lu objects:%@ indexesCount:%lu indexesFirstIndex:%lu indexesLastIndex:%lu", (unsigned long) self.count, objects, (unsigned long) indexes.count, (unsigned long) indexes.firstIndex, (unsigned long) indexes.lastIndex);
    }
}

- (void)removeObjectsAtIndexesForTravel:(NSIndexSet *)indexes {
    if (indexes.firstIndex < self.count && indexes.lastIndex < self.count) {
        [self removeObjectsAtIndexes:indexes];
    } else {
        SLog(@"count:%lu indexesCount:%lu indexesFirstIndex:%lu indexesLastIndex:%lu", (unsigned long) self.count, (unsigned long) indexes.count, (unsigned long) indexes.firstIndex, (unsigned long) indexes.lastIndex);
    }
}

- (void)replaceObjectsAtIndexesForTravel:(NSIndexSet *)indexes withObjects:(NSArray *)objects {
    if (nil != objects && indexes.count == objects.count && indexes.firstIndex < self.count && indexes.lastIndex < self.count) {
        [self replaceObjectsAtIndexes:indexes withObjects:objects];
    } else {
        SLog(@"count:%lu objects:%@ indexesCount:%lu indexesFirstIndex:%lu indexesLastIndex:%lu", (unsigned long) self.count, objects, (unsigned long) indexes.count, (unsigned long) indexes.firstIndex, (unsigned long) indexes.lastIndex);
    }
}

@end
