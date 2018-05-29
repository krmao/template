__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-navigation/src/createNavigationContainer.js";
  exports._TESTING_ONLY_reset_container_count = _TESTING_ONLY_reset_container_count;
  exports.default = createNavigationContainer;

  var _react = _require(_dependencyMap[0], "react");

  var _react2 = babelHelpers.interopRequireDefault(_react);

  var _reactNative = _require(_dependencyMap[1], "react-native");

  var _reactLifecyclesCompat = _require(_dependencyMap[2], "react-lifecycles-compat");

  var _PlatformHelpers = _require(_dependencyMap[3], "./PlatformHelpers");

  var _NavigationActions = _require(_dependencyMap[4], "./NavigationActions");

  var _NavigationActions2 = babelHelpers.interopRequireDefault(_NavigationActions);

  var _invariant = _require(_dependencyMap[5], "./utils/invariant");

  var _invariant2 = babelHelpers.interopRequireDefault(_invariant);

  var _getNavigationActionCreators = _require(_dependencyMap[6], "./routers/getNavigationActionCreators");

  var _getNavigationActionCreators2 = babelHelpers.interopRequireDefault(_getNavigationActionCreators);

  var _docsUrl = _require(_dependencyMap[7], "./utils/docsUrl");

  var _docsUrl2 = babelHelpers.interopRequireDefault(_docsUrl);

  function isStateful(props) {
    return !props.navigation;
  }

  function validateProps(props) {
    if (isStateful(props)) {
      return;
    }

    var navigation = props.navigation,
        screenProps = props.screenProps,
        containerProps = babelHelpers.objectWithoutProperties(props, ["navigation", "screenProps"]);
    var keys = Object.keys(containerProps);

    if (keys.length !== 0) {
      throw new Error('This navigator has both navigation and container props, so it is ' + ("unclear if it should own its own state. Remove props: \"" + keys.join(', ') + "\" ") + 'if the navigator should get its state from the navigation prop. If the ' + 'navigator should maintain its own state, do not pass a navigation prop.');
    }
  }

  var _statefulContainerCount = 0;

  function _TESTING_ONLY_reset_container_count() {
    _statefulContainerCount = 0;
  }

  var _reactNavigationIsHydratingState = false;

  function createNavigationContainer(Component) {
    var NavigationContainer = function (_React$Component) {
      babelHelpers.inherits(NavigationContainer, _React$Component);
      babelHelpers.createClass(NavigationContainer, null, [{
        key: "getDerivedStateFromProps",
        value: function getDerivedStateFromProps(nextProps, prevState) {
          validateProps(nextProps);
          return null;
        }
      }]);

      function NavigationContainer(props) {
        var _this2 = this;

        babelHelpers.classCallCheck(this, NavigationContainer);

        var _this = babelHelpers.possibleConstructorReturn(this, (NavigationContainer.__proto__ || Object.getPrototypeOf(NavigationContainer)).call(this, props));

        _this.subs = null;
        _this._actionEventSubscribers = new Set();

        _this._handleOpenURL = function (_ref) {
          var url = _ref.url;

          var parsedUrl = _this._urlToPathAndParams(url);

          if (parsedUrl) {
            var path = parsedUrl.path,
                params = parsedUrl.params;
            var action = Component.router.getActionForPathAndParams(path, params);

            if (action) {
              _this.dispatch(action);
            }
          }
        };

        _this._persistNavigationState = function _callee(nav) {
          var persistenceKey;
          return regeneratorRuntime.async(function _callee$(_context) {
            while (1) {
              switch (_context.prev = _context.next) {
                case 0:
                  persistenceKey = _this.props.persistenceKey;

                  if (persistenceKey) {
                    _context.next = 3;
                    break;
                  }

                  return _context.abrupt("return");

                case 3:
                  _context.next = 5;
                  return regeneratorRuntime.awrap(_reactNative.AsyncStorage.setItem(persistenceKey, JSON.stringify(nav)));

                case 5:
                case "end":
                  return _context.stop();
              }
            }
          }, null, _this2);
        };

        _this.dispatch = function (action) {
          if (_this.props.navigation) {
            return _this.props.navigation.dispatch(action);
          }

          _this._navState = _this._navState || _this.state.nav;
          var lastNavState = _this._navState;
          (0, _invariant2.default)(lastNavState, 'should be set in constructor if stateful');
          var reducedState = Component.router.getStateForAction(action, lastNavState);
          var navState = reducedState === null ? lastNavState : reducedState;

          var dispatchActionEvents = function dispatchActionEvents() {
            _this._actionEventSubscribers.forEach(function (subscriber) {
              return subscriber({
                type: 'action',
                action: action,
                state: navState,
                lastState: lastNavState
              });
            });
          };

          if (reducedState === null) {
            dispatchActionEvents();
            return true;
          }

          if (navState !== lastNavState) {
            _this._navState = navState;

            _this.setState({
              nav: navState
            }, function () {
              _this._onNavigationStateChange(lastNavState, navState, action);

              dispatchActionEvents();

              _this._persistNavigationState(navState);
            });

            return true;
          }

          dispatchActionEvents();
          return false;
        };

        validateProps(props);
        _this._initialAction = _NavigationActions2.default.init();

        if (_this._isStateful()) {
          _this.subs = _PlatformHelpers.BackHandler.addEventListener('hardwareBackPress', function () {
            if (!_this._isMounted) {
              _this.subs && _this.subs.remove();
            } else {
              return _this.dispatch(_NavigationActions2.default.back());
            }
          });
        }

        _this.state = {
          nav: _this._isStateful() && !props.persistenceKey ? Component.router.getStateForAction(_this._initialAction) : null
        };
        return _this;
      }

      babelHelpers.createClass(NavigationContainer, [{
        key: "_renderLoading",
        value: function _renderLoading() {
          return this.props.renderLoadingExperimental ? this.props.renderLoadingExperimental() : null;
        }
      }, {
        key: "_isStateful",
        value: function _isStateful() {
          return isStateful(this.props);
        }
      }, {
        key: "_validateProps",
        value: function _validateProps(props) {
          if (this._isStateful()) {
            return;
          }

          var navigation = props.navigation,
              screenProps = props.screenProps,
              containerProps = babelHelpers.objectWithoutProperties(props, ["navigation", "screenProps"]);
          var keys = Object.keys(containerProps);

          if (keys.length !== 0) {
            throw new Error('This navigator has both navigation and container props, so it is ' + ("unclear if it should own its own state. Remove props: \"" + keys.join(', ') + "\" ") + 'if the navigator should get its state from the navigation prop. If the ' + 'navigator should maintain its own state, do not pass a navigation prop.');
          }
        }
      }, {
        key: "_urlToPathAndParams",
        value: function _urlToPathAndParams(url) {
          var params = {};
          var delimiter = this.props.uriPrefix || '://';
          var path = url.split(delimiter)[1];

          if (typeof path === 'undefined') {
            path = url;
          } else if (path === '') {
            path = '/';
          }

          return {
            path: path,
            params: params
          };
        }
      }, {
        key: "_onNavigationStateChange",
        value: function _onNavigationStateChange(prevNav, nav, action) {
          if (typeof this.props.onNavigationStateChange === 'undefined' && this._isStateful() && !!process.env.REACT_NAV_LOGGING) {
            if (console.group) {
              console.group('Navigation Dispatch: ');
              console.log('Action: ', action);
              console.log('New State: ', nav);
              console.log('Last State: ', prevNav);
              console.groupEnd();
            } else {
              console.log('Navigation Dispatch: ', {
                action: action,
                newState: nav,
                lastState: prevNav
              });
            }

            return;
          }

          if (typeof this.props.onNavigationStateChange === 'function') {
            this.props.onNavigationStateChange(prevNav, nav, action);
          }
        }
      }, {
        key: "componentDidUpdate",
        value: function componentDidUpdate() {
          if (this._navState === this.state.nav) {
            this._navState = null;
          }
        }
      }, {
        key: "componentDidMount",
        value: function componentDidMount() {
          var _this3 = this;

          var persistenceKey, startupStateJSON, url, parsedUrl, action, startupState, path, params, urlAction, dispatchActions;
          return regeneratorRuntime.async(function componentDidMount$(_context2) {
            while (1) {
              switch (_context2.prev = _context2.next) {
                case 0:
                  this._isMounted = true;

                  if (this._isStateful()) {
                    _context2.next = 3;
                    break;
                  }

                  return _context2.abrupt("return");

                case 3:
                  if (__DEV__ && !this.props.detached) {
                    if (_statefulContainerCount > 0) {
                      if (_reactNative.Platform.OS === 'ios') {
                        console.warn("You should only render one navigator explicitly in your app, and other navigators should by rendered by including them in that navigator. Full details at: " + (0, _docsUrl2.default)('common-mistakes.html#explicitly-rendering-more-than-one-navigator'));
                      }
                    }
                  }

                  _statefulContainerCount++;

                  _reactNative.Linking.addEventListener('url', this._handleOpenURL);

                  persistenceKey = this.props.persistenceKey;
                  _context2.t0 = persistenceKey;

                  if (!_context2.t0) {
                    _context2.next = 12;
                    break;
                  }

                  _context2.next = 11;
                  return regeneratorRuntime.awrap(_reactNative.AsyncStorage.getItem(persistenceKey));

                case 11:
                  _context2.t0 = _context2.sent;

                case 12:
                  startupStateJSON = _context2.t0;
                  _context2.next = 15;
                  return regeneratorRuntime.awrap(_reactNative.Linking.getInitialURL());

                case 15:
                  url = _context2.sent;
                  parsedUrl = url && this._urlToPathAndParams(url);
                  action = this._initialAction;
                  startupState = this.state.nav;

                  if (!startupState) {
                    !!process.env.REACT_NAV_LOGGING && console.log('Init new Navigation State');
                    startupState = Component.router.getStateForAction(action);
                  }

                  if (startupStateJSON) {
                    try {
                      startupState = JSON.parse(startupStateJSON);
                      _reactNavigationIsHydratingState = true;
                    } catch (e) {}
                  }

                  if (parsedUrl) {
                    path = parsedUrl.path, params = parsedUrl.params;
                    urlAction = Component.router.getActionForPathAndParams(path, params);

                    if (urlAction) {
                      !!process.env.REACT_NAV_LOGGING && console.log('Applying Navigation Action for Initial URL:', url);
                      action = urlAction;
                      startupState = Component.router.getStateForAction(urlAction, startupState);
                    }
                  }

                  dispatchActions = function dispatchActions() {
                    return _this3._actionEventSubscribers.forEach(function (subscriber) {
                      return subscriber({
                        type: 'action',
                        action: action,
                        state: _this3.state.nav,
                        lastState: null
                      });
                    });
                  };

                  if (!(startupState === this.state.nav)) {
                    _context2.next = 26;
                    break;
                  }

                  dispatchActions();
                  return _context2.abrupt("return");

                case 26:
                  this.setState({
                    nav: startupState
                  }, function () {
                    _reactNavigationIsHydratingState = false;
                    dispatchActions();
                  });

                case 27:
                case "end":
                  return _context2.stop();
              }
            }
          }, null, this);
        }
      }, {
        key: "componentDidCatch",
        value: function componentDidCatch(e, errorInfo) {
          if (_reactNavigationIsHydratingState) {
            _reactNavigationIsHydratingState = false;
            console.warn('Uncaught exception while starting app from persisted navigation state! Trying to render again with a fresh navigation state..');
            this.dispatch(_NavigationActions2.default.init());
          } else {
            throw new Error(e);
          }
        }
      }, {
        key: "componentWillUnmount",
        value: function componentWillUnmount() {
          this._isMounted = false;

          _reactNative.Linking.removeEventListener('url', this._handleOpenURL);

          this.subs && this.subs.remove();

          if (this._isStateful()) {
            _statefulContainerCount--;
          }
        }
      }, {
        key: "render",
        value: function render() {
          var _this4 = this;

          var navigation = this.props.navigation;

          if (this._isStateful()) {
            var nav = this.state.nav;

            if (!nav) {
              return this._renderLoading();
            }

            if (!this._navigation || this._navigation.state !== nav) {
              this._navigation = {
                dispatch: this.dispatch,
                state: nav,
                addListener: function addListener(eventName, handler) {
                  if (eventName !== 'action') {
                    return {
                      remove: function remove() {}
                    };
                  }

                  _this4._actionEventSubscribers.add(handler);

                  return {
                    remove: function remove() {
                      _this4._actionEventSubscribers.delete(handler);
                    }
                  };
                }
              };
              var actionCreators = (0, _getNavigationActionCreators2.default)(nav);
              Object.keys(actionCreators).forEach(function (actionName) {
                _this4._navigation[actionName] = function () {
                  return _this4.dispatch(actionCreators[actionName].apply(actionCreators, arguments));
                };
              });
            }

            navigation = this._navigation;
          }

          (0, _invariant2.default)(navigation, 'failed to get navigation');
          return _react2.default.createElement(Component, babelHelpers.extends({}, this.props, {
            navigation: navigation,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 393
            }
          }));
        }
      }]);
      return NavigationContainer;
    }(_react2.default.Component);

    NavigationContainer.router = Component.router;
    NavigationContainer.navigationOptions = null;
    return (0, _reactLifecyclesCompat.polyfill)(NavigationContainer);
  }
},"2682705ec52ed4b42777ccfb240eb792",["c42a5e17831e80ed1e1c8cf91f5ddb40","cc757a791ecb3cd320f65c256a791c04","9b11863b437a9ee794a44444ae115a3b","96119f6541fc2eb680448e5350aaae93","e91a423170a2d063c973900187b02b24","09df40ab147e7353903f31659d93ee58","1c61d573044491492645aa9a9dd9fc34","06279d3845dec407e734489e2336fee3"],"node_modules/react-navigation/src/createNavigationContainer.js");