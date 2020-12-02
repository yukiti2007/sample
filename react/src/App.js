import './App.css';
import React from "react";
import {BrowserRouter as Router, Link, Route, Switch} from "react-router-dom";
import Department from "./routerTest/Department";
import User from "./routerTest/User";

function App() {

    let a = 0

    let departs = [1, 2]
    let users = [1, 2, 3, 4]

    let getConfirmation = (message, callback) => {
        console.log(`${message} ${++a}`)
        // callback(allowTransition)
    }

    return (

        <Router getUserConfirmation={getConfirmation('Are you sure?', null)}>
            <div>
                查询部门信息：
                <ul>
                    {departs.map((id) => (
                            <li key={`depart_${id}`}><Link to={`/depart_${id}`}> 部门Id：{id}</Link></li>
                        )
                    )}
                </ul>
                查询组员信息：
                <ul>
                    {users.map((id) => (
                            <li key={`user_${id}`}><Link to={`/user_${id}`}> 组员Id：{id}</Link></li>
                        )
                    )}
                </ul>

                <hr/>

                <Switch>
                    <Route path={`/depart_:id`} component={Department}/>
                    <Route path={`/user_:id`} component={User}/>
                </Switch>
            </div>
        </Router>
    );
}

export default App;