__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var ListViewDataSource = _require(_dependencyMap[0], 'ListViewDataSource');

  var SwipeableListViewDataSource = function () {
    function SwipeableListViewDataSource(params) {
      var _this = this;

      babelHelpers.classCallCheck(this, SwipeableListViewDataSource);
      this._dataSource = new ListViewDataSource({
        getRowData: params.getRowData,
        getSectionHeaderData: params.getSectionHeaderData,
        rowHasChanged: function rowHasChanged(row1, row2) {
          return row1.id !== _this._previousOpenRowID && row2.id === _this._openRowID || row1.id === _this._previousOpenRowID && row2.id !== _this._openRowID || params.rowHasChanged(row1, row2);
        },
        sectionHeaderHasChanged: params.sectionHeaderHasChanged
      });
    }

    babelHelpers.createClass(SwipeableListViewDataSource, [{
      key: "cloneWithRowsAndSections",
      value: function cloneWithRowsAndSections(dataBlob, sectionIdentities, rowIdentities) {
        this._dataSource = this._dataSource.cloneWithRowsAndSections(dataBlob, sectionIdentities, rowIdentities);
        this._dataBlob = dataBlob;
        this.rowIdentities = this._dataSource.rowIdentities;
        this.sectionIdentities = this._dataSource.sectionIdentities;
        return this;
      }
    }, {
      key: "getDataSource",
      value: function getDataSource() {
        return this._dataSource;
      }
    }, {
      key: "getOpenRowID",
      value: function getOpenRowID() {
        return this._openRowID;
      }
    }, {
      key: "getFirstRowID",
      value: function getFirstRowID() {
        if (this.rowIdentities) {
          return this.rowIdentities[0] && this.rowIdentities[0][0];
        }

        return Object.keys(this._dataBlob)[0];
      }
    }, {
      key: "getLastRowID",
      value: function getLastRowID() {
        if (this.rowIdentities && this.rowIdentities.length) {
          var lastSection = this.rowIdentities[this.rowIdentities.length - 1];

          if (lastSection && lastSection.length) {
            return lastSection[lastSection.length - 1];
          }
        }

        return Object.keys(this._dataBlob)[this._dataBlob.length - 1];
      }
    }, {
      key: "setOpenRowID",
      value: function setOpenRowID(rowID) {
        this._previousOpenRowID = this._openRowID;
        this._openRowID = rowID;
        this._dataSource = this._dataSource.cloneWithRowsAndSections(this._dataBlob, this.sectionIdentities, this.rowIdentities);
        return this;
      }
    }]);
    return SwipeableListViewDataSource;
  }();

  module.exports = SwipeableListViewDataSource;
},301,[247],"SwipeableListViewDataSource");