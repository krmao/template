__d(function (global, _require, module, exports, _dependencyMap) {
  'use strict';

  var _jsxFileName = "/Users/maokangren/workspace/template/apps/app-template/react_native/node_modules/react-native/Libraries/Lists/VirtualizedSectionList.js";

  var React = _require(_dependencyMap[0], 'React');

  var View = _require(_dependencyMap[1], 'View');

  var VirtualizedList = _require(_dependencyMap[2], 'VirtualizedList');

  var invariant = _require(_dependencyMap[3], 'fbjs/lib/invariant');

  var VirtualizedSectionList = function (_React$PureComponent) {
    babelHelpers.inherits(VirtualizedSectionList, _React$PureComponent);
    babelHelpers.createClass(VirtualizedSectionList, [{
      key: "scrollToLocation",
      value: function scrollToLocation(params) {
        var index = params.itemIndex + 1;

        for (var ii = 0; ii < params.sectionIndex; ii++) {
          index += this.props.sections[ii].data.length + 2;
        }

        var toIndexParams = babelHelpers.extends({}, params, {
          index: index
        });

        this._listRef.scrollToIndex(toIndexParams);
      }
    }, {
      key: "getListRef",
      value: function getListRef() {
        return this._listRef;
      }
    }, {
      key: "_subExtractor",
      value: function _subExtractor(index) {
        var itemIndex = index;
        var defaultKeyExtractor = this.props.keyExtractor;

        for (var ii = 0; ii < this.props.sections.length; ii++) {
          var _section = this.props.sections[ii];

          var _key = _section.key || String(ii);

          itemIndex -= 1;

          if (itemIndex >= _section.data.length + 1) {
            itemIndex -= _section.data.length + 1;
          } else if (itemIndex === -1) {
            return {
              section: _section,
              key: _key + ':header',
              index: null,
              header: true,
              trailingSection: this.props.sections[ii + 1]
            };
          } else if (itemIndex === _section.data.length) {
            return {
              section: _section,
              key: _key + ':footer',
              index: null,
              header: false,
              trailingSection: this.props.sections[ii + 1]
            };
          } else {
            var _keyExtractor = _section.keyExtractor || defaultKeyExtractor;

            return {
              section: _section,
              key: _key + ':' + _keyExtractor(_section.data[itemIndex], itemIndex),
              index: itemIndex,
              leadingItem: _section.data[itemIndex - 1],
              leadingSection: this.props.sections[ii - 1],
              trailingItem: _section.data[itemIndex + 1],
              trailingSection: this.props.sections[ii + 1]
            };
          }
        }
      }
    }, {
      key: "_getSeparatorComponent",
      value: function _getSeparatorComponent(index, info) {
        info = info || this._subExtractor(index);

        if (!info) {
          return null;
        }

        var ItemSeparatorComponent = info.section.ItemSeparatorComponent || this.props.ItemSeparatorComponent;
        var SectionSeparatorComponent = this.props.SectionSeparatorComponent;
        var isLastItemInList = index === this.state.childProps.getItemCount() - 1;
        var isLastItemInSection = info.index === info.section.data.length - 1;

        if (SectionSeparatorComponent && isLastItemInSection) {
          return SectionSeparatorComponent;
        }

        if (ItemSeparatorComponent && !isLastItemInSection && !isLastItemInList) {
          return ItemSeparatorComponent;
        }

        return null;
      }
    }, {
      key: "_computeState",
      value: function _computeState(props) {
        var offset = props.ListHeaderComponent ? 1 : 0;
        var stickyHeaderIndices = [];
        var itemCount = props.sections.reduce(function (v, section) {
          stickyHeaderIndices.push(v + offset);
          return v + section.data.length + 2;
        }, 0);
        return {
          childProps: babelHelpers.extends({}, props, {
            renderItem: this._renderItem,
            ItemSeparatorComponent: undefined,
            data: props.sections,
            getItemCount: function getItemCount() {
              return itemCount;
            },
            getItem: getItem,
            keyExtractor: this._keyExtractor,
            onViewableItemsChanged: props.onViewableItemsChanged ? this._onViewableItemsChanged : undefined,
            stickyHeaderIndices: props.stickySectionHeadersEnabled ? stickyHeaderIndices : undefined
          })
        };
      }
    }]);

    function VirtualizedSectionList(props, context) {
      babelHelpers.classCallCheck(this, VirtualizedSectionList);

      var _this = babelHelpers.possibleConstructorReturn(this, (VirtualizedSectionList.__proto__ || Object.getPrototypeOf(VirtualizedSectionList)).call(this, props, context));

      _this._keyExtractor = function (item, index) {
        var info = _this._subExtractor(index);

        return info && info.key || String(index);
      };

      _this._convertViewable = function (viewable) {
        invariant(viewable.index != null, 'Received a broken ViewToken');

        var info = _this._subExtractor(viewable.index);

        if (!info) {
          return null;
        }

        var keyExtractor = info.section.keyExtractor || _this.props.keyExtractor;
        return babelHelpers.extends({}, viewable, {
          index: info.index,
          key: keyExtractor(viewable.item, info.index),
          section: info.section
        });
      };

      _this._onViewableItemsChanged = function (_ref) {
        var viewableItems = _ref.viewableItems,
            changed = _ref.changed;

        if (_this.props.onViewableItemsChanged) {
          _this.props.onViewableItemsChanged({
            viewableItems: viewableItems.map(_this._convertViewable, _this).filter(Boolean),
            changed: changed.map(_this._convertViewable, _this).filter(Boolean)
          });
        }
      };

      _this._renderItem = function (_ref2) {
        var item = _ref2.item,
            index = _ref2.index;

        var info = _this._subExtractor(index);

        if (!info) {
          return null;
        }

        var infoIndex = info.index;

        if (infoIndex == null) {
          var _section2 = info.section;

          if (info.header === true) {
            var _renderSectionHeader = _this.props.renderSectionHeader;
            return _renderSectionHeader ? _renderSectionHeader({
              section: _section2
            }) : null;
          } else {
            var _renderSectionFooter = _this.props.renderSectionFooter;
            return _renderSectionFooter ? _renderSectionFooter({
              section: _section2
            }) : null;
          }
        } else {
          var _renderItem = info.section.renderItem || _this.props.renderItem;

          var _SeparatorComponent = _this._getSeparatorComponent(index, info);

          invariant(_renderItem, 'no renderItem!');
          return React.createElement(ItemWithSeparator, {
            SeparatorComponent: _SeparatorComponent,
            LeadingSeparatorComponent: infoIndex === 0 ? _this.props.SectionSeparatorComponent : undefined,
            cellKey: info.key,
            index: infoIndex,
            item: item,
            leadingItem: info.leadingItem,
            leadingSection: info.leadingSection,
            onUpdateSeparator: _this._onUpdateSeparator,
            prevCellKey: (_this._subExtractor(index - 1) || {}).key,
            ref: function ref(_ref3) {
              _this._cellRefs[info.key] = _ref3;
            },
            renderItem: _renderItem,
            section: info.section,
            trailingItem: info.trailingItem,
            trailingSection: info.trailingSection,
            __source: {
              fileName: _jsxFileName,
              lineNumber: 279
            }
          });
        }
      };

      _this._onUpdateSeparator = function (key, newProps) {
        var ref = _this._cellRefs[key];
        ref && ref.updateSeparatorProps(newProps);
      };

      _this._cellRefs = {};

      _this._captureRef = function (ref) {
        _this._listRef = ref;
      };

      _this.state = _this._computeState(props);
      return _this;
    }

    babelHelpers.createClass(VirtualizedSectionList, [{
      key: "UNSAFE_componentWillReceiveProps",
      value: function UNSAFE_componentWillReceiveProps(nextProps) {
        this.setState(this._computeState(nextProps));
      }
    }, {
      key: "render",
      value: function render() {
        return React.createElement(VirtualizedList, babelHelpers.extends({}, this.state.childProps, {
          ref: this._captureRef,
          __source: {
            fileName: _jsxFileName,
            lineNumber: 368
          }
        }));
      }
    }]);
    return VirtualizedSectionList;
  }(React.PureComponent);

  VirtualizedSectionList.defaultProps = babelHelpers.extends({}, VirtualizedList.defaultProps, {
    data: []
  });

  var ItemWithSeparator = function (_React$Component) {
    babelHelpers.inherits(ItemWithSeparator, _React$Component);

    function ItemWithSeparator() {
      var _ref4;

      var _temp, _this2, _ret;

      babelHelpers.classCallCheck(this, ItemWithSeparator);

      for (var _len = arguments.length, args = Array(_len), _key2 = 0; _key2 < _len; _key2++) {
        args[_key2] = arguments[_key2];
      }

      return _ret = (_temp = (_this2 = babelHelpers.possibleConstructorReturn(this, (_ref4 = ItemWithSeparator.__proto__ || Object.getPrototypeOf(ItemWithSeparator)).call.apply(_ref4, [this].concat(args))), _this2), _this2.state = {
        separatorProps: {
          highlighted: false,
          leadingItem: _this2.props.item,
          leadingSection: _this2.props.leadingSection,
          section: _this2.props.section,
          trailingItem: _this2.props.trailingItem,
          trailingSection: _this2.props.trailingSection
        },
        leadingSeparatorProps: {
          highlighted: false,
          leadingItem: _this2.props.leadingItem,
          leadingSection: _this2.props.leadingSection,
          section: _this2.props.section,
          trailingItem: _this2.props.item,
          trailingSection: _this2.props.trailingSection
        }
      }, _this2._separators = {
        highlight: function highlight() {
          ['leading', 'trailing'].forEach(function (s) {
            return _this2._separators.updateProps(s, {
              highlighted: true
            });
          });
        },
        unhighlight: function unhighlight() {
          ['leading', 'trailing'].forEach(function (s) {
            return _this2._separators.updateProps(s, {
              highlighted: false
            });
          });
        },
        updateProps: function updateProps(select, newProps) {
          var _this2$props = _this2.props,
              LeadingSeparatorComponent = _this2$props.LeadingSeparatorComponent,
              cellKey = _this2$props.cellKey,
              prevCellKey = _this2$props.prevCellKey;

          if (select === 'leading' && LeadingSeparatorComponent) {
            _this2.setState(function (state) {
              return {
                leadingSeparatorProps: babelHelpers.extends({}, state.leadingSeparatorProps, newProps)
              };
            });
          } else {
            _this2.props.onUpdateSeparator(select === 'leading' && prevCellKey || cellKey, newProps);
          }
        }
      }, _temp), babelHelpers.possibleConstructorReturn(_this2, _ret);
    }

    babelHelpers.createClass(ItemWithSeparator, [{
      key: "UNSAFE_componentWillReceiveProps",
      value: function UNSAFE_componentWillReceiveProps(props) {
        var _this3 = this;

        this.setState(function (state) {
          return {
            separatorProps: babelHelpers.extends({}, _this3.state.separatorProps, {
              leadingItem: props.item,
              leadingSection: props.leadingSection,
              section: props.section,
              trailingItem: props.trailingItem,
              trailingSection: props.trailingSection
            }),
            leadingSeparatorProps: babelHelpers.extends({}, _this3.state.leadingSeparatorProps, {
              leadingItem: props.leadingItem,
              leadingSection: props.leadingSection,
              section: props.section,
              trailingItem: props.item,
              trailingSection: props.trailingSection
            })
          };
        });
      }
    }, {
      key: "updateSeparatorProps",
      value: function updateSeparatorProps(newProps) {
        this.setState(function (state) {
          return {
            separatorProps: babelHelpers.extends({}, state.separatorProps, newProps)
          };
        });
      }
    }, {
      key: "render",
      value: function render() {
        var _props = this.props,
            LeadingSeparatorComponent = _props.LeadingSeparatorComponent,
            SeparatorComponent = _props.SeparatorComponent,
            item = _props.item,
            index = _props.index,
            section = _props.section;
        var element = this.props.renderItem({
          item: item,
          index: index,
          section: section,
          separators: this._separators
        });
        var leadingSeparator = LeadingSeparatorComponent && React.createElement(LeadingSeparatorComponent, babelHelpers.extends({}, this.state.leadingSeparatorProps, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 489
          }
        }));
        var separator = SeparatorComponent && React.createElement(SeparatorComponent, babelHelpers.extends({}, this.state.separatorProps, {
          __source: {
            fileName: _jsxFileName,
            lineNumber: 492
          }
        }));
        return leadingSeparator || separator ? React.createElement(
          View,
          {
            __source: {
              fileName: _jsxFileName,
              lineNumber: 495
            }
          },
          leadingSeparator,
          element,
          separator
        ) : element;
      }
    }]);
    return ItemWithSeparator;
  }(React.Component);

  function getItem(sections, index) {
    if (!sections) {
      return null;
    }

    var itemIdx = index - 1;

    for (var ii = 0; ii < sections.length; ii++) {
      if (itemIdx === -1 || itemIdx === sections[ii].data.length) {
        return sections[ii];
      } else if (itemIdx < sections[ii].data.length) {
        return sections[ii].data[itemIdx];
      } else {
        itemIdx -= sections[ii].data.length + 2;
      }
    }

    return null;
  }

  module.exports = VirtualizedSectionList;
},"77576e8f76491543bfcecd3e2a2e853c",["e6db4f0efed6b72f641ef0ffed29569f","30a3b04291b6e1f01b778ff31271ccc5","acd0db067821acba68e31f8e1998a3c5","8940a4ad43b101ffc23e725363c70f8d"],"VirtualizedSectionList");