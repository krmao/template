__d(function(e,t,r,l,s){Object.defineProperty(l,"__esModule",{value:!0});var a=t(s[0]),n=babelHelpers.interopRequireWildcard(a),o=t(s[1]),i=(function(e){function t(){return babelHelpers.classCallCheck(this,t),babelHelpers.possibleConstructorReturn(this,(t.__proto__||Object.getPrototypeOf(t)).apply(this,arguments))}return babelHelpers.inherits(t,e),babelHelpers.createClass(t,[{key:"render",value:function(){var e=this.props,t=e.isFocused,r=e.children,l=e.style,s=babelHelpers.objectWithoutProperties(e,["isFocused","children","style"]);return n.createElement(o.View,babelHelpers.extends({style:[c.container,l],collapsable:!1,removeClippedSubviews:!0,pointerEvents:t?'auto':'none'},s),n.createElement(o.View,{style:t?c.attached:c.detached},r))}}]),t})(n.Component);l.default=i;var c=o.StyleSheet.create({container:{flex:1,overflow:'hidden'},attached:{flex:1},detached:{flex:1,top:3e3}})},391,[12,17]);