__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var BatchedBridge = _require(_dependencyMap[0], 'BatchedBridge');

  var EventEmitter = _require(_dependencyMap[1], 'EventEmitter');

  var Set = _require(_dependencyMap[2], 'Set');

  var TaskQueue = _require(_dependencyMap[3], 'TaskQueue');

  var infoLog = _require(_dependencyMap[4], 'infoLog');

  var invariant = _require(_dependencyMap[5], 'fbjs/lib/invariant');

  var keyMirror = _require(_dependencyMap[6], 'fbjs/lib/keyMirror');

  var _emitter = new EventEmitter();

  var DEBUG_DELAY = 0;
  var DEBUG = false;
  var InteractionManager = {
    Events: keyMirror({
      interactionStart: true,
      interactionComplete: true
    }),
    runAfterInteractions: function runAfterInteractions(task) {
      var tasks = [];
      var promise = new Promise(function (resolve) {
        _scheduleUpdate();

        if (task) {
          tasks.push(task);
        }

        tasks.push({
          run: resolve,
          name: 'resolve ' + (task && task.name || '?')
        });

        _taskQueue.enqueueTasks(tasks);
      });
      return {
        then: promise.then.bind(promise),
        done: function done() {
          if (promise.done) {
            return promise.done.apply(promise, arguments);
          } else {
            console.warn('Tried to call done when not supported by current Promise implementation.');
          }
        },
        cancel: function cancel() {
          _taskQueue.cancelTasks(tasks);
        }
      };
    },
    createInteractionHandle: function createInteractionHandle() {
      DEBUG && infoLog('create interaction handle');

      _scheduleUpdate();

      var handle = ++_inc;

      _addInteractionSet.add(handle);

      return handle;
    },
    clearInteractionHandle: function clearInteractionHandle(handle) {
      DEBUG && infoLog('clear interaction handle');
      invariant(!!handle, 'Must provide a handle to clear.');

      _scheduleUpdate();

      _addInteractionSet.delete(handle);

      _deleteInteractionSet.add(handle);
    },
    addListener: _emitter.addListener.bind(_emitter),
    setDeadline: function setDeadline(deadline) {
      _deadline = deadline;
    }
  };

  var _interactionSet = new Set();

  var _addInteractionSet = new Set();

  var _deleteInteractionSet = new Set();

  var _taskQueue = new TaskQueue({
    onMoreTasks: _scheduleUpdate
  });

  var _nextUpdateHandle = 0;
  var _inc = 0;

  var _deadline = -1;

  function _scheduleUpdate() {
    if (!_nextUpdateHandle) {
      if (_deadline > 0) {
        _nextUpdateHandle = setTimeout(_processUpdate, 0 + DEBUG_DELAY);
      } else {
        _nextUpdateHandle = setImmediate(_processUpdate);
      }
    }
  }

  function _processUpdate() {
    _nextUpdateHandle = 0;
    var interactionCount = _interactionSet.size;

    _addInteractionSet.forEach(function (handle) {
      return _interactionSet.add(handle);
    });

    _deleteInteractionSet.forEach(function (handle) {
      return _interactionSet.delete(handle);
    });

    var nextInteractionCount = _interactionSet.size;

    if (interactionCount !== 0 && nextInteractionCount === 0) {
      _emitter.emit(InteractionManager.Events.interactionComplete);
    } else if (interactionCount === 0 && nextInteractionCount !== 0) {
      _emitter.emit(InteractionManager.Events.interactionStart);
    }

    if (nextInteractionCount === 0) {
      while (_taskQueue.hasTasksToProcess()) {
        _taskQueue.processNext();

        if (_deadline > 0 && BatchedBridge.getEventLoopRunningTime() >= _deadline) {
          _scheduleUpdate();

          break;
        }
      }
    }

    _addInteractionSet.clear();

    _deleteInteractionSet.clear();
  }

  module.exports = InteractionManager;
},"000e46d8eda06a212663acb7d3fd4965",["fb53f04427490b2bd8fd29ce5c52844e","6ab4a8e3cdc2c08a793730abd92b6eff","a140e1090b5f286a36e432887e992109","260fc98af4b4410ef36f3fdcb630681c","ee890a01982619b17df5249a8a8fb462","8940a4ad43b101ffc23e725363c70f8d","61a2e2589ea976cf04a51cc242a466dd"],"InteractionManager");