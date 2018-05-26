__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/ReactNative/renderApplication.js";

  var AppContainer = _require(_dependencyMap[0], 'AppContainer');

  var React = _require(_dependencyMap[1], 'React');

  var ReactNative = _require(_dependencyMap[2], 'ReactNative');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  _require(_dependencyMap[4], 'BackHandler');

  function renderApplication(RootComponent, initialProps, rootTag, WrapperComponent) {
    invariant(rootTag, 'Expect to have a valid rootTag, instead got ', rootTag);
    var renderable = React.createElement(
      AppContainer,
      {
        rootTag: rootTag,
        WrapperComponent: WrapperComponent,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 32
        }
      },
      React.createElement(RootComponent, babelHelpers.extends({}, initialProps, {
        rootTag: rootTag,
        __source: {
          fileName: _jsxFileName,
          lineNumber: 33
        }
      }))
    );

    if (RootComponent.prototype != null && RootComponent.prototype.unstable_isAsyncReactComponent === true) {
      var AsyncMode = React.unstable_AsyncMode;
      renderable = React.createElement(
        AsyncMode,
        {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 46
          }
        },
        renderable
      );
    }

    ReactNative.render(renderable, rootTag);
  }

  module.exports = renderApplication;
},319,[265,132,49,18,320],"renderApplication");