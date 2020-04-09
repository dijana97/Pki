import React from 'react';
import './App.css';

import {Container, Row, Col} from 'react-bootstrap';
import {BrowserRouter as Router, Switch, Route} from 'react-router-dom';

import NavigationBar from './components/NavigationBar';
import Welcome from './components/Welcome';
import Certificate from './components/Certificate';
import CertificateList from './components/CertificateList';
import UserList from './components/UserList';


export default function App() {

  const heading = "Public key infrastructure";

  return (
    <Router>
        <NavigationBar/>
        <Container>
            <Row>
                <Col lg={12} className={"margin-top"}>
                    <Switch>
                        <Route path="/" exact component={() => <Welcome heading={heading}/>}/>
                        <Route path="/add" exact component={Certificate}/>
                        <Route path="/edit/:id" exact component={Certificate}/>
                        <Route path="/list" exact component={CertificateList}/>
                        <Route path="/users" exact component={UserList}/>
                    </Switch>
                </Col>
            </Row>
        </Container>
    </Router>
  );
}
