__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var React = _require(_dependencyMap[0], 'react');

  var factory = _require(_dependencyMap[1], './factory');

  if (typeof React === 'undefined') {
    throw Error('create-react-class could not find the React object. If you are using script tags, ' + 'make sure that React is being loaded before create-react-class.');
  }

  var ReactNoopUpdateQueue = new React.Component().updater;
  module.exports = factory(React.Component, React.isValidElement, ReactNoopUpdateQueue);
},"29cb0e104e5fce198008f3e789631772",["c42a5e17831e80ed1e1c8cf91f5ddb40","c4d7c5b2fcc6d490ff5514c0f74aaa0b"],"node_modules/create-react-class/index.js");