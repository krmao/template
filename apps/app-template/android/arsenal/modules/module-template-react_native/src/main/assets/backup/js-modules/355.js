__d(function(e,n,r,o,t){Object.defineProperty(o,"__esModule",{value:!0});var a=n(t[0]),c=babelHelpers.interopRequireDefault(a);function i(e){return e?e.screen?e.screen:e:null}o.default=function(e){var n=Object.keys(e);(0,c.default)(n.length>0,'Please specify at least one route when configuring a navigator.'),n.forEach(function(n){var r=e[n],o=i(r);if(!o||'function'!=typeof o&&'string'!=typeof o&&!r.getScreen)throw new Error("The component for route '"+n+"' must be a React component. For example:\n\nimport MyScreen from './MyScreen';\n...\n"+n+": MyScreen,\n}\n\nYou can also use a navigator:\n\nimport MyNavigator from './MyNavigator';\n...\n"+n+": MyNavigator,\n}");if(r.screen&&r.getScreen)throw new Error("Route '"+n+"' should declare a screen or a getScreen, not both.")})}},355,[310]);