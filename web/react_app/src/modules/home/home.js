import React from "react";
import {Link, withRouter} from "react-router-dom";

import logo from "../../logo.svg";
import "./home.css";

function home() {
    return (
        <div className="home">
            <header className="home-header">
                <img src={logo} className="home-logo" alt="logo" />
                <Link className="home-link" to="/mine">
                    Jump To Mine
                </Link>
                <Link className="home-link" to="/mine/settings">
                    Jump To Settings
                </Link>
            </header>
        </div>
    );
}

export default home;
