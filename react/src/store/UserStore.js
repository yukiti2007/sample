const UserStore = {

    // 2-1、定义动作名称
    ACTION_SAVE: "user_save",
    ACTION_REMOVE: "user_remove",

    // 2-2、定义存储的数据结构
    defaultUserInfo: {
        userId: "",
        userName: "",
    },

    // 2-3、定义动作及其内容
    action: {
        save: (userInfo) => {
            return {
                type: UserStore.ACTION_SAVE,
                userInfo: userInfo,
            }
        },
        remove: () => {
            return {
                type: UserStore.ACTION_REMOVE
            }
        }
    },

    // 2-4、定义响应动作具体的操作内容
    reducer: (state = UserStore.defaultUserInfo, action) => {
        console.log("UserStore reducer => " + JSON.stringify(action))
        switch (action.type) {
            case UserStore.ACTION_SAVE:
                return action.userInfo || {};
            case UserStore.ACTION_REMOVE:
                return UserStore.defaultUserInfo;
            default:
                return state
        }
    }
}

export default UserStore