__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var BatchedBridge = _require(_dependencyMap[0], 'BatchedBridge');

  var BugReporting = _require(_dependencyMap[1], 'BugReporting');

  var NativeModules = _require(_dependencyMap[2], 'NativeModules');

  var ReactNative = _require(_dependencyMap[3], 'ReactNative');

  var SceneTracker = _require(_dependencyMap[4], 'SceneTracker');

  var infoLog = _require(_dependencyMap[5], 'infoLog');

  var invariant = _require(_dependencyMap[6], 'fbjs/lib/invariant');

  var renderApplication = _require(_dependencyMap[7], 'renderApplication');

  var runnables = {};
  var runCount = 1;
  var sections = {};
  var tasks = new Map();

  var componentProviderInstrumentationHook = function componentProviderInstrumentationHook(component) {
    return component();
  };

  var wrapperComponentProvider = void 0;
  var AppRegistry = {
    setWrapperComponentProvider: function setWrapperComponentProvider(provider) {
      wrapperComponentProvider = provider;
    },
    registerConfig: function registerConfig(config) {
      config.forEach(function (appConfig) {
        if (appConfig.run) {
          AppRegistry.registerRunnable(appConfig.appKey, appConfig.run);
        } else {
          invariant(appConfig.component != null, 'AppRegistry.registerConfig(...): Every config is expected to set ' + 'either `run` or `component`, but `%s` has neither.', appConfig.appKey);
          AppRegistry.registerComponent(appConfig.appKey, appConfig.component, appConfig.section);
        }
      });
    },
    registerComponent: function registerComponent(appKey, componentProvider, section) {
      runnables[appKey] = {
        componentProvider: componentProvider,
        run: function run(appParameters) {
          return renderApplication(componentProviderInstrumentationHook(componentProvider), appParameters.initialProps, appParameters.rootTag, wrapperComponentProvider && wrapperComponentProvider(appParameters));
        }
      };

      if (section) {
        sections[appKey] = runnables[appKey];
      }

      return appKey;
    },
    registerRunnable: function registerRunnable(appKey, run) {
      runnables[appKey] = {
        run: run
      };
      return appKey;
    },
    registerSection: function registerSection(appKey, component) {
      AppRegistry.registerComponent(appKey, component, true);
    },
    getAppKeys: function getAppKeys() {
      return Object.keys(runnables);
    },
    getSectionKeys: function getSectionKeys() {
      return Object.keys(sections);
    },
    getSections: function getSections() {
      return babelHelpers.extends({}, sections);
    },
    getRunnable: function getRunnable(appKey) {
      return runnables[appKey];
    },
    getRegistry: function getRegistry() {
      return {
        sections: AppRegistry.getSectionKeys(),
        runnables: babelHelpers.extends({}, runnables)
      };
    },
    setComponentProviderInstrumentationHook: function setComponentProviderInstrumentationHook(hook) {
      componentProviderInstrumentationHook = hook;
    },
    runApplication: function runApplication(appKey, appParameters) {
      var msg = 'Running application "' + appKey + '" with appParams: ' + JSON.stringify(appParameters) + '. ' + '__DEV__ === ' + String(__DEV__) + ', development-level warning are ' + (__DEV__ ? 'ON' : 'OFF') + ', performance optimizations are ' + (__DEV__ ? 'OFF' : 'ON');
      infoLog(msg);
      BugReporting.addSource('AppRegistry.runApplication' + runCount++, function () {
        return msg;
      });
      invariant(runnables[appKey] && runnables[appKey].run, 'Application ' + appKey + ' has not been registered.\n\n' + "Hint: This error often happens when you're running the packager " + '(local dev server) from a wrong folder. For example you have ' + 'multiple apps and the packager is still running for the app you ' + 'were working on before.\nIf this is the case, simply kill the old ' + 'packager instance (e.g. close the packager terminal window) ' + 'and start the packager in the correct app folder (e.g. cd into app ' + "folder and run 'npm start').\n\n" + 'This error can also happen due to a require() error during ' + 'initialization or failure to call AppRegistry.registerComponent.\n\n');
      SceneTracker.setActiveScene({
        name: appKey
      });
      runnables[appKey].run(appParameters);
    },
    unmountApplicationComponentAtRootTag: function unmountApplicationComponentAtRootTag(rootTag) {
      ReactNative.unmountComponentAtNodeAndRemoveContainer(rootTag);
    },
    registerHeadlessTask: function registerHeadlessTask(taskKey, task) {
      if (tasks.has(taskKey)) {
        console.warn("registerHeadlessTask called multiple times for same key '" + taskKey + "'");
      }

      tasks.set(taskKey, task);
    },
    startHeadlessTask: function startHeadlessTask(taskId, taskKey, data) {
      var taskProvider = tasks.get(taskKey);

      if (!taskProvider) {
        throw new Error("No task registered for key " + taskKey);
      }

      taskProvider()(data).then(function () {
        return NativeModules.HeadlessJsTaskSupport.notifyTaskFinished(taskId);
      }).catch(function (reason) {
        console.error(reason);
        NativeModules.HeadlessJsTaskSupport.notifyTaskFinished(taskId);
      });
    }
  };
  BatchedBridge.registerCallableModule('AppRegistry', AppRegistry);
  module.exports = AppRegistry;
},"678a9708f2b8f1301d32e61850b31874",["fb53f04427490b2bd8fd29ce5c52844e","432f03d473313e462d36aa91969cadef","ce21807d4d291be64fa852393519f6c8","1102b68d89d7a6aede9677567aa01362","860ea624e11af795a48395a46f27393b","ee890a01982619b17df5249a8a8fb462","8940a4ad43b101ffc23e725363c70f8d","edfdfc63759f727adf359906657551ca"],"AppRegistry");