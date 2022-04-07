import React from "react";
import "./about.scss";

class About extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: null
        };
    }

    render() {
        return (
            <div className="about_root">
                <p>about</p>
            </div>
        );
    }
}

export default About;
