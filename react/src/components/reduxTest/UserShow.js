import React from 'react'
import {connect} from 'react-redux'

class UserShow extends React.Component {

    constructor(props) {
        super(props);
        this.state = {}
    }

    render() {
        console.log("UserShow render => props=" + JSON.stringify(this.props) + " state=" + JSON.stringify(this.state))
        let userId = this.props.userId
        let userName = this.props.userName
        return (
            <div>
                {userName} is loggedin. userId = {userId}
            </div>
        )
    }
}

export default connect(
    (state) => {    // mapStateToProps
        console.log("UserShow mapStateToProps => " + JSON.stringify(state))
        return {
            userId: state.userInfo.userId,
            userName: state.userInfo.userName,
        }
    },
    (dispatch) => { // mapDispatchToProps
        console.log("UserShow mapDispatchToProps => " + JSON.stringify(dispatch))
        return {}
    }
)(UserShow)