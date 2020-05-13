import React from 'react';

import {Navbar, Nav} from 'react-bootstrap';
import {Link} from 'react-router-dom';


export default function NavigationBar() {

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
                <Link to={"http://localhost:8081/rest/logout"} className="nav-link">Logout</Link>
            </Nav>
        </Navbar>
    );
}