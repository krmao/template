__d(function(e,t,n,r,o){Object.defineProperty(r,"__esModule",{value:!0});var i=t(o[0]),a=babelHelpers.interopRequireDefault(i),l=t(o[1]),u=(function(e){function t(){return babelHelpers.classCallCheck(this,t),babelHelpers.possibleConstructorReturn(this,(t.__proto__||Object.getPrototypeOf(t)).apply(this,arguments))}return babelHelpers.inherits(t,e),babelHelpers.createClass(t,[{key:"render",value:function(){var e=this.props,t=e.position,n=e.scene,r=e.navigation,o=e.activeTintColor,i=e.inactiveTintColor,u=e.style,c=n.route,p=n.index,d=r.state.routes,f=[-1].concat(babelHelpers.toConsumableArray(d.map(function(e,t){return t}))),b=t.interpolate({inputRange:f,outputRange:f.map(function(e){return e===p?1:0})}),h=t.interpolate({inputRange:f,outputRange:f.map(function(e){return e===p?0:1})});return a.default.createElement(l.View,{style:u},a.default.createElement(l.Animated.View,{style:[s.icon,{opacity:b}]},this.props.renderIcon({route:c,index:p,focused:!0,tintColor:o})),a.default.createElement(l.Animated.View,{style:[s.icon,{opacity:h}]},this.props.renderIcon({route:c,index:p,focused:!1,tintColor:i})))}}]),t})(a.default.PureComponent);r.default=u;var s=l.StyleSheet.create({icon:{position:'absolute',alignSelf:'center',alignItems:'center',justifyContent:'center',height:'100%',width:'100%',minWidth:30}})},383,[12,17]);