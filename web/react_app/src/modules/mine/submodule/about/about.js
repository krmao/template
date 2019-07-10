import React from "react";
import "./about.css";

class About extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: null
        };
    }

    render() {
        return (
            <div>
                <p>about</p>
            </div>
        );
    }
}

export default About;
