import React, {useState} from 'react';
import axios from "axios";
import {Helmet} from "react-helmet";

export default function LoginPage(props) {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function handleSubmit(event) {
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/login`,
            'data': {
                username: username,
                password: password
            }
        }).then(() => {
            props.history.push("/home");
        }).catch(() => {
            alert("Unsuccessful login!");
        });
        event.preventDefault();
    }

    function handleSwitchToRegister() {
        props.history.push("/register");
    }

    return (
        <div className="login">
            <Helmet>
                <meta charSet="utf-8"/>
                <title>Login to your account</title>
            </Helmet>
            <h2>
                Login to your account
            </h2>
            <form onSubmit={handleSubmit}>
                <div className="formRow">
                    <label>Username: </label>
                    <input
                        contentEditable="true"
                        type="text"
                        placeholder="Username"
                        name="username"
                        value={username}
                        onChange={e => setUsername(e.target.value)}/>
                </div>
                <div className="formRow">
                    <label>Password: </label>
                    <input
                        contentEditable="true"
                        type="password"
                        placeholder="Password"
                        name="password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}/>
                </div>
                <span>
                    <button type="submit" id="submit">Login</button>
                    <button onClick={handleSwitchToRegister} id="switchToRegister"
                            className="register">Register</button>
                </span>
            </form>
        </div>
    );

}
