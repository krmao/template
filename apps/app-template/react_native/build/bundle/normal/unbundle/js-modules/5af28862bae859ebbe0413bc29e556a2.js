__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var invariant = _require(_dependencyMap[0], 'fbjs/lib/invariant');

  var isEmpty = _require(_dependencyMap[1], 'isEmpty');

  var warning = _require(_dependencyMap[2], 'fbjs/lib/warning');

  function defaultGetRowData(dataBlob, sectionID, rowID) {
    return dataBlob[sectionID][rowID];
  }

  function defaultGetSectionHeaderData(dataBlob, sectionID) {
    return dataBlob[sectionID];
  }

  var ListViewDataSource = function () {
    function ListViewDataSource(params) {
      babelHelpers.classCallCheck(this, ListViewDataSource);
      invariant(params && typeof params.rowHasChanged === 'function', 'Must provide a rowHasChanged function.');
      this._rowHasChanged = params.rowHasChanged;
      this._getRowData = params.getRowData || defaultGetRowData;
      this._sectionHeaderHasChanged = params.sectionHeaderHasChanged;
      this._getSectionHeaderData = params.getSectionHeaderData || defaultGetSectionHeaderData;
      this._dataBlob = null;
      this._dirtyRows = [];
      this._dirtySections = [];
      this._cachedRowCount = 0;
      this.rowIdentities = [];
      this.sectionIdentities = [];
    }

    babelHelpers.createClass(ListViewDataSource, [{
      key: "cloneWithRows",
      value: function cloneWithRows(dataBlob, rowIdentities) {
        var rowIds = rowIdentities ? [[].concat(babelHelpers.toConsumableArray(rowIdentities))] : null;

        if (!this._sectionHeaderHasChanged) {
          this._sectionHeaderHasChanged = function () {
            return false;
          };
        }

        return this.cloneWithRowsAndSections({
          s1: dataBlob
        }, ['s1'], rowIds);
      }
    }, {
      key: "cloneWithRowsAndSections",
      value: function cloneWithRowsAndSections(dataBlob, sectionIdentities, rowIdentities) {
        invariant(typeof this._sectionHeaderHasChanged === 'function', 'Must provide a sectionHeaderHasChanged function with section data.');
        invariant(!sectionIdentities || !rowIdentities || sectionIdentities.length === rowIdentities.length, 'row and section ids lengths must be the same');
        var newSource = new ListViewDataSource({
          getRowData: this._getRowData,
          getSectionHeaderData: this._getSectionHeaderData,
          rowHasChanged: this._rowHasChanged,
          sectionHeaderHasChanged: this._sectionHeaderHasChanged
        });
        newSource._dataBlob = dataBlob;

        if (sectionIdentities) {
          newSource.sectionIdentities = sectionIdentities;
        } else {
          newSource.sectionIdentities = Object.keys(dataBlob);
        }

        if (rowIdentities) {
          newSource.rowIdentities = rowIdentities;
        } else {
          newSource.rowIdentities = [];
          newSource.sectionIdentities.forEach(function (sectionID) {
            newSource.rowIdentities.push(Object.keys(dataBlob[sectionID]));
          });
        }

        newSource._cachedRowCount = countRows(newSource.rowIdentities);

        newSource._calculateDirtyArrays(this._dataBlob, this.sectionIdentities, this.rowIdentities);

        return newSource;
      }
    }, {
      key: "getRowCount",
      value: function getRowCount() {
        return this._cachedRowCount;
      }
    }, {
      key: "getRowAndSectionCount",
      value: function getRowAndSectionCount() {
        return this._cachedRowCount + this.sectionIdentities.length;
      }
    }, {
      key: "rowShouldUpdate",
      value: function rowShouldUpdate(sectionIndex, rowIndex) {
        var needsUpdate = this._dirtyRows[sectionIndex][rowIndex];
        warning(needsUpdate !== undefined, 'missing dirtyBit for section, row: ' + sectionIndex + ', ' + rowIndex);
        return needsUpdate;
      }
    }, {
      key: "getRowData",
      value: function getRowData(sectionIndex, rowIndex) {
        var sectionID = this.sectionIdentities[sectionIndex];
        var rowID = this.rowIdentities[sectionIndex][rowIndex];
        warning(sectionID !== undefined && rowID !== undefined, 'rendering invalid section, row: ' + sectionIndex + ', ' + rowIndex);
        return this._getRowData(this._dataBlob, sectionID, rowID);
      }
    }, {
      key: "getRowIDForFlatIndex",
      value: function getRowIDForFlatIndex(index) {
        var accessIndex = index;

        for (var ii = 0; ii < this.sectionIdentities.length; ii++) {
          if (accessIndex >= this.rowIdentities[ii].length) {
            accessIndex -= this.rowIdentities[ii].length;
          } else {
            return this.rowIdentities[ii][accessIndex];
          }
        }

        return null;
      }
    }, {
      key: "getSectionIDForFlatIndex",
      value: function getSectionIDForFlatIndex(index) {
        var accessIndex = index;

        for (var ii = 0; ii < this.sectionIdentities.length; ii++) {
          if (accessIndex >= this.rowIdentities[ii].length) {
            accessIndex -= this.rowIdentities[ii].length;
          } else {
            return this.sectionIdentities[ii];
          }
        }

        return null;
      }
    }, {
      key: "getSectionLengths",
      value: function getSectionLengths() {
        var results = [];

        for (var ii = 0; ii < this.sectionIdentities.length; ii++) {
          results.push(this.rowIdentities[ii].length);
        }

        return results;
      }
    }, {
      key: "sectionHeaderShouldUpdate",
      value: function sectionHeaderShouldUpdate(sectionIndex) {
        var needsUpdate = this._dirtySections[sectionIndex];
        warning(needsUpdate !== undefined, 'missing dirtyBit for section: ' + sectionIndex);
        return needsUpdate;
      }
    }, {
      key: "getSectionHeaderData",
      value: function getSectionHeaderData(sectionIndex) {
        if (!this._getSectionHeaderData) {
          return null;
        }

        var sectionID = this.sectionIdentities[sectionIndex];
        warning(sectionID !== undefined, 'renderSection called on invalid section: ' + sectionIndex);
        return this._getSectionHeaderData(this._dataBlob, sectionID);
      }
    }, {
      key: "_calculateDirtyArrays",
      value: function _calculateDirtyArrays(prevDataBlob, prevSectionIDs, prevRowIDs) {
        var prevSectionsHash = keyedDictionaryFromArray(prevSectionIDs);
        var prevRowsHash = {};

        for (var ii = 0; ii < prevRowIDs.length; ii++) {
          var sectionID = prevSectionIDs[ii];
          warning(!prevRowsHash[sectionID], 'SectionID appears more than once: ' + sectionID);
          prevRowsHash[sectionID] = keyedDictionaryFromArray(prevRowIDs[ii]);
        }

        this._dirtySections = [];
        this._dirtyRows = [];
        var dirty;

        for (var sIndex = 0; sIndex < this.sectionIdentities.length; sIndex++) {
          var sectionID = this.sectionIdentities[sIndex];
          dirty = !prevSectionsHash[sectionID];
          var sectionHeaderHasChanged = this._sectionHeaderHasChanged;

          if (!dirty && sectionHeaderHasChanged) {
            dirty = sectionHeaderHasChanged(this._getSectionHeaderData(prevDataBlob, sectionID), this._getSectionHeaderData(this._dataBlob, sectionID));
          }

          this._dirtySections.push(!!dirty);

          this._dirtyRows[sIndex] = [];

          for (var rIndex = 0; rIndex < this.rowIdentities[sIndex].length; rIndex++) {
            var rowID = this.rowIdentities[sIndex][rIndex];
            dirty = !prevSectionsHash[sectionID] || !prevRowsHash[sectionID][rowID] || this._rowHasChanged(this._getRowData(prevDataBlob, sectionID, rowID), this._getRowData(this._dataBlob, sectionID, rowID));

            this._dirtyRows[sIndex].push(!!dirty);
          }
        }
      }
    }]);
    return ListViewDataSource;
  }();

  function countRows(allRowIDs) {
    var totalRows = 0;

    for (var sectionIdx = 0; sectionIdx < allRowIDs.length; sectionIdx++) {
      var rowIDs = allRowIDs[sectionIdx];
      totalRows += rowIDs.length;
    }

    return totalRows;
  }

  function keyedDictionaryFromArray(arr) {
    if (isEmpty(arr)) {
      return {};
    }

    var result = {};

    for (var ii = 0; ii < arr.length; ii++) {
      var key = arr[ii];
      warning(!result[key], 'Value appears more than once in array: ' + key);
      result[key] = true;
    }

    return result;
  }

  module.exports = ListViewDataSource;
},"5af28862bae859ebbe0413bc29e556a2",["8940a4ad43b101ffc23e725363c70f8d","22f4957a8dfdf9b2393880addce36ff6","09babf511a081d9520406a63f452d2ef"],"ListViewDataSource");