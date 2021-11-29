import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Switch, Redirect} from "react-router-dom";
import store from "./redux/store";
import {Provider} from 'react-redux';
import HomePage from './home/HomePage';
import LoginPage from './login/LoginPage';
import RegisterPage from './register/RegisterPage';
import "./login/loginregister.scss";
import "./home/home.scss";

ReactDOM.render(
    <Provider store={store}>
        <BrowserRouter>
            <Switch>
                <Route exact path="/login" component={LoginPage}/>
                <Route exact path="/register" component={RegisterPage}/>
                <Route exact path="/home" component={HomePage}/>
                <Redirect from="/" to="/login"/>
            </Switch>
        </BrowserRouter>
    </Provider>,
    document.getElementById('root')
);