import React from 'react'
import {UserContext} from "./UserStore";

class UserShow extends React.Component {

    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {
        return (
            <UserContext.Consumer>
                {
                    ({userInfo}) => (
                        <div>{userInfo.userName} is loggedin. userId = {userInfo.userId}</div>
                    )
                }
            </UserContext.Consumer>
        )
    }
}

export default UserShow