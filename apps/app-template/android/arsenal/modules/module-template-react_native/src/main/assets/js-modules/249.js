__d(function(e,t,s,i,o){'use strict';var n=t(o[0]),r=t(o[1]),l=t(o[2]),a=t(o[3]),p=t(o[4]),c=t(o[5]),d=t(o[6]),h=t(o[7]),u=t(o[8]),v=p(babelHelpers.extends({},d,{color:n})),b=(function(e){function t(e,s){babelHelpers.classCallCheck(this,t);var i=babelHelpers.possibleConstructorReturn(this,(t.__proto__||Object.getPrototypeOf(t)).call(this,e,s));f.call(i);var o=i._stateFromProps(e);return i.state=babelHelpers.extends({},o,{initialSelectedIndex:o.selectedIndex}),i}return babelHelpers.inherits(t,e),babelHelpers.createClass(t,[{key:"UNSAFE_componentWillReceiveProps",value:function(e){this.setState(this._stateFromProps(e))}},{key:"render",value:function(){var e="dropdown"===this.props.mode?y:x,t={enabled:this.props.enabled,items:this.state.items,mode:this.props.mode,onSelect:this._onChange,prompt:this.props.prompt,selected:this.state.initialSelectedIndex,testID:this.props.testID,style:[m.pickerAndroid,this.props.style],accessibilityLabel:this.props.accessibilityLabel};return r.createElement(e,babelHelpers.extends({ref:"picker"},t))}},{key:"componentDidMount",value:function(){this._lastNativePosition=this.state.initialSelectedIndex}},{key:"componentDidUpdate",value:function(){this.refs.picker&&this.state.selectedIndex!==this._lastNativePosition&&(this.refs.picker.setNativeProps({selected:this.state.selectedIndex}),this._lastNativePosition=this.state.selectedIndex)}}]),t})(r.Component);b.propTypes=babelHelpers.extends({},c,{style:v,selectedValue:l.any,enabled:l.bool,mode:l.oneOf(['dialog','dropdown']),onValueChange:l.func,prompt:l.string,testID:l.string});var f=function(){var e=this;this._stateFromProps=function(e){var t=0,s=r.Children.map(e.children,function(s,i){s.props.value===e.selectedValue&&(t=i);var o={value:s.props.value,label:s.props.label};return s.props.color&&(o.color=h(s.props.color)),o});return{selectedIndex:t,items:s}},this._onChange=function(t){if(e.props.onValueChange){var s=t.nativeEvent.position;if(s>=0){var i=r.Children.toArray(e.props.children)[s].props.value;e.props.onValueChange(i,s)}else e.props.onValueChange(null,s)}e._lastNativePosition=t.nativeEvent.position,e.forceUpdate()}},m=a.create({pickerAndroid:{height:50}}),_={nativeOnly:{items:!0,selected:!0}},y=u('AndroidDropdownPicker',b,_),x=u('AndroidDialogPicker',b,_);s.exports=b},249,[40,111,108,150,120,112,121,134,127]);