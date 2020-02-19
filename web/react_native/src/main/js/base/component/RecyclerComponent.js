import React from 'react';
import {requireNativeComponent} from 'react-native';
import PropTypes from 'prop-types'; // https://reactjs.org/docs/typechecking-with-proptypes.html

const RCTRecyclerView = requireNativeComponent('RCTRecyclerView');

class RecyclerComponent extends React.Component {
    constructor(props) {
        super(props);
        this.onItemClicked = this.onItemClicked.bind(this);
    }

    onItemClicked(event) {
        console.log("onItemClicked", "nativeEvent:", event.nativeEvent)
        this.props.onItemClicked(event)
    }

    render() {
        return <RCTRecyclerView {...this.props} onItemClicked={this.onItemClicked}/>;
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
    spanCount: PropTypes.number,
    /**
     * @param orientation VERTICAL==1 or HORIZONTAL==0
     */
    orientation: PropTypes.number.isRequired,
    data: PropTypes.array.isRequired,
    onItemClicked: PropTypes.func.isRequired,
};

/**
 *
 * 可以使用 defaultProps 来设置默认值解决由于没传递参数抛出的警告
 */
RecyclerComponent.defaultProps = {
    spanCount: 2
};

// noinspection JSUnusedGlobalSymbols
export default RecyclerComponent;
