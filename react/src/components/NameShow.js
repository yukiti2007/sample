import React from 'react';

class NameShow extends React.Component {

    myName = ""             // 定义myName变量

    handleChange = (event) => {         // 此处需使用箭头函数，否则方法中的this将会识别为undefined
        console.log(event.target.value)
        this.myName = event.target.value
    }

    render() {
        return (
            <div>
                {/*尝试将myName变量绑定到input框上*/}
                <input value={this.myName} onChange={this.handleChange}/>
                {/*获取myName，并直接显示在页面上*/}
                <div>My Name Is {this.myName}</div>
            </div>
        )
    }
}

export default NameShow