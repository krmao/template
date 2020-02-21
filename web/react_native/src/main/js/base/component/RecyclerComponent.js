import React from 'react';
import {findNodeHandle, requireNativeComponent, UIManager} from 'react-native';
import PropTypes from 'prop-types'; // https://reactjs.org/docs/typechecking-with-proptypes.html

const RCTRecyclerView = requireNativeComponent('RCTRecyclerView');

class RecyclerComponent extends React.Component {
    constructor(props) {
        super(props);
        this.onItemClicked = this.onItemClicked.bind(this);
        this.onRequestLoadMore = this.onRequestLoadMore.bind(this);
    }

    onItemClicked(event) {
        console.log("onItemClicked", "nativeEvent:", event.nativeEvent);
        this.props.onItemClicked(event)
    }

    onRequestLoadMore(event) {
        console.log("onRequestLoadMore", "nativeEvent:", event.nativeEvent);
        this.props.onRequestLoadMore(event)
    }

    callNativeShowMoreData(loadMoreData) {
        // noinspection JSUnresolvedFunction,JSUnresolvedVariable
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.getViewManagerConfig('RCTRecyclerView').Commands.showMoreData,
            loadMoreData,
        )
    }

    render() {
        return <RCTRecyclerView {...this.props} onItemClicked={this.onItemClicked} onRequestLoadMore={this.onRequestLoadMore}/>;
    }
}

/**
 * isRequired 没传递参数将会抛出警告
 */
RecyclerComponent.propTypes = {
    /**
     * @param spanCount   If orientation is vertical, spanCount is number of columns. If
     *                    orientation is horizontal, spanCount is number of rows.
     */
    spanCount: PropTypes.number.isRequired,
    /**
     * @param orientation VERTICAL==1 or HORIZONTAL==0
     */
    stickyHeaderViewHeight: PropTypes.number.isRequired,
    orientation: PropTypes.number.isRequired,
    initData: PropTypes.array.isRequired,
    onItemClicked: PropTypes.func.isRequired,
    onRequestLoadMore: PropTypes.func.isRequired,
};

/**
 *
 * 可以使用 defaultProps 来设置默认值解决由于没传递参数抛出的警告
 */
RecyclerComponent.defaultProps = {
    orientation: 1
};

// noinspection JSUnusedGlobalSymbols
export default RecyclerComponent;
