import React from "react";
import "./mine.css";
import Repository from "../../repository/Repository";
import {Link, Route} from "react-router-dom";
import About from "./submodule/about/about";
import Settings from "./submodule/settings/settings";

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
        console.log(`${this.props.match.url}/settings`);
        console.log(`${this.props.match.path}/settings`);
    }

    render() {
        return (
            <div className="root">
                <div className="menu">
                    <Link to="/mine/about">关于</Link>
                    <Link to={`${this.props.match.url}/settings`}>设置</Link>
                </div>
                <div className="container">
                    <Route path="/mine/about" component={About} />
                    <Route path={`${this.props.match.path}/settings`} component={Settings} />
                </div>
            </div>
        );
    }
}

export default Mine;
