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

                <button className="button" onClick={() => this.setState({value: "X"})}>
                    {this.state.value}
                </button>
            </div>
        );
    }
}

export default About;
