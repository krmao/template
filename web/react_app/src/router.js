import React from "react";
import {HashRouter, Route, Switch} from "react-router-dom";

import Home from "./modules/home/home";
import Mine from "./modules/mine/mine";

const router = () => (
    <HashRouter>
        <Switch>
            <Route exact path="/" component={Home} />
            <Route exact path="/mine" component={Mine} />
        </Switch>
    </HashRouter>
);

export default router;
