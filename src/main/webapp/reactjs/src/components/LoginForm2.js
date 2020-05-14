import React, { Component } from 'react';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import {Card} from "react-bootstrap";
import axios from 'axios';

export default class LoginForm2 extends Component{

    emptyItem = {
        username: '',
        password: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;

        this.setState({item});
    }

         handleSubmit(event) {
             event.preventDefault();
             const {item} = this.state;

             console.log("Sta je data "+ item.password+item.username);
             axios( {
                 method: 'POST',
                 url:'http://localhost:8081/rest/login',
                 data:item,
                contentType:'application/json'
             }).then((response)=>{
                 if(response===200){
                     this.props.history.push('list');
                 }
             })
             this.props.history.push('list');
         }


    render() {
        const {item} = this.state;


        return <div>

            <Container>
                <Card className={"border border-dark bg-dark text-white"}>
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="username">Username</Label>
                        <Input type="text" name="username" id="username" value={item.username || ''}
                               onChange={this.handleChange}  className={"bg-dark text-white"} autoComplete="name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="password">Password</Label>
                        <Input type="password" name="password" id="password" value={item.password || ''}
                               onChange={this.handleChange}  className={"bg-dark text-white"} autoComplete="password-level1"/>
                    </FormGroup>

                    <FormGroup >
                        <Button color="primary" type="submit">Log in</Button>{' '}
                    </FormGroup>
                </Form>
                </Card>
            </Container>
        </div>
    }
}

