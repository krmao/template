import React from "react";
import "./settings.scss";

class Settings extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: null
        };
    }

    render() {
        return (
            <div className="settings_root">
                <p>settings</p>
            </div>
        );
    }
}

export default Settings;
