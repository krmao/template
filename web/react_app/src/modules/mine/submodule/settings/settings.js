import React from "react";
import "./settings.css";

class Settings extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: null
        };
    }

    render() {
        return (
            <div>
                <p>settings</p>
            </div>
        );
    }
}

export default Settings;
