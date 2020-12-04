import React from 'react';
import {Link, Route, Switch} from "react-router-dom";
import User from "./User";

class Department extends React.Component {

    departs = {
        1: {name: "开发一部", users: [1, 3]},
        2: {name: "开发二部", users: [2, 4]}
    }

    getDepart = (departId) => {
        return this.departs[departId] || {name: "部门Id错误", users: []}
    }

    jumpout = () => {
        this.props.history.push("http://www.baidu.com")
    }

    render() {
        let url = this.props.match.url;
        let id = this.props.match.params.id;
        let depart = this.getDepart(id)

        return (
            <div>
                <div>
                    当前选择的部门：部门Id={id}，部门名：{depart.name}
                    <br/>

                    <ul>
                        查询当前部门下的人员信息：
                        {
                            depart.users.map((userId) => (
                                <li key={`user_${userId}`}><Link to={`${url}/user_${userId}`}>Id：{userId}</Link></li>
                            ))
                        }
                    </ul>
                    <br/>
                    <a href={'#'} onClick={this.jumpout}>www.baidu.com</a>
                </div>

                <hr/>

                <Switch>
                    <Route path={`${url}/user_:id`} component={User}/>
                </Switch>
            </div>
        )
    }

}

export default Department;