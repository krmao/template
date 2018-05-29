__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _assign = _require(_dependencyMap[0], 'object-assign');

  var emptyObject = _require(_dependencyMap[1], 'fbjs/lib/emptyObject');

  var _invariant = _require(_dependencyMap[2], 'fbjs/lib/invariant');

  if (process.env.NODE_ENV !== 'production') {
    var warning = _require(_dependencyMap[3], 'fbjs/lib/warning');
  }

  var MIXINS_KEY = 'mixins';

  function identity(fn) {
    return fn;
  }

  var ReactPropTypeLocationNames;

  if (process.env.NODE_ENV !== 'production') {
    ReactPropTypeLocationNames = {
      prop: 'prop',
      context: 'context',
      childContext: 'child context'
    };
  } else {
    ReactPropTypeLocationNames = {};
  }

  function factory(ReactComponent, isValidElement, ReactNoopUpdateQueue) {
    var injectedMixins = [];
    var ReactClassInterface = {
      mixins: 'DEFINE_MANY',
      statics: 'DEFINE_MANY',
      propTypes: 'DEFINE_MANY',
      contextTypes: 'DEFINE_MANY',
      childContextTypes: 'DEFINE_MANY',
      getDefaultProps: 'DEFINE_MANY_MERGED',
      getInitialState: 'DEFINE_MANY_MERGED',
      getChildContext: 'DEFINE_MANY_MERGED',
      render: 'DEFINE_ONCE',
      componentWillMount: 'DEFINE_MANY',
      componentDidMount: 'DEFINE_MANY',
      componentWillReceiveProps: 'DEFINE_MANY',
      shouldComponentUpdate: 'DEFINE_ONCE',
      componentWillUpdate: 'DEFINE_MANY',
      componentDidUpdate: 'DEFINE_MANY',
      componentWillUnmount: 'DEFINE_MANY',
      UNSAFE_componentWillMount: 'DEFINE_MANY',
      UNSAFE_componentWillReceiveProps: 'DEFINE_MANY',
      UNSAFE_componentWillUpdate: 'DEFINE_MANY',
      updateComponent: 'OVERRIDE_BASE'
    };
    var ReactClassStaticInterface = {
      getDerivedStateFromProps: 'DEFINE_MANY_MERGED'
    };
    var RESERVED_SPEC_KEYS = {
      displayName: function displayName(Constructor, _displayName) {
        Constructor.displayName = _displayName;
      },
      mixins: function mixins(Constructor, _mixins) {
        if (_mixins) {
          for (var i = 0; i < _mixins.length; i++) {
            mixSpecIntoComponent(Constructor, _mixins[i]);
          }
        }
      },
      childContextTypes: function childContextTypes(Constructor, _childContextTypes) {
        if (process.env.NODE_ENV !== 'production') {
          validateTypeDef(Constructor, _childContextTypes, 'childContext');
        }

        Constructor.childContextTypes = _assign({}, Constructor.childContextTypes, _childContextTypes);
      },
      contextTypes: function contextTypes(Constructor, _contextTypes) {
        if (process.env.NODE_ENV !== 'production') {
          validateTypeDef(Constructor, _contextTypes, 'context');
        }

        Constructor.contextTypes = _assign({}, Constructor.contextTypes, _contextTypes);
      },
      getDefaultProps: function getDefaultProps(Constructor, _getDefaultProps) {
        if (Constructor.getDefaultProps) {
          Constructor.getDefaultProps = createMergedResultFunction(Constructor.getDefaultProps, _getDefaultProps);
        } else {
          Constructor.getDefaultProps = _getDefaultProps;
        }
      },
      propTypes: function propTypes(Constructor, _propTypes) {
        if (process.env.NODE_ENV !== 'production') {
          validateTypeDef(Constructor, _propTypes, 'prop');
        }

        Constructor.propTypes = _assign({}, Constructor.propTypes, _propTypes);
      },
      statics: function statics(Constructor, _statics) {
        mixStaticSpecIntoComponent(Constructor, _statics);
      },
      autobind: function autobind() {}
    };

    function validateTypeDef(Constructor, typeDef, location) {
      for (var propName in typeDef) {
        if (typeDef.hasOwnProperty(propName)) {
          if (process.env.NODE_ENV !== 'production') {
            warning(typeof typeDef[propName] === 'function', '%s: %s type `%s` is invalid; it must be a function, usually from ' + 'React.PropTypes.', Constructor.displayName || 'ReactClass', ReactPropTypeLocationNames[location], propName);
          }
        }
      }
    }

    function validateMethodOverride(isAlreadyDefined, name) {
      var specPolicy = ReactClassInterface.hasOwnProperty(name) ? ReactClassInterface[name] : null;

      if (ReactClassMixin.hasOwnProperty(name)) {
        _invariant(specPolicy === 'OVERRIDE_BASE', 'ReactClassInterface: You are attempting to override ' + '`%s` from your class specification. Ensure that your method names ' + 'do not overlap with React methods.', name);
      }

      if (isAlreadyDefined) {
        _invariant(specPolicy === 'DEFINE_MANY' || specPolicy === 'DEFINE_MANY_MERGED', 'ReactClassInterface: You are attempting to define ' + '`%s` on your component more than once. This conflict may be due ' + 'to a mixin.', name);
      }
    }

    function mixSpecIntoComponent(Constructor, spec) {
      if (!spec) {
        if (process.env.NODE_ENV !== 'production') {
          var typeofSpec = typeof spec;
          var isMixinValid = typeofSpec === 'object' && spec !== null;

          if (process.env.NODE_ENV !== 'production') {
            warning(isMixinValid, "%s: You're attempting to include a mixin that is either null " + 'or not an object. Check the mixins included by the component, ' + 'as well as any mixins they include themselves. ' + 'Expected object but got %s.', Constructor.displayName || 'ReactClass', spec === null ? null : typeofSpec);
          }
        }

        return;
      }

      _invariant(typeof spec !== 'function', "ReactClass: You're attempting to " + 'use a component class or function as a mixin. Instead, just use a ' + 'regular object.');

      _invariant(!isValidElement(spec), "ReactClass: You're attempting to " + 'use a component as a mixin. Instead, just use a regular object.');

      var proto = Constructor.prototype;
      var autoBindPairs = proto.__reactAutoBindPairs;

      if (spec.hasOwnProperty(MIXINS_KEY)) {
        RESERVED_SPEC_KEYS.mixins(Constructor, spec.mixins);
      }

      for (var name in spec) {
        if (!spec.hasOwnProperty(name)) {
          continue;
        }

        if (name === MIXINS_KEY) {
          continue;
        }

        var property = spec[name];
        var isAlreadyDefined = proto.hasOwnProperty(name);
        validateMethodOverride(isAlreadyDefined, name);

        if (RESERVED_SPEC_KEYS.hasOwnProperty(name)) {
          RESERVED_SPEC_KEYS[name](Constructor, property);
        } else {
          var isReactClassMethod = ReactClassInterface.hasOwnProperty(name);
          var isFunction = typeof property === 'function';
          var shouldAutoBind = isFunction && !isReactClassMethod && !isAlreadyDefined && spec.autobind !== false;

          if (shouldAutoBind) {
            autoBindPairs.push(name, property);
            proto[name] = property;
          } else {
            if (isAlreadyDefined) {
              var specPolicy = ReactClassInterface[name];

              _invariant(isReactClassMethod && (specPolicy === 'DEFINE_MANY_MERGED' || specPolicy === 'DEFINE_MANY'), 'ReactClass: Unexpected spec policy %s for key %s ' + 'when mixing in component specs.', specPolicy, name);

              if (specPolicy === 'DEFINE_MANY_MERGED') {
                proto[name] = createMergedResultFunction(proto[name], property);
              } else if (specPolicy === 'DEFINE_MANY') {
                proto[name] = createChainedFunction(proto[name], property);
              }
            } else {
              proto[name] = property;

              if (process.env.NODE_ENV !== 'production') {
                if (typeof property === 'function' && spec.displayName) {
                  proto[name].displayName = spec.displayName + '_' + name;
                }
              }
            }
          }
        }
      }
    }

    function mixStaticSpecIntoComponent(Constructor, statics) {
      if (!statics) {
        return;
      }

      for (var name in statics) {
        var property = statics[name];

        if (!statics.hasOwnProperty(name)) {
          continue;
        }

        var isReserved = name in RESERVED_SPEC_KEYS;

        _invariant(!isReserved, 'ReactClass: You are attempting to define a reserved ' + 'property, `%s`, that shouldn\'t be on the "statics" key. Define it ' + 'as an instance property instead; it will still be accessible on the ' + 'constructor.', name);

        var isAlreadyDefined = name in Constructor;

        if (isAlreadyDefined) {
          var specPolicy = ReactClassStaticInterface.hasOwnProperty(name) ? ReactClassStaticInterface[name] : null;

          _invariant(specPolicy === 'DEFINE_MANY_MERGED', 'ReactClass: You are attempting to define ' + '`%s` on your component more than once. This conflict may be ' + 'due to a mixin.', name);

          Constructor[name] = createMergedResultFunction(Constructor[name], property);
          return;
        }

        Constructor[name] = property;
      }
    }

    function mergeIntoWithNoDuplicateKeys(one, two) {
      _invariant(one && two && typeof one === 'object' && typeof two === 'object', 'mergeIntoWithNoDuplicateKeys(): Cannot merge non-objects.');

      for (var key in two) {
        if (two.hasOwnProperty(key)) {
          _invariant(one[key] === undefined, 'mergeIntoWithNoDuplicateKeys(): ' + 'Tried to merge two objects with the same key: `%s`. This conflict ' + 'may be due to a mixin; in particular, this may be caused by two ' + 'getInitialState() or getDefaultProps() methods returning objects ' + 'with clashing keys.', key);

          one[key] = two[key];
        }
      }

      return one;
    }

    function createMergedResultFunction(one, two) {
      return function mergedResult() {
        var a = one.apply(this, arguments);
        var b = two.apply(this, arguments);

        if (a == null) {
          return b;
        } else if (b == null) {
          return a;
        }

        var c = {};
        mergeIntoWithNoDuplicateKeys(c, a);
        mergeIntoWithNoDuplicateKeys(c, b);
        return c;
      };
    }

    function createChainedFunction(one, two) {
      return function chainedFunction() {
        one.apply(this, arguments);
        two.apply(this, arguments);
      };
    }

    function bindAutoBindMethod(component, method) {
      var boundMethod = method.bind(component);

      if (process.env.NODE_ENV !== 'production') {
        boundMethod.__reactBoundContext = component;
        boundMethod.__reactBoundMethod = method;
        boundMethod.__reactBoundArguments = null;
        var componentName = component.constructor.displayName;
        var _bind = boundMethod.bind;

        boundMethod.bind = function (newThis) {
          for (var _len = arguments.length, args = Array(_len > 1 ? _len - 1 : 0), _key = 1; _key < _len; _key++) {
            args[_key - 1] = arguments[_key];
          }

          if (newThis !== component && newThis !== null) {
            if (process.env.NODE_ENV !== 'production') {
              warning(false, 'bind(): React component methods may only be bound to the ' + 'component instance. See %s', componentName);
            }
          } else if (!args.length) {
            if (process.env.NODE_ENV !== 'production') {
              warning(false, 'bind(): You are binding a component method to the component. ' + 'React does this for you automatically in a high-performance ' + 'way, so you can safely remove this call. See %s', componentName);
            }

            return boundMethod;
          }

          var reboundMethod = _bind.apply(boundMethod, arguments);

          reboundMethod.__reactBoundContext = component;
          reboundMethod.__reactBoundMethod = method;
          reboundMethod.__reactBoundArguments = args;
          return reboundMethod;
        };
      }

      return boundMethod;
    }

    function bindAutoBindMethods(component) {
      var pairs = component.__reactAutoBindPairs;

      for (var i = 0; i < pairs.length; i += 2) {
        var autoBindKey = pairs[i];
        var method = pairs[i + 1];
        component[autoBindKey] = bindAutoBindMethod(component, method);
      }
    }

    var IsMountedPreMixin = {
      componentDidMount: function componentDidMount() {
        this.__isMounted = true;
      }
    };
    var IsMountedPostMixin = {
      componentWillUnmount: function componentWillUnmount() {
        this.__isMounted = false;
      }
    };
    var ReactClassMixin = {
      replaceState: function replaceState(newState, callback) {
        this.updater.enqueueReplaceState(this, newState, callback);
      },
      isMounted: function isMounted() {
        if (process.env.NODE_ENV !== 'production') {
          warning(this.__didWarnIsMounted, '%s: isMounted is deprecated. Instead, make sure to clean up ' + 'subscriptions and pending requests in componentWillUnmount to ' + 'prevent memory leaks.', this.constructor && this.constructor.displayName || this.name || 'Component');
          this.__didWarnIsMounted = true;
        }

        return !!this.__isMounted;
      }
    };

    var ReactClassComponent = function ReactClassComponent() {};

    _assign(ReactClassComponent.prototype, ReactComponent.prototype, ReactClassMixin);

    function createClass(spec) {
      var Constructor = identity(function (props, context, updater) {
        if (process.env.NODE_ENV !== 'production') {
          warning(this instanceof Constructor, 'Something is calling a React component directly. Use a factory or ' + 'JSX instead. See: https://fb.me/react-legacyfactory');
        }

        if (this.__reactAutoBindPairs.length) {
          bindAutoBindMethods(this);
        }

        this.props = props;
        this.context = context;
        this.refs = emptyObject;
        this.updater = updater || ReactNoopUpdateQueue;
        this.state = null;
        var initialState = this.getInitialState ? this.getInitialState() : null;

        if (process.env.NODE_ENV !== 'production') {
          if (initialState === undefined && this.getInitialState._isMockFunction) {
            initialState = null;
          }
        }

        _invariant(typeof initialState === 'object' && !Array.isArray(initialState), '%s.getInitialState(): must return an object or null', Constructor.displayName || 'ReactCompositeComponent');

        this.state = initialState;
      });
      Constructor.prototype = new ReactClassComponent();
      Constructor.prototype.constructor = Constructor;
      Constructor.prototype.__reactAutoBindPairs = [];
      injectedMixins.forEach(mixSpecIntoComponent.bind(null, Constructor));
      mixSpecIntoComponent(Constructor, IsMountedPreMixin);
      mixSpecIntoComponent(Constructor, spec);
      mixSpecIntoComponent(Constructor, IsMountedPostMixin);

      if (Constructor.getDefaultProps) {
        Constructor.defaultProps = Constructor.getDefaultProps();
      }

      if (process.env.NODE_ENV !== 'production') {
        if (Constructor.getDefaultProps) {
          Constructor.getDefaultProps.isReactClassApproved = {};
        }

        if (Constructor.prototype.getInitialState) {
          Constructor.prototype.getInitialState.isReactClassApproved = {};
        }
      }

      _invariant(Constructor.prototype.render, 'createClass(...): Class specification must implement a `render` method.');

      if (process.env.NODE_ENV !== 'production') {
        warning(!Constructor.prototype.componentShouldUpdate, '%s has a method called ' + 'componentShouldUpdate(). Did you mean shouldComponentUpdate()? ' + 'The name is phrased as a question because the function is ' + 'expected to return a value.', spec.displayName || 'A component');
        warning(!Constructor.prototype.componentWillRecieveProps, '%s has a method called ' + 'componentWillRecieveProps(). Did you mean componentWillReceiveProps()?', spec.displayName || 'A component');
        warning(!Constructor.prototype.UNSAFE_componentWillRecieveProps, '%s has a method called UNSAFE_componentWillRecieveProps(). ' + 'Did you mean UNSAFE_componentWillReceiveProps()?', spec.displayName || 'A component');
      }

      for (var methodName in ReactClassInterface) {
        if (!Constructor.prototype[methodName]) {
          Constructor.prototype[methodName] = null;
        }
      }

      return Constructor;
    }

    return createClass;
  }

  module.exports = factory;
},"c4d7c5b2fcc6d490ff5514c0f74aaa0b",["8af2bf7b94417b5c71327646f65c8821","da32f9ead03e97a13bff4dea57a4330e","8940a4ad43b101ffc23e725363c70f8d","09babf511a081d9520406a63f452d2ef"],"node_modules/create-react-class/factory.js");