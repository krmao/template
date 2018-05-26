__d(function (global, _require, module, exports, _dependencyMap) {
  Object.defineProperty(exports, "__esModule", {
    value: true
  });

  function getSceneIndicesForInterpolationInputRange(props) {
    var scene = props.scene,
        scenes = props.scenes;
    var index = scene.index;
    var lastSceneIndexInScenes = scenes.length - 1;
    var isBack = !scenes[lastSceneIndexInScenes].isActive;

    if (isBack) {
      var currentSceneIndexInScenes = scenes.findIndex(function (item) {
        return item === scene;
      });
      var targetSceneIndexInScenes = scenes.findIndex(function (item) {
        return item.isActive;
      });
      var targetSceneIndex = scenes[targetSceneIndexInScenes].index;
      var lastSceneIndex = scenes[lastSceneIndexInScenes].index;

      if (index !== targetSceneIndex && currentSceneIndexInScenes === lastSceneIndexInScenes) {
        return {
          first: Math.min(targetSceneIndex, index - 1),
          last: index + 1
        };
      } else if (index === targetSceneIndex && currentSceneIndexInScenes === targetSceneIndexInScenes) {
        return {
          first: index - 1,
          last: Math.max(lastSceneIndex, index + 1)
        };
      } else if (index === targetSceneIndex || currentSceneIndexInScenes > targetSceneIndexInScenes) {
        return null;
      } else {
        return {
          first: index - 1,
          last: index + 1
        };
      }
    } else {
      return {
        first: index - 1,
        last: index + 1
      };
    }
  }

  exports.default = getSceneIndicesForInterpolationInputRange;
},366,[],"node_modules/react-navigation/src/utils/getSceneIndicesForInterpolationInputRange.js");