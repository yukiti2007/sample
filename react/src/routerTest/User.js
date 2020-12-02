import React from 'react';
import {Link, Route, Switch,BrowserRouter} from "react-router-dom";
import Department from "./Department";

class User extends React.Component {

    users = {
        1: {name: "张三", departId: 1},
        2: {name: "李四", departId: 2},
        3: {name: "王五", departId: 1},
        4: {name: "赵六", departId: 2}
    }

    getUser = (userId) => {
        return this.users[userId] || {name: "用户Id错误", departId: 0}
    }

    render() {
        let url = this.props.match.url;
        let id = this.props.match.params.id;
        let user = this.getUser(id)

        return (
            <div>
                <div>
                    当前选择的组员：组员Id={id}，姓名={user.name}
                    <br/>
                    <Link to={`${url}/depart_${user.departId}`}>查看所在部门信息</Link>
                </div>

                <hr/>

                <Switch>
                    <Route path={`${url}/depart_:id`} component={Department}/>
                </Switch>
            </div>
        )
    }

}

export default User;