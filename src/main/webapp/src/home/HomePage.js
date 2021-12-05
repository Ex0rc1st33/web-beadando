import React, {useEffect, useState} from 'react';
import axios from "axios";
import {Helmet} from "react-helmet";
import Modal from 'react-awesome-modal';

function HomePage(props) {

    const [user, setUser] = useState({});
    const [selectedId, setSelectedId] = useState(0);
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [completedTasks, setCompletedTasks] = useState([]);
    const [incompleteTasks, setIncompleteTasks] = useState([]);
    const [isPopupVisible, setIsPopupVisible] = useState(false);

    useEffect(() => {
        axios({
            'method': 'GET',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/me`
        }).then((res) => {
            setUser(res.data);
            axios({
                'method': 'POST',
                'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/list`,
                'data': res.data.username
            }).then((response) => {
                let completed = [];
                let incomplete = [];
                for (let i = 0; i < response.data.length; i++) {
                    if (response.data[i].isDone) {
                        completed.push(
                            <tr key={response.data[i].id}>
                                <td>{response.data[i].title}</td>
                                <td>{response.data[i].description}</td>
                                <td>{response.data[i].creationDate}</td>
                                {!response.data[i].isDone ?
                                    <td id="checkmark" onClick={() => handleDone(response.data[i])}><i
                                        className="fa fa-check"/></td> : <td hidden/>}
                                <td id="trashcan"
                                    onClick={() => handleDelete(response.data[i].id, res.data.username)}><i
                                    className="fa fa-trash-o"/></td>
                            </tr>
                        );
                    } else {
                        incomplete.push(
                            <tr key={response.data[i].id} onMouseEnter={() => handleHoverEnter(i)}
                                onMouseLeave={() => handleHoverLeave(i)}>
                                <td>{response.data[i].title}</td>
                                <td>{response.data[i].description}</td>
                                <td>{response.data[i].creationDate}</td>
                                {!response.data[i].isDone ?
                                    <td id="checkmark" onClick={() => handleDone(response.data[i])}><i
                                        className="fa fa-check"/></td> : <td hidden/>}
                                <td id="trashcan"
                                    onClick={() => handleDelete(response.data[i].id, res.data.username)}><i
                                    className="fa fa-trash-o"/></td>
                                <th style={{visibility: "hidden"}} id={"edit" + i}>
                                    <button className="edit" onClick={() => handleEdit(response.data[i])}>Edit</button>
                                </th>
                            </tr>
                        );
                    }
                }
                setCompletedTasks(completed);
                setIncompleteTasks(incomplete);
            }).catch((exception) => {
                if (exception.response.status === 500) {
                    handleError();
                }
            });
        }).catch((exception) => {
            if (exception.response.status === 500) {
                handleError();
            }
        });
    }, []);

    function handleSubmit(event) {
        if (document.getElementById("submit").innerText === "Create new task") {
            axios({
                'method': 'POST',
                'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/create`,
                'data': {
                    title: title,
                    description: description,
                    username: user.username
                }
            }).then((response) => {
                setIncompleteTasks([
                    <tr id={response.data.id} key={response.data.id}
                        onMouseEnter={() => handleHoverEnter(incompleteTasks.length)}
                        onMouseLeave={() => handleHoverLeave(incompleteTasks.length)}>
                        <td>{response.data.title}</td>
                        <td>{response.data.description}</td>
                        <td>{response.data.creationDate}</td>
                        {!response.data.isDone ?
                            <td id="checkmark" onClick={() => handleDone(response.data)}><i className="fa fa-check"/>
                            </td> : <td hidden/>}
                        <td id="trashcan" onClick={() => handleDelete(response.data.id, user.username)}><i
                            className="fa fa-trash-o"/>
                        </td>
                        <th style={{visibility: "hidden"}} id={"edit" + incompleteTasks.length}>
                            <button className="edit" onClick={() => handleEdit(response.data)}>Edit</button>
                        </th>
                    </tr>,
                    ...incompleteTasks
                ]);
            }).catch((exception) => {
                if (exception.response.status === 500) {
                    handleError();
                }
            });
            event.preventDefault();
        } else if (document.getElementById("submit").innerText === "Edit task") {
            axios({
                'method': 'POST',
                'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/update`,
                'data': {
                    id: selectedId,
                    title: title,
                    description: description,
                    creationDate: null,
                    isDone: false,
                    username: user.username
                }
            }).then(() => {
                window.location.reload(true);
            }).catch((exception) => {
                if (exception.response.status === 500) {
                    handleError();
                }
            });
        }
    }

    function handleDelete(id, username) {
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/delete`,
            'data': {
                id: id,
                title: null,
                description: null,
                creationDate: null,
                isDone: null,
                username: username
            }
        }).then(() => {
            window.location.reload(true);
        }).catch((exception) => {
            if (exception.response.status === 500) {
                handleError();
            }
        });
    }

    function handleDone(task) {
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/update`,
            'data': {
                id: task.id,
                title: task.title,
                description: task.description,
                creationDate: task.creationDate,
                isDone: true,
                username: user.username
            }
        }).then(() => {
            window.location.reload(true);
        }).catch((exception) => {
            if (exception.response.status === 500) {
                handleError();
            }
        });
    }

    function handleHoverEnter(index) {
        const id = "edit" + index;
        document.getElementById(id).style.visibility = "visible";
    }

    function handleHoverLeave(index) {
        const id = "edit" + index;
        document.getElementById(id).style.visibility = "hidden";
    }

    function handleEdit(task) {
        setSelectedId(task.id);
        setTitle(task.title);
        setDescription(task.description);
        document.getElementById("submit").innerText = "Edit task";
    }

    function handleClear(username) {
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/delete_all`,
            'data': username
        }).then(() => {
            window.location.reload(true);
        }).catch((exception) => {
            if (exception.response.status === 500) {
                handleError();
            }
        });
    }

    function handleLogout() {
        axios({
            'method': 'GET',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/logout`
        }).then((response) => {
            alert(response.data);
            props.history.push("/login");
        });
    }

    function handleError() {
        setIsPopupVisible(true);
    }

    function hidePopup() {
        setIsPopupVisible(false);
    }

    function handleRefresh() {
        axios({
            'method': 'GET',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/refresh`
        }).then(() => {
            hidePopup();
            window.location.reload(true);
        });
    }

    return (
        <div className="home">
            <div id="home">
                <Helmet>
                    <meta charSet="utf-8"/>
                    <title>Your TO-DO list</title>
                </Helmet>
                <h2>
                    Welcome, {user.username}!
                </h2>
                <form onSubmit={handleSubmit}>
                    <div className="formRow">
                        <label>Title: </label>
                        <input
                            contentEditable="true"
                            type="text"
                            placeholder="Title"
                            name="title"
                            value={title}
                            onChange={e => setTitle(e.target.value)}/>
                    </div>
                    <div className="formRow">
                        <label>Description: </label>
                        <input
                            contentEditable="true"
                            type="textarea"
                            placeholder="Description"
                            name="description"
                            value={description}
                            onChange={e => setDescription(e.target.value)}/>
                    </div>
                    <button type="submit" id="submit">Create new task</button>
                </form>
                <Modal visible={isPopupVisible} width="400" height="300" effect="fadeInUp" onClickAway={hidePopup}>
                    <div className="popup">
                        <div className="message">Session expired! Want to extend it or logout now?</div>
                        <div className="cont">
                            <button id="popup_refresh" onClick={handleRefresh}>Refresh token</button>
                            <button id="popup_logout" onClick={handleLogout}>Logout</button>
                        </div>
                    </div>
                </Modal>
                <table className="uncompleted">
                    <thead>
                    <tr>
                        <th>Uncompleted tasks</th>
                    </tr>
                    <tr>
                        <th>Task</th>
                        <th>Description</th>
                        <th>Created at</th>
                    </tr>
                    </thead>
                    <tbody>
                    {incompleteTasks}
                    </tbody>
                </table>
                <table className="completed">
                    <thead>
                    <tr>
                        <th>Completed tasks</th>
                    </tr>
                    <tr>
                        <th>Task</th>
                        <th>Description</th>
                        <th>Created at</th>
                    </tr>
                    </thead>
                    <tbody>
                    {completedTasks}
                    </tbody>
                </table>
                <span>
                    <button onClick={() => handleClear(user.username)} id="submit">Clear all tasks</button>
                    <button onClick={handleLogout} id="logout">Logout</button>
                </span>
            </div>
        </div>
    );
}

export default HomePage;
