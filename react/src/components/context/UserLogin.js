import React from 'react'
import {UserContext} from "./UserStore";

class UserLogin extends React.Component {

    static contextType = UserContext    // 接收UserContext对象

    constructor(props) {
        super(props);
        this.state = {
            userName: "",
            userId: 0
        }
        this.handleNameChange = this.handleNameChange.bind(this)
    }

    componentDidMount() {
        let value = this.context;
        this.setState(
            value.userInfo
        )
    }

    handleNameChange(event) {
        this.setState(
            {
                userId: 9527,
                userName: event.target.value
            }
        )
    }

    render() {
        return (
            <UserContext.Consumer>
                {({doLogin, doLogout}) => (
                    <div>
                        <input value={this.state.userName} onChange={this.handleNameChange}/>
                        <button onClick={() => doLogin(this.state)}>登录</button>
                        <button onClick={doLogout}>登出</button>
                    </div>
                )}
            </UserContext.Consumer>
        )
    }
}

UserLogin.contextType = UserContext     // 接收UserContext对象

export default UserLogin