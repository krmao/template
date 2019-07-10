import React from "react";
import "./mine.css";
import Repository from "../../repository/Repository";

class Mine extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: null
        };
    }

    componentWillMount() {
        Repository.getDirList(
            {userName: "krmao", userPwd: "123456"},
            function(data) {
                console.log("oh got success:", data);
            },
            function(error) {
                console.log("oh got error:", error);
            }
        );
    }

    componentDidMount() {
        console.log(this.props.match.params);
    }

    render() {
        return (
            <div className="root">
                <p>mine</p>

                <button className="button" onClick={() => this.setState({value: "X"})}>
                    {this.state.value}
                </button>
            </div>
        );
    }
}

export default Mine;
