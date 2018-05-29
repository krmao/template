__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var React = _require(_dependencyMap[0], 'React');

  var PropTypes = _require(_dependencyMap[1], 'prop-types');

  var StaticRenderer = function (_React$Component) {
    babelHelpers.inherits(StaticRenderer, _React$Component);

    function StaticRenderer() {
      babelHelpers.classCallCheck(this, StaticRenderer);
      return babelHelpers.possibleConstructorReturn(this, (StaticRenderer.__proto__ || Object.getPrototypeOf(StaticRenderer)).apply(this, arguments));
    }

    babelHelpers.createClass(StaticRenderer, [{
      key: "shouldComponentUpdate",
      value: function shouldComponentUpdate(nextProps) {
        return nextProps.shouldUpdate;
      }
    }, {
      key: "render",
      value: function render() {
        return this.props.render();
      }
    }]);
    return StaticRenderer;
  }(React.Component);

  StaticRenderer.propTypes = {
    shouldUpdate: PropTypes.bool.isRequired,
    render: PropTypes.func.isRequired
  };
  module.exports = StaticRenderer;
},"2a3b918a2dbde47333fd60d9eb86ecb6",["e6db4f0efed6b72f641ef0ffed29569f","18eeaf4e01377a466daaccc6ba8ce6f5"],"StaticRenderer");