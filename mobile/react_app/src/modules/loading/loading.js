import React from "react";
import "./loading.scss";

class Loading extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: null
        };
    }

    render() {
        return (
            <div className="loading_root">
                <p>loading</p>
            </div>
        );
    }
}

export default Loading;
