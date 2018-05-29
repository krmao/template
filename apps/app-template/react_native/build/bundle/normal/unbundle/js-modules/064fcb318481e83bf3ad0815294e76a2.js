__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Inspector/Inspector.js";

  var Dimensions = _require(_dependencyMap[0], 'Dimensions');

  var InspectorOverlay = _require(_dependencyMap[1], 'InspectorOverlay');

  var InspectorPanel = _require(_dependencyMap[2], 'InspectorPanel');

  var Platform = _require(_dependencyMap[3], 'Platform');

  var React = _require(_dependencyMap[4], 'React');

  var ReactNative = _require(_dependencyMap[5], 'ReactNative');

  var StyleSheet = _require(_dependencyMap[6], 'StyleSheet');

  var Touchable = _require(_dependencyMap[7], 'Touchable');

  var UIManager = _require(_dependencyMap[8], 'UIManager');

  var View = _require(_dependencyMap[9], 'View');

  var emptyObject = _require(_dependencyMap[10], 'fbjs/lib/emptyObject');

  var invariant = _require(_dependencyMap[11], 'fbjs/lib/invariant');

  var hook = window.__REACT_DEVTOOLS_GLOBAL_HOOK__;
  var renderers = findRenderers();
  hook.resolveRNStyle = _require(_dependencyMap[12], 'flattenStyle');

  function findRenderers() {
    var allRenderers = Object.keys(hook._renderers).map(function (key) {
      return hook._renderers[key];
    });
    invariant(allRenderers.length >= 1, 'Expected to find at least one React Native renderer on DevTools hook.');
    return allRenderers;
  }

  function getInspectorDataForViewTag(touchedViewTag) {
    for (var i = 0; i < renderers.length; i++) {
      var renderer = renderers[i];
      var inspectorData = renderer.getInspectorDataForViewTag(touchedViewTag);

      if (inspectorData.hierarchy.length > 0) {
        return inspectorData;
      }
    }

    throw new Error('Expected to find at least one React renderer.');
  }

  var Inspector = function (_React$Component) {
    babelHelpers.inherits(Inspector, _React$Component);

    function Inspector(props) {
      babelHelpers.classCallCheck(this, Inspector);

      var _this = babelHelpers.possibleConstructorReturn(this, (Inspector.__proto__ || Object.getPrototypeOf(Inspector)).call(this, props));

      _initialiseProps.call(_this);

      _this.state = {
        devtoolsAgent: null,
        hierarchy: null,
        panelPos: 'bottom',
        inspecting: true,
        perfing: false,
        inspected: null,
        selection: null,
        inspectedViewTag: _this.props.inspectedViewTag,
        networking: false
      };
      return _this;
    }

    babelHelpers.createClass(Inspector, [{
      key: "componentDidMount",
      value: function componentDidMount() {
        hook.on('react-devtools', this.attachToDevtools);

        if (hook.reactDevtoolsAgent) {
          this.attachToDevtools(hook.reactDevtoolsAgent);
        }
      }
    }, {
      key: "componentWillUnmount",
      value: function componentWillUnmount() {
        if (this._subs) {
          this._subs.map(function (fn) {
            return fn();
          });
        }

        hook.off('react-devtools', this.attachToDevtools);
      }
    }, {
      key: "UNSAFE_componentWillReceiveProps",
      value: function UNSAFE_componentWillReceiveProps(newProps) {
        this.setState({
          inspectedViewTag: newProps.inspectedViewTag
        });
      }
    }, {
      key: "setSelection",
      value: function setSelection(i) {
        var _this2 = this;

        var hierarchyItem = this.state.hierarchy[i];

        var _hierarchyItem$getIns = hierarchyItem.getInspectorData(ReactNative.findNodeHandle),
            measure = _hierarchyItem$getIns.measure,
            props = _hierarchyItem$getIns.props,
            source = _hierarchyItem$getIns.source;

        measure(function (x, y, width, height, left, top) {
          _this2.setState({
            inspected: {
              frame: {
                left: left,
                top: top,
                width: width,
                height: height
              },
              style: props.style,
              source: source
            },
            selection: i
          });
        });
      }
    }, {
      key: "onTouchViewTag",
      value: function onTouchViewTag(touchedViewTag, frame, pointerY) {
        var _getInspectorDataForV = getInspectorDataForViewTag(touchedViewTag),
            hierarchy = _getInspectorDataForV.hierarchy,
            props = _getInspectorDataForV.props,
            selection = _getInspectorDataForV.selection,
            source = _getInspectorDataForV.source;

        if (this.state.devtoolsAgent) {
          var offsetFromLeaf = hierarchy.length - 1 - selection;
          this.state.devtoolsAgent.selectFromDOMNode(touchedViewTag, true, offsetFromLeaf);
        }

        this.setState({
          panelPos: pointerY > Dimensions.get('window').height / 2 ? 'top' : 'bottom',
          selection: selection,
          hierarchy: hierarchy,
          inspected: {
            style: props.style,
            frame: frame,
            source: source
          }
        });
      }
    }, {
      key: "setPerfing",
      value: function setPerfing(val) {
        this.setState({
          perfing: val,
          inspecting: false,
          inspected: null,
          networking: false
        });
      }
    }, {
      key: "setInspecting",
      value: function setInspecting(val) {
        this.setState({
          inspecting: val,
          inspected: null
        });
      }
    }, {
      key: "setTouchTargeting",
      value: function setTouchTargeting(val) {
        var _this3 = this;

        Touchable.TOUCH_TARGET_DEBUG = val;
        this.props.onRequestRerenderApp(function (inspectedViewTag) {
          _this3.setState({
            inspectedViewTag: inspectedViewTag
          });
        });
      }
    }, {
      key: "setNetworking",
      value: function setNetworking(val) {
        this.setState({
          networking: val,
          perfing: false,
          inspecting: false,
          inspected: null
        });
      }
    }, {
      key: "render",
      value: function render() {
        var panelContainerStyle = this.state.panelPos === 'bottom' ? {
          bottom: 0
        } : {
          top: Platform.OS === 'ios' ? 20 : 0
        };
        return React.createElement(
          View,
          {
            style: styles.container,
            pointerEvents: "box-none",
            __source: {
              fileName: _jsxFileName,
              lineNumber: 241
            }
          },
          this.state.inspecting && React.createElement(InspectorOverlay, {
            inspected: this.state.inspected,
            inspectedViewTag: this.state.inspectedViewTag,
            onTouchViewTag: this.onTouchViewTag.bind(this),
            __source: {
              fileName: _jsxFileName,
              lineNumber: 243
            }
          }),
          React.createElement(
            View,
            {
              style: [styles.panelContainer, panelContainerStyle],
              __source: {
                fileName: _jsxFileName,
                lineNumber: 248
              }
            },
            React.createElement(InspectorPanel, {
              devtoolsIsOpen: !!this.state.devtoolsAgent,
              inspecting: this.state.inspecting,
              perfing: this.state.perfing,
              setPerfing: this.setPerfing.bind(this),
              setInspecting: this.setInspecting.bind(this),
              inspected: this.state.inspected,
              hierarchy: this.state.hierarchy,
              selection: this.state.selection,
              setSelection: this.setSelection.bind(this),
              touchTargeting: Touchable.TOUCH_TARGET_DEBUG,
              setTouchTargeting: this.setTouchTargeting.bind(this),
              networking: this.state.networking,
              setNetworking: this.setNetworking.bind(this),
              __source: {
                fileName: _jsxFileName,
                lineNumber: 249
              }
            })
          )
        );
      }
    }]);
    return Inspector;
  }(React.Component);

  var _initialiseProps = function _initialiseProps() {
    var _this4 = this;

    this.attachToDevtools = function (agent) {
      var _hideWait = null;
      var hlSub = agent.sub('highlight', function (_ref) {
        var node = _ref.node,
            name = _ref.name,
            props = _ref.props;
        clearTimeout(_hideWait);

        if (typeof node !== 'number') {
          node = ReactNative.findNodeHandle(node);
        }

        UIManager.measure(node, function (x, y, width, height, left, top) {
          _this4.setState({
            hierarchy: [],
            inspected: {
              frame: {
                left: left,
                top: top,
                width: width,
                height: height
              },
              style: props ? props.style : emptyObject
            }
          });
        });
      });
      var hideSub = agent.sub('hideHighlight', function () {
        if (_this4.state.inspected === null) {
          return;
        }

        _hideWait = setTimeout(function () {
          _this4.setState({
            inspected: null
          });
        }, 100);
      });
      _this4._subs = [hlSub, hideSub];
      agent.on('shutdown', function () {
        _this4.setState({
          devtoolsAgent: null
        });

        _this4._subs = null;
      });

      _this4.setState({
        devtoolsAgent: agent
      });
    };
  };

  var styles = StyleSheet.create({
    container: {
      position: 'absolute',
      backgroundColor: 'transparent',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0
    },
    panelContainer: {
      position: 'absolute',
      left: 0,
      right: 0
    }
  });
  module.exports = Inspector;
},"064fcb318481e83bf3ad0815294e76a2",["cbac9baa189a2fa69f7f5fdd76e9bc71","664571e32b5433e678fc5ea87be9b300","1f3dc5e64b748b6c689aa1472d5e6963","9493a89f5d95c3a8a47c65cfed9b5542","e6db4f0efed6b72f641ef0ffed29569f","1102b68d89d7a6aede9677567aa01362","d31e8c1a3f9844becc88973ecddac872","9be7bff2ec732c7f9f96a83cea3bc22f","467cd3365342d9aaa2e941fe7ace641c","30a3b04291b6e1f01b778ff31271ccc5","da32f9ead03e97a13bff4dea57a4330e","8940a4ad43b101ffc23e725363c70f8d","869f0bd4eed428d95df80a8c03d71093"],"Inspector");