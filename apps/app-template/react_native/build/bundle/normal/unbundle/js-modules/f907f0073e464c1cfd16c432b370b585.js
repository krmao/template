__d(function (global, _require, module, exports, _dependencyMap) {
    (function (global, factory) {
        typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() : typeof define === 'function' && define.amd ? define(factory) : global.hoistNonReactStatics = factory();
    })(this, function () {
        'use strict';

        var REACT_STATICS = {
            childContextTypes: true,
            contextTypes: true,
            defaultProps: true,
            displayName: true,
            getDefaultProps: true,
            getDerivedStateFromProps: true,
            mixins: true,
            propTypes: true,
            type: true
        };
        var KNOWN_STATICS = {
            name: true,
            length: true,
            prototype: true,
            caller: true,
            callee: true,
            arguments: true,
            arity: true
        };
        var defineProperty = Object.defineProperty;
        var getOwnPropertyNames = Object.getOwnPropertyNames;
        var getOwnPropertySymbols = Object.getOwnPropertySymbols;
        var getOwnPropertyDescriptor = Object.getOwnPropertyDescriptor;
        var getPrototypeOf = Object.getPrototypeOf;
        var objectPrototype = getPrototypeOf && getPrototypeOf(Object);
        return function hoistNonReactStatics(targetComponent, sourceComponent, blacklist) {
            if (typeof sourceComponent !== 'string') {
                if (objectPrototype) {
                    var inheritedComponent = getPrototypeOf(sourceComponent);

                    if (inheritedComponent && inheritedComponent !== objectPrototype) {
                        hoistNonReactStatics(targetComponent, inheritedComponent, blacklist);
                    }
                }

                var keys = getOwnPropertyNames(sourceComponent);

                if (getOwnPropertySymbols) {
                    keys = keys.concat(getOwnPropertySymbols(sourceComponent));
                }

                for (var i = 0; i < keys.length; ++i) {
                    var key = keys[i];

                    if (!REACT_STATICS[key] && !KNOWN_STATICS[key] && (!blacklist || !blacklist[key])) {
                        var descriptor = getOwnPropertyDescriptor(sourceComponent, key);

                        try {
                            defineProperty(targetComponent, key, descriptor);
                        } catch (e) {}
                    }
                }

                return targetComponent;
            }

            return targetComponent;
        };
    });
},"f907f0073e464c1cfd16c432b370b585",[],"node_modules/hoist-non-react-statics/index.js");