__d(function(e,r,o,t,a){'use strict';var n=r(a[0]),s=r(a[1]),i=r(a[2]),d=r(a[3]),l=r(a[4]),w=r(a[5]),p=r(a[6]),u=r(a[7]),c=r(a[8]),h=r(a[9]),g=r(a[10]),D=c.AndroidDrawerLayout.Constants,f=r(a[11]),C=r(a[12]),b=r(a[13]),m=['Idle','Dragging','Settling'],S=f({displayName:'DrawerLayoutAndroid',statics:{positions:D.DrawerPosition},propTypes:babelHelpers.extends({},g,{keyboardDismissMode:l.oneOf(['none','on-drag']),drawerBackgroundColor:n,drawerPosition:l.oneOf([D.DrawerPosition.Left,D.DrawerPosition.Right]),drawerWidth:l.number,drawerLockMode:l.oneOf(['unlocked','locked-closed','locked-open']),onDrawerSlide:l.func,onDrawerStateChanged:l.func,onDrawerOpen:l.func,onDrawerClose:l.func,renderNavigationView:l.func.isRequired,statusBarBackgroundColor:n}),mixins:[s],getDefaultProps:function(){return{drawerBackgroundColor:'white'}},getInitialState:function(){return{statusBarBackgroundColor:void 0}},getInnerViewNode:function(){return this.refs.innerView.getInnerViewNode()},render:function(){var e=i.Version>=21&&this.props.statusBarBackgroundColor,r=d.createElement(h,{style:[k.drawerSubview,{width:this.props.drawerWidth,backgroundColor:this.props.drawerBackgroundColor}],collapsable:!1},this.props.renderNavigationView(),e&&d.createElement(h,{style:k.drawerStatusBar})),o=d.createElement(h,{ref:"innerView",style:k.mainSubview,collapsable:!1},e&&d.createElement(p,{translucent:!0,backgroundColor:this.props.statusBarBackgroundColor}),e&&d.createElement(h,{style:[k.statusBar,{backgroundColor:this.props.statusBarBackgroundColor}]}),this.props.children);return d.createElement(y,babelHelpers.extends({},this.props,{ref:"drawerlayout",drawerWidth:this.props.drawerWidth,drawerPosition:this.props.drawerPosition,drawerLockMode:this.props.drawerLockMode,style:[k.base,this.props.style],onDrawerSlide:this._onDrawerSlide,onDrawerOpen:this._onDrawerOpen,onDrawerClose:this._onDrawerClose,onDrawerStateChanged:this._onDrawerStateChanged}),o,r)},_onDrawerSlide:function(e){this.props.onDrawerSlide&&this.props.onDrawerSlide(e),'on-drag'===this.props.keyboardDismissMode&&C()},_onDrawerOpen:function(){this.props.onDrawerOpen&&this.props.onDrawerOpen()},_onDrawerClose:function(){this.props.onDrawerClose&&this.props.onDrawerClose()},_onDrawerStateChanged:function(e){this.props.onDrawerStateChanged&&this.props.onDrawerStateChanged(m[e.nativeEvent.drawerState])},openDrawer:function(){c.dispatchViewManagerCommand(this._getDrawerLayoutHandle(),c.AndroidDrawerLayout.Commands.openDrawer,null)},closeDrawer:function(){c.dispatchViewManagerCommand(this._getDrawerLayoutHandle(),c.AndroidDrawerLayout.Commands.closeDrawer,null)},_getDrawerLayoutHandle:function(){return w.findNodeHandle(this.refs.drawerlayout)}}),k=u.create({base:{flex:1,elevation:16},mainSubview:{position:'absolute',top:0,left:0,right:0,bottom:0},drawerSubview:{position:'absolute',top:0,bottom:0},statusBar:{height:p.currentHeight},drawerStatusBar:{position:'absolute',top:0,left:0,right:0,height:p.currentHeight,backgroundColor:'rgba(0, 0, 0, 0.251)'}}),y=b('AndroidDrawerLayout',S);o.exports=S},221,[40,42,28,111,108,43,222,150,99,152,112,155,213,127]);