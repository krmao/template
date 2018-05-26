__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var EventValidator = {
    addValidation: function addValidation(emitter, types) {
      var eventTypes = Object.keys(types);
      var emitterWithValidation = Object.create(emitter);
      babelHelpers.extends(emitterWithValidation, {
        emit: function emit(type, a, b, c, d, e, _) {
          assertAllowsEventType(type, eventTypes);
          return emitter.emit.call(this, type, a, b, c, d, e, _);
        }
      });
      return emitterWithValidation;
    }
  };

  function assertAllowsEventType(type, allowedTypes) {
    if (allowedTypes.indexOf(type) === -1) {
      throw new TypeError(errorMessageFor(type, allowedTypes));
    }
  }

  function errorMessageFor(type, allowedTypes) {
    var message = 'Unknown event type "' + type + '". ';

    if (__DEV__) {
      message += recommendationFor(type, allowedTypes);
    }

    message += 'Known event types: ' + allowedTypes.join(', ') + '.';
    return message;
  }

  if (__DEV__) {
    var recommendationFor = function recommendationFor(type, allowedTypes) {
      var closestTypeRecommendation = closestTypeFor(type, allowedTypes);

      if (isCloseEnough(closestTypeRecommendation, type)) {
        return 'Did you mean "' + closestTypeRecommendation.type + '"? ';
      } else {
        return '';
      }
    };

    var closestTypeFor = function closestTypeFor(type, allowedTypes) {
      var typeRecommendations = allowedTypes.map(typeRecommendationFor.bind(this, type));
      return typeRecommendations.sort(recommendationSort)[0];
    };

    var typeRecommendationFor = function typeRecommendationFor(type, recommendedType) {
      return {
        type: recommendedType,
        distance: damerauLevenshteinDistance(type, recommendedType)
      };
    };

    var recommendationSort = function recommendationSort(recommendationA, recommendationB) {
      if (recommendationA.distance < recommendationB.distance) {
        return -1;
      } else if (recommendationA.distance > recommendationB.distance) {
        return 1;
      } else {
        return 0;
      }
    };

    var isCloseEnough = function isCloseEnough(closestType, actualType) {
      return closestType.distance / actualType.length < 0.334;
    };

    var damerauLevenshteinDistance = function damerauLevenshteinDistance(a, b) {
      var i = void 0,
          j = void 0;
      var d = [];

      for (i = 0; i <= a.length; i++) {
        d[i] = [i];
      }

      for (j = 1; j <= b.length; j++) {
        d[0][j] = j;
      }

      for (i = 1; i <= a.length; i++) {
        for (j = 1; j <= b.length; j++) {
          var cost = a.charAt(i - 1) === b.charAt(j - 1) ? 0 : 1;
          d[i][j] = Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);

          if (i > 1 && j > 1 && a.charAt(i - 1) === b.charAt(j - 2) && a.charAt(i - 2) === b.charAt(j - 1)) {
            d[i][j] = Math.min(d[i][j], d[i - 2][j - 2] + cost);
          }
        }
      }

      return d[a.length][b.length];
    };
  }

  module.exports = EventValidator;
},310,[],"EventValidator");