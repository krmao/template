__d(function(e,t,n,a,o){Object.defineProperty(a,"__esModule",{value:!0});var r=t(o[0]),u=babelHelpers.interopRequireDefault(r),i=t(o[1]),l=babelHelpers.interopRequireDefault(i),s=t(o[2]),p=babelHelpers.interopRequireDefault(s);function f(e,t,n){return'function'==typeof e?babelHelpers.extends({},t,e(babelHelpers.extends({},n,{navigationOptions:t}))):'object'==typeof e?babelHelpers.extends({},t,e):t}a.default=function(e,t){return function(n,a){var o=n.state,r=(n.dispatch,o);(0,u.default)(r.routeName&&'string'==typeof r.routeName,'Cannot get config because the route does not have a routeName.');var i=(0,l.default)(e,r.routeName),s=e[r.routeName],b=s===i?null:s.navigationOptions,d=i.navigationOptions,c={navigation:n,screenProps:a||{}},v=f(t,{},c);return v=f(b,v=f(d,v,c),c),(0,p.default)(v,r),v}}},352,[310,353,354]);