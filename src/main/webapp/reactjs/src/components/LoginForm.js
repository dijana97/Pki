import React, {Component} from 'react';

import { Form,Button} from 'react-bootstrap';
import axios from 'axios';




export default class LoginForm extends Component {


    constructor(props){
        super(props);
        this.state={
            username:'',
            password:''
        }
    }

    initialState = {
        username:'', password:''
    };

    handleClick(event){
        var apiBaseUrl = "http://localhost:8081/rest/login/";
        var self = this;
        var payload={
            "username":this.state.username,
            "password":this.state.password
        }
        axios.post(apiBaseUrl+'logindata', payload)
            .then(function (response) {
                console.log(response);
                if(response.data.code === 200){
                    console.log("Login successfull");
                    var uploadScreen=[];

                    self.props.appContext.setState({loginPage:[],uploadScreen:uploadScreen})
                }
                else if(response.data.code === 204){
                    console.log("Username password do not match");
                    alert("username password do not match")
                }
                else{
                    console.log("Username does not exists");
                    alert("Username does not exist");
                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }


    render() {
        return (

            <div>
                <div>
                    <Form>
                    <Form.Label>Username</Form.Label>
                    <Form.Control
                        name="username" value={this.username}
                        hintText="Enter your Username"
                        floatingLabelText="Username"
                        onChange = {(event,newValue) => this.setState({username:newValue})}
                    />
                    <br/>
                    <Form.Label>Password</Form.Label>
                    <Form.Control
                        name="password" value={this.password}
                        type="password"
                        hintText="Enter your Password"
                        floatingLabelText="Password"
                        onChange = {(event,newValue) => this.setState({password:newValue})}
                    />
                    </Form>
                    <br/>
                    <Button  height='15' width='15' variant="success" label="Submit" primary={true}  onClick={(event) => this.handleClick(event)}/>
                </div>
            </div>

        );
    }
}
