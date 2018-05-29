__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var PropTypes = _require(_dependencyMap[0], 'prop-types');

  var UIManager = _require(_dependencyMap[1], 'UIManager');

  var keyMirror = _require(_dependencyMap[2], 'fbjs/lib/keyMirror');

  var checkPropTypes = PropTypes.checkPropTypes;
  var TypesEnum = {
    spring: true,
    linear: true,
    easeInEaseOut: true,
    easeIn: true,
    easeOut: true,
    keyboard: true
  };
  var Types = keyMirror(TypesEnum);
  var PropertiesEnum = {
    opacity: true,
    scaleXY: true
  };
  var Properties = keyMirror(PropertiesEnum);
  var animType = PropTypes.shape({
    duration: PropTypes.number,
    delay: PropTypes.number,
    springDamping: PropTypes.number,
    initialVelocity: PropTypes.number,
    type: PropTypes.oneOf(Object.keys(Types)).isRequired,
    property: PropTypes.oneOf(Object.keys(Properties))
  });
  var configType = PropTypes.shape({
    duration: PropTypes.number.isRequired,
    create: animType,
    update: animType,
    delete: animType
  });

  function checkConfig(config, location, name) {
    checkPropTypes({
      config: configType
    }, {
      config: config
    }, location, name);
  }

  function configureNext(config, onAnimationDidEnd) {
    if (__DEV__) {
      checkConfig(config, 'config', 'LayoutAnimation.configureNext');
    }

    UIManager.configureNextLayoutAnimation(config, onAnimationDidEnd || function () {}, function () {});
  }

  function create(duration, type, creationProp) {
    return {
      duration: duration,
      create: {
        type: type,
        property: creationProp
      },
      update: {
        type: type
      },
      delete: {
        type: type,
        property: creationProp
      }
    };
  }

  var Presets = {
    easeInEaseOut: create(300, Types.easeInEaseOut, Properties.opacity),
    linear: create(500, Types.linear, Properties.opacity),
    spring: {
      duration: 700,
      create: {
        type: Types.linear,
        property: Properties.opacity
      },
      update: {
        type: Types.spring,
        springDamping: 0.4
      },
      delete: {
        type: Types.linear,
        property: Properties.opacity
      }
    }
  };
  var LayoutAnimation = {
    configureNext: configureNext,
    create: create,
    Types: Types,
    Properties: Properties,
    checkConfig: checkConfig,
    Presets: Presets,
    easeInEaseOut: configureNext.bind(null, Presets.easeInEaseOut),
    linear: configureNext.bind(null, Presets.linear),
    spring: configureNext.bind(null, Presets.spring)
  };
  module.exports = LayoutAnimation;
},"c49af8845a46d9e34f4f556b366e752b",["18eeaf4e01377a466daaccc6ba8ce6f5","467cd3365342d9aaa2e941fe7ace641c","61a2e2589ea976cf04a51cc242a466dd"],"LayoutAnimation");