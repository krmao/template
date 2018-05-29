__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Lists/SectionList.js";

  var MetroListView = _require(_dependencyMap[0], 'MetroListView');

  var Platform = _require(_dependencyMap[1], 'Platform');

  var React = _require(_dependencyMap[2], 'React');

  var ScrollView = _require(_dependencyMap[3], 'ScrollView');

  var VirtualizedSectionList = _require(_dependencyMap[4], 'VirtualizedSectionList');

  var defaultProps = babelHelpers.extends({}, VirtualizedSectionList.defaultProps, {
    stickySectionHeadersEnabled: Platform.OS === 'ios'
  });

  var SectionList = function (_React$PureComponent) {
    babelHelpers.inherits(SectionList, _React$PureComponent);

    function SectionList() {
      var _ref;

      var _temp, _this, _ret;

      babelHelpers.classCallCheck(this, SectionList);

      for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
        args[_key] = arguments[_key];
      }

      return _ret = (_temp = (_this = babelHelpers.possibleConstructorReturn(this, (_ref = SectionList.__proto__ || Object.getPrototypeOf(SectionList)).call.apply(_ref, [this].concat(args))), _this), _this._captureRef = function (ref) {
        _this._wrapperListRef = ref;
      }, _temp), babelHelpers.possibleConstructorReturn(_this, _ret);
    }

    babelHelpers.createClass(SectionList, [{
      key: "scrollToLocation",
      value: function scrollToLocation(params) {
        this._wrapperListRef.scrollToLocation(params);
      }
    }, {
      key: "recordInteraction",
      value: function recordInteraction() {
        var listRef = this._wrapperListRef && this._wrapperListRef.getListRef();

        listRef && listRef.recordInteraction();
      }
    }, {
      key: "flashScrollIndicators",
      value: function flashScrollIndicators() {
        var listRef = this._wrapperListRef && this._wrapperListRef.getListRef();

        listRef && listRef.flashScrollIndicators();
      }
    }, {
      key: "getScrollResponder",
      value: function getScrollResponder() {
        var listRef = this._wrapperListRef && this._wrapperListRef.getListRef();

        if (listRef) {
          return listRef.getScrollResponder();
        }
      }
    }, {
      key: "getScrollableNode",
      value: function getScrollableNode() {
        var listRef = this._wrapperListRef && this._wrapperListRef.getListRef();

        if (listRef) {
          return listRef.getScrollableNode();
        }
      }
    }, {
      key: "setNativeProps",
      value: function setNativeProps(props) {
        var listRef = this._wrapperListRef && this._wrapperListRef.getListRef();

        if (listRef) {
          listRef.setNativeProps(props);
        }
      }
    }, {
      key: "render",
      value: function render() {
        var List = this.props.legacyImplementation ? MetroListView : VirtualizedSectionList;
        return React.createElement(List, babelHelpers.extends({}, this.props, {
          ref: this._captureRef,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 332
          }
        }));
      }
    }]);
    return SectionList;
  }(React.PureComponent);

  SectionList.defaultProps = defaultProps;
  module.exports = SectionList;
},"6906fe1ba8331785c7e8d8a39741106e",["4297680cb8b66ff3508edfa2ae182462","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","aa8514022050149acc8c46c0b18dc75a","77576e8f76491543bfcecd3e2a2e853c"],"SectionList");