import {combineReducers, createStore} from 'redux'
import UserStore from "./UserStore";

// 合并多个reducer
const reducers = combineReducers(
    {userInfo: UserStore.reducer}
)

// 创建store
const store = createStore(reducers);

export {store};

