__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var KIND_KEY = 'key';
  var KIND_VALUE = 'value';
  var KIND_KEY_VAL = 'key+value';
  var ITERATOR_SYMBOL = typeof Symbol === 'function' ? typeof Symbol === "function" ? Symbol.iterator : "@@iterator" : '@@iterator';

  var toIterator = function () {
    if (!(Array.prototype[ITERATOR_SYMBOL] && String.prototype[ITERATOR_SYMBOL])) {
      return function () {
        var ArrayIterator = function () {
          function ArrayIterator(array, kind) {
            babelHelpers.classCallCheck(this, ArrayIterator);

            if (!Array.isArray(array)) {
              throw new TypeError('Object is not an Array');
            }

            this._iteratedObject = array;
            this._kind = kind;
            this._nextIndex = 0;
          }

          babelHelpers.createClass(ArrayIterator, [{
            key: "next",
            value: function next() {
              if (!this instanceof ArrayIterator) {
                throw new TypeError('Object is not an ArrayIterator');
              }

              if (this._iteratedObject == null) {
                return createIterResultObject(undefined, true);
              }

              var array = this._iteratedObject;
              var len = this._iteratedObject.length;
              var index = this._nextIndex;
              var kind = this._kind;

              if (index >= len) {
                this._iteratedObject = undefined;
                return createIterResultObject(undefined, true);
              }

              this._nextIndex = index + 1;

              if (kind === KIND_KEY) {
                return createIterResultObject(index, false);
              } else if (kind === KIND_VALUE) {
                return createIterResultObject(array[index], false);
              } else if (kind === KIND_KEY_VAL) {
                return createIterResultObject([index, array[index]], false);
              }
            }
          }, {
            key: '@@iterator',
            value: function iterator() {
              return this;
            }
          }]);
          return ArrayIterator;
        }();

        var StringIterator = function () {
          function StringIterator(string) {
            babelHelpers.classCallCheck(this, StringIterator);

            if (typeof string !== 'string') {
              throw new TypeError('Object is not a string');
            }

            this._iteratedString = string;
            this._nextIndex = 0;
          }

          babelHelpers.createClass(StringIterator, [{
            key: "next",
            value: function next() {
              if (!this instanceof StringIterator) {
                throw new TypeError('Object is not a StringIterator');
              }

              if (this._iteratedString == null) {
                return createIterResultObject(undefined, true);
              }

              var index = this._nextIndex;
              var s = this._iteratedString;
              var len = s.length;

              if (index >= len) {
                this._iteratedString = undefined;
                return createIterResultObject(undefined, true);
              }

              var ret;
              var first = s.charCodeAt(index);

              if (first < 0xD800 || first > 0xDBFF || index + 1 === len) {
                ret = s[index];
              } else {
                var second = s.charCodeAt(index + 1);

                if (second < 0xDC00 || second > 0xDFFF) {
                  ret = s[index];
                } else {
                  ret = s[index] + s[index + 1];
                }
              }

              this._nextIndex = index + ret.length;
              return createIterResultObject(ret, false);
            }
          }, {
            key: '@@iterator',
            value: function iterator() {
              return this;
            }
          }]);
          return StringIterator;
        }();

        function createIterResultObject(value, done) {
          return {
            value: value,
            done: done
          };
        }

        return function (object, kind) {
          if (typeof object === 'string') {
            return new StringIterator(object);
          } else if (Array.isArray(object)) {
            return new ArrayIterator(object, kind || KIND_VALUE);
          } else {
            return object[ITERATOR_SYMBOL]();
          }
        };
      }();
    } else {
      return function (object) {
        return object[ITERATOR_SYMBOL]();
      };
    }
  }();

  babelHelpers.extends(toIterator, {
    KIND_KEY: KIND_KEY,
    KIND_VALUE: KIND_VALUE,
    KIND_KEY_VAL: KIND_KEY_VAL,
    ITERATOR_SYMBOL: ITERATOR_SYMBOL
  });
  module.exports = toIterator;
},"1de9aa85c939144deee9416ef1890ac1",[],"toIterator");