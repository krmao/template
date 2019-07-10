import React from "react";
import {HashRouter, Route, Switch} from "react-router-dom";

import Home from "./modules/home/home";
import Mine from "./modules/mine/mine";
import About from "./modules/about/about";
import Settings from "./modules/settings/settings";

const router = () => (
    <HashRouter>
        <Switch>
            <Route exact path="/" component={Home} />
            <Route exact path="/mine" component={Mine} />
            <Route exact path="/about" component={About} />
            <Route exact path="/settings" component={Settings} />
        </Switch>
    </HashRouter>
);

export default router;
