__d(function(n,e,t,o,i){Object.defineProperty(o,"__esModule",{value:!0});var a=['tabBar'];o.default=function(n,e){var t=Object.keys(n).find(function(n){return a.includes(n)});if('function'==typeof n.title)throw new Error(["`title` cannot be defined as a function in navigation options for `"+e.routeName+"` screen. \n",'Try replacing the following:','{','    title: ({ state }) => state...','}','','with:','({ navigation }) => ({','    title: navigation.state...','})'].join('\n'));if(t&&'function'==typeof n[t])throw new Error(["`"+t+"` cannot be defined as a function in navigation options for `"+e.routeName+"` screen. \n",'Try replacing the following:','{',"    "+t+": ({ state }) => ({",'         key: state...','    })','}','','with:','({ navigation }) => ({',"    "+t+"Key: navigation.state...",'})'].join('\n'));if(t&&'object'==typeof n[t])throw new Error(["Invalid key `"+t+"` defined in navigation options for `"+e.routeName+"` screen.",'\n','Try replacing the following navigation options:','{',"    "+t+": {"].concat(babelHelpers.toConsumableArray(Object.keys(n[t]).map(function(n){return"        "+n+": ...,"})),['    },','}','\n','with:','{'],babelHelpers.toConsumableArray(Object.keys(n[t]).map(function(n){return"    "+(t+n[0].toUpperCase()+n.slice(1))+": ...,"})),['}']).join('\n'))}},354,[]);