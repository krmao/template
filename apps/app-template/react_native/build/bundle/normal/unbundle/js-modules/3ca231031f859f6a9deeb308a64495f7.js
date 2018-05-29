__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _shouldPolyfillES6Collection = _require(_dependencyMap[0], '_shouldPolyfillES6Collection');

  var guid = _require(_dependencyMap[1], 'guid');

  var isNode = _require(_dependencyMap[2], 'fbjs/lib/isNode');

  var toIterator = _require(_dependencyMap[3], 'toIterator');

  module.exports = function (global, undefined) {
    if (!_shouldPolyfillES6Collection('Map')) {
      return global.Map;
    }

    var KIND_KEY = 'key';
    var KIND_VALUE = 'value';
    var KIND_KEY_VALUE = 'key+value';
    var KEY_PREFIX = '$map_';
    var SECRET_SIZE_PROP;

    if (__DEV__) {
      SECRET_SIZE_PROP = '$size' + guid();
    }

    var OLD_IE_HASH_PREFIX = 'IE_HASH_';

    var Map = function () {
      function Map(iterable) {
        babelHelpers.classCallCheck(this, Map);

        if (!isObject(this)) {
          throw new TypeError('Wrong map object type.');
        }

        initMap(this);

        if (iterable != null) {
          var it = toIterator(iterable);
          var next;

          while (!(next = it.next()).done) {
            if (!isObject(next.value)) {
              throw new TypeError('Expected iterable items to be pair objects.');
            }

            this.set(next.value[0], next.value[1]);
          }
        }
      }

      babelHelpers.createClass(Map, [{
        key: "clear",
        value: function clear() {
          initMap(this);
        }
      }, {
        key: "has",
        value: function has(key) {
          var index = getIndex(this, key);
          return !!(index != null && this._mapData[index]);
        }
      }, {
        key: "set",
        value: function set(key, value) {
          var index = getIndex(this, key);

          if (index != null && this._mapData[index]) {
            this._mapData[index][1] = value;
          } else {
            index = this._mapData.push([key, value]) - 1;
            setIndex(this, key, index);

            if (__DEV__) {
              this[SECRET_SIZE_PROP] += 1;
            } else {
              this.size += 1;
            }
          }

          return this;
        }
      }, {
        key: "get",
        value: function get(key) {
          var index = getIndex(this, key);

          if (index == null) {
            return undefined;
          } else {
            return this._mapData[index][1];
          }
        }
      }, {
        key: "delete",
        value: function _delete(key) {
          var index = getIndex(this, key);

          if (index != null && this._mapData[index]) {
            setIndex(this, key, undefined);
            this._mapData[index] = undefined;

            if (__DEV__) {
              this[SECRET_SIZE_PROP] -= 1;
            } else {
              this.size -= 1;
            }

            return true;
          } else {
            return false;
          }
        }
      }, {
        key: "entries",
        value: function entries() {
          return new MapIterator(this, KIND_KEY_VALUE);
        }
      }, {
        key: "keys",
        value: function keys() {
          return new MapIterator(this, KIND_KEY);
        }
      }, {
        key: "values",
        value: function values() {
          return new MapIterator(this, KIND_VALUE);
        }
      }, {
        key: "forEach",
        value: function forEach(callback, thisArg) {
          if (typeof callback !== 'function') {
            throw new TypeError('Callback must be callable.');
          }

          var boundCallback = callback.bind(thisArg || undefined);
          var mapData = this._mapData;

          for (var i = 0; i < mapData.length; i++) {
            var entry = mapData[i];

            if (entry != null) {
              boundCallback(entry[1], entry[0], this);
            }
          }
        }
      }]);
      return Map;
    }();

    Map.prototype[toIterator.ITERATOR_SYMBOL] = Map.prototype.entries;

    var MapIterator = function () {
      function MapIterator(map, kind) {
        babelHelpers.classCallCheck(this, MapIterator);

        if (!(isObject(map) && map._mapData)) {
          throw new TypeError('Object is not a map.');
        }

        if ([KIND_KEY, KIND_KEY_VALUE, KIND_VALUE].indexOf(kind) === -1) {
          throw new Error('Invalid iteration kind.');
        }

        this._map = map;
        this._nextIndex = 0;
        this._kind = kind;
      }

      babelHelpers.createClass(MapIterator, [{
        key: "next",
        value: function next() {
          if (!this instanceof Map) {
            throw new TypeError('Expected to be called on a MapIterator.');
          }

          var map = this._map;
          var index = this._nextIndex;
          var kind = this._kind;

          if (map == null) {
            return createIterResultObject(undefined, true);
          }

          var entries = map._mapData;

          while (index < entries.length) {
            var record = entries[index];
            index += 1;
            this._nextIndex = index;

            if (record) {
              if (kind === KIND_KEY) {
                return createIterResultObject(record[0], false);
              } else if (kind === KIND_VALUE) {
                return createIterResultObject(record[1], false);
              } else if (kind) {
                return createIterResultObject(record, false);
              }
            }
          }

          this._map = undefined;
          return createIterResultObject(undefined, true);
        }
      }]);
      return MapIterator;
    }();

    MapIterator.prototype[toIterator.ITERATOR_SYMBOL] = function () {
      return this;
    };

    function getIndex(map, key) {
      if (isObject(key)) {
        var hash = getHash(key);
        return map._objectIndex[hash];
      } else {
        var prefixedKey = KEY_PREFIX + key;

        if (typeof key === 'string') {
          return map._stringIndex[prefixedKey];
        } else {
          return map._otherIndex[prefixedKey];
        }
      }
    }

    function setIndex(map, key, index) {
      var shouldDelete = index == null;

      if (isObject(key)) {
        var hash = getHash(key);

        if (shouldDelete) {
          delete map._objectIndex[hash];
        } else {
          map._objectIndex[hash] = index;
        }
      } else {
        var prefixedKey = KEY_PREFIX + key;

        if (typeof key === 'string') {
          if (shouldDelete) {
            delete map._stringIndex[prefixedKey];
          } else {
            map._stringIndex[prefixedKey] = index;
          }
        } else {
          if (shouldDelete) {
            delete map._otherIndex[prefixedKey];
          } else {
            map._otherIndex[prefixedKey] = index;
          }
        }
      }
    }

    function initMap(map) {
      map._mapData = [];
      map._objectIndex = {};
      map._stringIndex = {};
      map._otherIndex = {};

      if (__DEV__) {
        if (isES5) {
          if (map.hasOwnProperty(SECRET_SIZE_PROP)) {
            map[SECRET_SIZE_PROP] = 0;
          } else {
            Object.defineProperty(map, SECRET_SIZE_PROP, {
              value: 0,
              writable: true
            });
            Object.defineProperty(map, 'size', {
              set: function set(v) {
                console.error('PLEASE FIX ME: You are changing the map size property which ' + 'should not be writable and will break in production.');
                throw new Error('The map size property is not writable.');
              },
              get: function get() {
                return map[SECRET_SIZE_PROP];
              }
            });
          }

          return;
        }
      }

      map.size = 0;
    }

    function isObject(o) {
      return o != null && (typeof o === 'object' || typeof o === 'function');
    }

    function createIterResultObject(value, done) {
      return {
        value: value,
        done: done
      };
    }

    var isES5 = function () {
      try {
        Object.defineProperty({}, 'x', {});
        return true;
      } catch (e) {
        return false;
      }
    }();

    function isExtensible(o) {
      if (!isES5) {
        return true;
      } else {
        return Object.isExtensible(o);
      }
    }

    function getIENodeHash(node) {
      var uniqueID;

      switch (node.nodeType) {
        case 1:
          uniqueID = node.uniqueID;
          break;

        case 9:
          uniqueID = node.documentElement.uniqueID;
          break;

        default:
          return null;
      }

      if (uniqueID) {
        return OLD_IE_HASH_PREFIX + uniqueID;
      } else {
        return null;
      }
    }

    var getHash = function () {
      var propIsEnumerable = Object.prototype.propertyIsEnumerable;
      var hashProperty = guid();
      var hashCounter = 0;
      return function getHash(o) {
        if (o[hashProperty]) {
          return o[hashProperty];
        } else if (!isES5 && o.propertyIsEnumerable && o.propertyIsEnumerable[hashProperty]) {
          return o.propertyIsEnumerable[hashProperty];
        } else if (!isES5 && isNode(o) && getIENodeHash(o)) {
          return getIENodeHash(o);
        } else if (!isES5 && o[hashProperty]) {
          return o[hashProperty];
        }

        if (isExtensible(o)) {
          hashCounter += 1;

          if (isES5) {
            Object.defineProperty(o, hashProperty, {
              enumerable: false,
              writable: false,
              configurable: false,
              value: hashCounter
            });
          } else if (o.propertyIsEnumerable) {
            o.propertyIsEnumerable = function () {
              return propIsEnumerable.apply(this, arguments);
            };

            o.propertyIsEnumerable[hashProperty] = hashCounter;
          } else if (isNode(o)) {
            o[hashProperty] = hashCounter;
          } else {
            throw new Error('Unable to set a non-enumerable property on object.');
          }

          return hashCounter;
        } else {
          throw new Error('Non-extensible objects are not allowed as keys.');
        }
      };
    }();

    return Map;
  }(Function('return this')());
},"3ca231031f859f6a9deeb308a64495f7",["ee4b9bb0852a41afb06d2c68334f0f19","64db148920110163e29d54a1e3046610","d53475fc6035d1db4b728ec49bcf05ca","1de9aa85c939144deee9416ef1890ac1"],"Map");