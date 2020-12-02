import React from 'react';

class NameShow2 extends React.Component {

    constructor(props) {
        super(props);
        this.state = {      // 注意1：此处需先对state进行初始化，不然编译会报错“TypeError: Cannot read property 'myName' of null”
            myName: ""      // 注意2：此处需要将变量在state中进行定义，否则会报警告，具体原因可以参考：https://reactjs.org/docs/forms.html#controlled-components
        }
        this.handleChange = this.handleChange.bind(this)
    }

    // handleChange = (event) => {
    //     console.log(event.target.value)
    //     // this.state.myName = event.target.value       // 注意3：此处直接赋值是无效的，一定要使用this的setState方法
    //     this.setState(
    //         {myName: event.target.value}
    //     )
    // }

    handleChange(event) {
        console.log(event.target.value)
        // this.state.myName = event.target.value       // 注意3：此处直接赋值是无效的，一定要使用this的setState方法
        this.setState(
            {myName: event.target.value}
        )
    }

    render() {
        return (
            <div>
                {/*尝试将myName变量绑定到input框上*/}
                <input value={this.state.myName} onChange={this.handleChange}/>
                {/*获取myName，并直接显示在页面上*/}
                <div>My Name Is {this.state.myName}</div>
            </div>
        )
    }
}

export default NameShow2