import React from 'react';

class Addr extends React.Component {
    render() {
        return (
            <input onChange={this.props.handleChange}/>     // 将handleChange熟悉绑定到onChange事件上
        );
    }
}

export default Addr;