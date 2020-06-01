import React from "react";
import "./mine.scss";
import {BrowserRouter as Router, Link, Route, Switch} from "react-router-dom";
import Settings from "../settings/settings";
import Loading from "../loading/loading";

class Mine extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: null
        };
    }

    componentWillMount() {}

    componentDidMount() {
        console.log("mine props=", this.props);
        console.log("mine props.match.path=", this.props.match.path);
    }

    render() {
        return (
            <Router>
                <div className="mine_root">
                    <ul className="menu">
                        <li className="menu_item">
                            <Link to="/mine/loading">loading</Link>
                        </li>
                        <li className="menu_item">
                            <Link to="/mine/settings">settings</Link>
                        </li>
                    </ul>
                    <div className="container">
                        <Switch>
                            <Route exact path={["/mine", "/mine/loading"]} component={Loading} />
                            <Route exact path={"/mine/settings"} component={Settings} />
                            <Route render={() => <div>Not Found</div>} />
                        </Switch>
                    </div>
                </div>
            </Router>
        );
    }
}

export default Mine;
