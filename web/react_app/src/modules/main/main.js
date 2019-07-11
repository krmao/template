import React from "react";
import "./main.css";
import {BrowserRouter as Router, withRouter, Link, Route, Switch} from "react-router-dom";
import Home from "../home/home";
import Mine from "../mine/mine";
import About from "../about/about";

class Main extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: null
        };
    }

    componentWillMount() {}

    componentDidMount() {}

    render() {
        return (
            <Router>
                <div className="root">
                    <div className="container">
                        <Switch>
                            <Route exact path={["/", "/home"]} component={Home} />
                            <Route
                                exact
                                path={["/mine", "/mine/settings", "/mine/loading"]}
                                component={withRouter((props) => (
                                    <Mine {...props} />
                                ))}
                            />
                            <Route exact path={"/about"} component={About} />
                        </Switch>
                    </div>
                    <ul className="main_menu">
                        <li className="menu_item">
                            <Link to="/home">home</Link>
                        </li>
                        <li className="menu_item">
                            <Link to="/mine">Mine</Link>
                        </li>
                        <li className="menu_item">
                            <Link to="/about">About</Link>
                        </li>
                    </ul>
                </div>
            </Router>
        );
    }
}

export default Main;
