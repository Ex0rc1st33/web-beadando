import {Helmet} from "react-helmet";
import React, {useState} from "react";
import axios from "axios";

export default function RegisterPage(props) {

    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    function handleSubmit(event) {
        if (password !== confirmPassword) {
            alert("Unsuccessful registration!");
            return;
        }
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/register`,
            'data': {
                username: username,
                email: email,
                password: password,
                roles: ["ROLE_CLIENT"]
            }
        }).then(() => {
            alert("Successful registration!");
            props.history.push("/login");
        }).catch(() => {
            alert("Unsuccessful registration!");
        });
        event.preventDefault();
    }

    function handleSwitchToLogin() {
        props.history.push("/login");
    }

    return (
        <div className="register">
            <Helmet>
                <meta charSet="utf-8"/>
                <title>Register new account</title>
            </Helmet>
            <h2>
                Register new account
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
                    <label>Email: </label>
                    <input
                        contentEditable="true"
                        type="email"
                        placeholder="Email"
                        name="email"
                        value={email}
                        onChange={e => setEmail(e.target.value)}/>
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
                <div className="formRow">
                    <label>Confirm password: </label>
                    <input
                        contentEditable="true"
                        type="password"
                        placeholder="Confirm password"
                        name="confirmPassword"
                        value={confirmPassword}
                        onChange={e => setConfirmPassword(e.target.value)}/>
                </div>
                <span>
                    <button type="submit" id="register">Register</button>
                    <button onClick={handleSwitchToLogin} id="submit" className="register">Back</button>
                </span>
            </form>
        </div>
    );

}
