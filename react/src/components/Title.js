import React from 'react';

class Title extends React.Component {
    render() {
        return <h1>Title : {this.props.title}</h1>;     // 直接从this.props中获取传入的参数值
    }
}

export default Title