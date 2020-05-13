import React from 'react';

import {Navbar, Nav} from 'react-bootstrap';
import {Link} from 'react-router-dom';
import axios from "axios";


export default function NavigationBar() {
    constructor(props)
    {
        this.logoutEvent = this.logoutEvent.bind(this)
    };
    logoutEvent()
    {
        axios.post("http://localhost:8081/rest/logout",admin)
            .then(response => {
                if(response.data != null) {
                    this.setState({"show":true, "method":"post"});
                    setTimeout(() => this.setState({"show":false}), 3000);
                } else {
                    this.setState({"show":false});
                }
            });

    }

    return (
        <Navbar bg="dark" variant="dark">
            <Link to={""} className="navbar-brand">
                <img src="https://w0.pngwave.com/png/507/808/computer-icons-academic-certificate-icon-design-skills-certificate-icon-png-clip-art.png" width="25" height="25" alt="brand"/> PKI
            </Link>
            <Nav className="mr-auto">
                <Link to={"add"} className="nav-link">Add Certificate</Link>
                <Link to={"list"} className="nav-link">Certificate List</Link>
            </Nav>
            <Nav className="justify-content-end" >
                <Link to={"login-url"} className="nav-link">Login</Link>
                <Link onClick={this.logoutEvent} to={"http://localhost:8081/rest/logout"} className="nav-link">Logout</Link>
            </Nav>
        </Navbar>
    );
}