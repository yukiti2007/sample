import React from 'react'
import logo from './logo.svg';
import UserStore, {UserContext} from "./components/context/UserStore";
import UserLogin from "./components/context/UserLogin";
import UserShow from "./components/context/UserShow";
import './App.css';

class App extends React.Component {
    constructor(props) {
        super(props);

        this.doLogin = (userInfo) => {  //  定义登录方法
            this.setState({
                userInfo: userInfo
            })
        };

        this.doLogout = () => {         //  定义登出方法
            this.setState({
                userInfo: UserStore.defaultUserInfo
            })
        }

        this.state = {
            userInfo: UserStore.defaultUserInfo,
            doLogin: this.doLogin,
            doLogout: this.doLogout,
        };
    }

    render() {
        return (
            <UserContext.Provider value={this.state}>   {/*将App的state绑定到Context上*/}
                <div className="App">
                    <header className="App-header">
                        <img src={logo} className="App-logo" alt="logo"/>
                        <UserLogin/>
                        <UserShow/>
                    </header>
                </div>
            </UserContext.Provider>
        );
    }
}

export default App
// ReactDOM.render(<App />, document.root);