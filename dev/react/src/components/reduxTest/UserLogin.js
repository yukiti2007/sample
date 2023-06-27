import React from 'react'
import {connect} from 'react-redux'
import UserStore from '../../store/UserStore'

class UserLogin extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            userName: "",
            userId: 0
        }

        this.handleNameChange = this.handleNameChange.bind(this)
        this.doLogin = this.doLogin.bind(this)
        this.doLogout = this.doLogout.bind(this)
    }

    handleNameChange(event) {
        this.setState(
            {
                userId: 9527,
                userName: event.target.value
            }
        )
    }

    doLogin(event) {
        this.props.doLogin(this.state)
    }

    doLogout(event) {
        this.props.doLogout()
    }

    render() {
        console.log("UserLogin render => props=" + JSON.stringify(this.props) + " state=" + JSON.stringify(this.state))

        let userName = this.state.userName

        return (
            <div>
                <input value={userName} onChange={this.handleNameChange}/>
                <button onClick={this.doLogin}>登录</button>
                <button onClick={this.doLogout}>登出</button>
            </div>
        )
    }
}

export default connect(
    (state) => {    // mapStateToProps
        console.log("UserLogin mapStateToProps => " + JSON.stringify(state))
        return {}
    },
    (dispatch) => { // mapDispatchToProps
        console.log("UserLogin mapDispatchToProps => " + JSON.stringify(dispatch))
        return {
            doLogin: (userInfo) => dispatch(UserStore.action.save(userInfo)),
            doLogout: () => dispatch(UserStore.action.remove()),
        }
    }
)(UserLogin)