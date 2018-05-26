__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });
  var deprecatedKeys = ['tabBar'];

  exports.default = function (screenOptions, route) {
    var keys = Object.keys(screenOptions);
    var deprecatedKey = keys.find(function (key) {
      return deprecatedKeys.includes(key);
    });

    if (typeof screenOptions.title === 'function') {
      throw new Error(["`title` cannot be defined as a function in navigation options for `" + route.routeName + "` screen. \n", 'Try replacing the following:', '{', '    title: ({ state }) => state...', '}', '', 'with:', '({ navigation }) => ({', '    title: navigation.state...', '})'].join('\n'));
    }

    if (deprecatedKey && typeof screenOptions[deprecatedKey] === 'function') {
      throw new Error(["`" + deprecatedKey + "` cannot be defined as a function in navigation options for `" + route.routeName + "` screen. \n", 'Try replacing the following:', '{', "    " + deprecatedKey + ": ({ state }) => ({", '         key: state...', '    })', '}', '', 'with:', '({ navigation }) => ({', "    " + deprecatedKey + "Key: navigation.state...", '})'].join('\n'));
    }

    if (deprecatedKey && typeof screenOptions[deprecatedKey] === 'object') {
      throw new Error(["Invalid key `" + deprecatedKey + "` defined in navigation options for `" + route.routeName + "` screen.", '\n', 'Try replacing the following navigation options:', '{', "    " + deprecatedKey + ": {"].concat(babelHelpers.toConsumableArray(Object.keys(screenOptions[deprecatedKey]).map(function (key) {
        return "        " + key + ": ...,";
      })), ['    },', '}', '\n', 'with:', '{'], babelHelpers.toConsumableArray(Object.keys(screenOptions[deprecatedKey]).map(function (key) {
        return "    " + (deprecatedKey + key[0].toUpperCase() + key.slice(1)) + ": ...,";
      })), ['}']).join('\n'));
    }
  };
},386,[],"node_modules/react-navigation/src/routers/validateScreenOptions.js");