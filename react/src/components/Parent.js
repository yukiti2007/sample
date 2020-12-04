import React from 'react'
import Son from "./Son";

class Parent extends React.Component {

    tmp = "";

    constructor(props) {
        super(props);
        // this.state = {myName: ""}
    }

    handleChange = (event) => {
        console.log(event.target.value)
        this.tmp = event.target.value
        // this.setState(
        //     {myName: event.target.value}
        // )
    }

    render() {
        return (
            <div>
                <input onChange={this.handleChange}/>
                <Son myName={this.tmp}/>
            </div>
        )
    }

}

export default Parent