__d(function(e,t,r,n,a){Object.defineProperty(n,"__esModule",{value:!0});var o=t(a[0]),i=babelHelpers.interopRequireWildcard(o),l=t(a[1]),s=t(a[2]),u=babelHelpers.interopRequireDefault(s),p=t(a[3]),c=babelHelpers.interopRequireDefault(p),d=t(a[4]),b=babelHelpers.interopRequireDefault(d),f=(function(e){function t(){var e,r,n,a;babelHelpers.classCallCheck(this,t);for(var o=arguments.length,l=Array(o),s=0;s<o;s++)l[s]=arguments[s];return r=n=babelHelpers.possibleConstructorReturn(this,(e=t.__proto__||Object.getPrototypeOf(t)).call.apply(e,[this].concat(l))),n.state={loaded:[n.props.navigation.state.index]},n._getLabel=function(e){var t=e.route,r=e.focused,a=e.tintColor,o=n.props.getLabelText({route:t});return'function'==typeof o?o({focused:r,tintColor:a}):o},n._renderTabBar=function(){var e=n.props,t=e.tabBarComponent,r=void 0===t?c.default:t,a=e.tabBarOptions,o=e.navigation,l=e.screenProps,s=e.getLabelText,u=e.renderIcon,p=e.onTabPress,d=n.props.descriptors,b=n.props.navigation.state;return!1===d[b.routes[b.index].key].options.tabBarVisible?null:i.createElement(r,babelHelpers.extends({},a,{jumpTo:n._jumpTo,navigation:o,screenProps:l,onTabPress:p,getLabelText:s,renderIcon:u}))},n._jumpTo=function(e){var t=n.props,r=t.navigation;(0,t.onIndexChange)(r.state.routes.findIndex(function(t){return t.key===e}))},a=r,babelHelpers.possibleConstructorReturn(n,a)}return babelHelpers.inherits(t,e),babelHelpers.createClass(t,[{key:"componentWillReceiveProps",value:function(e){if(e.navigation.state.index!==this.props.navigation.state.index){var t=e.navigation.state.index;this.setState(function(e){return{loaded:e.loaded.includes(t)?e.loaded:[].concat(babelHelpers.toConsumableArray(e.loaded),[t])}})}}},{key:"render",value:function(){var e=this.props,t=e.navigation,r=e.renderScene,n=t.state.routes,a=this.state.loaded;return i.createElement(l.View,{style:v.container},i.createElement(l.View,{style:v.pages},n.map(function(e,n){if(!a.includes(n))return null;var o=t.state.index===n;return i.createElement(b.default,{key:e.key,style:[l.StyleSheet.absoluteFill,{opacity:o?1:0}],isFocused:o},r({route:e}))})),this._renderTabBar())}}]),t})(i.PureComponent),v=l.StyleSheet.create({container:{flex:1,overflow:'hidden'},pages:{flex:1}});n.default=(0,u.default)(f)},386,[12,17,387,388,391]);