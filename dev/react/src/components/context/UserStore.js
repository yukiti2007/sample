import React from 'react';

const UserStore = {

    // 定义存储的数据结构
    defaultUserInfo: {
        userId: 0,
        userName: "guest",
    },


}
export default UserStore

export const UserContext = React.createContext({userInfo: {}});