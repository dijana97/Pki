import React, {Component} from 'react';

import {Card, Form, Button, Col} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faSave, faPlusSquare, faUndo, faList, faEdit} from '@fortawesome/free-solid-svg-icons';
import MyToast from './MyToast';
import axios from 'axios';

export default class Certificate extends Component {

    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state = {
            genres: [],
            languages : [],
            show : false
        };
        this.certificateChange = this.certificateChange.bind(this);
        this.submitCertificate = this.submitCertificate.bind(this);
    }

    initialState = {
        id:'', subject:'', issuer:'', startDate:'', endDate:'', name:'', surname:'',email:'',extension:'',type:''
    };

    componentDidMount() {
        const certificateId = +this.props.match.params.id;
        if(certificateId) {
            this.findCertificateById(certificateId);
        }
      //  this.findAllType();
    }

    findAllLanguages = () => {
        axios.get("http://localhost:8081/rest/certificates/languages")
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    languages: [{value:'', display:'Select Language'}]
                        .concat(data.map(language => {
                            return {value:language, display:language}
                        }))
                });
            });
    };

    findAllGenres = () => {
        axios.get("http://localhost:8081/rest/certificates/genres")
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    genres: [{value:'', display:'Select Type'}]
                        .concat(data.map(genre => {
                            return {value:genre, display:genre}
                        }))
                });
            });
    };


    findCertificateById = (certificateId) => {
        axios.get("http://localhost:8081/rest/certificates/"+certificateId)
            .then(response => {
                if(response.data != null) {
                    this.setState({
                        id: response.data.id,
                        subject: response.data.subject,
                        issuer: response.data.issuer,
                        startDate: response.data.startDate,
                        endDate: response.data.endDate,
                        language: response.data.language,
                        name: response.data.name,
                        surname: response.data.surname,
                        email: response.data.email,
                        type: response.data.type,
                        extension:response.data.type

                    });
                }
            }).catch((error) => {
                console.error("Error - "+error);
            });
    };

    resetCertificate = () => {
        this.setState(() => this.initialState);
    };


    submitCertificate = event => {
        event.preventDefault();

        const certificate = {

            subject: this.state.subject,
            issuer: this.state.issuer,
            startDate: this.state.startDate,
            endDate: this.state.endDate,
            extension: this.state.extension,
            name: this.state.name,
            surname: this.state.surname,
            email: this.state.email,
            type: this.state.type


        };

        axios.post("http://localhost:8081/rest/certificates", certificate)
            .then(response => {
                if(response.data != null) {
                    this.setState({"show":true, "method":"post"});
                    setTimeout(() => this.setState({"show":false}), 3000);
                } else {
                    this.setState({"show":false});
                }
            });

        this.setState(this.initialState);
    };

    updateCertificate = event => {
        event.preventDefault();

        const certificate = {
            id: this.state.id,
            subject: this.state.subject,
            issuer: this.state.issuer,
            startDate: this.state.startDate,
            endDate: this.state.endDate,
            name: this.state.name,
            surname: this.state.surname,
            email: this.state.email,
            type: this.state.type,
            extension: this.state.extension
        };

        axios.put("http://localhost:8081/rest/certificates", certificate)
            .then(response => {
                if(response.data != null) {
                    this.setState({"show":true, "method":"put"});
                    setTimeout(() => this.setState({"show":false}), 3000);
                    setTimeout(() => this.certificateList(), 3000);
                } else {
                    this.setState({"show":false});
                }
            });

        this.setState(this.initialState);
    };

    certificateChange = event => {
        this.setState({
            [event.target.name]:event.target.value
        });
    };

    certificateList = () => {
        return this.props.history.push("/list");
    };

    render() {
        const {subject, issuer, startDate, endDate, name, surname, email,type,extension} = this.state;

        return (
            <div>
                <div style={{"display":this.state.show ? "block" : "none"}}>
                    <MyToast show = {this.state.show} message = {this.state.method === "put" ? "Certificate Updated Successfully." : "Certificate Saved Successfully."} type = {"success"}/>
                </div>
                <Card className={"border border-dark bg-dark text-white"}>
                    <Card.Header>
                        <FontAwesomeIcon icon={this.state.id ? faEdit : faPlusSquare} /> {this.state.id ? "Update Certificate" : "Add New Certificate"}
                    </Card.Header>
                    <Form onReset={this.resetCerrtificate} onSubmit={this.state.id ? this.updateCertificate : this.submitCertificate} id="certificateFormId">
                        <Card.Body>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridSubject">
                                    <Form.Label>Subject</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="test" name="subject"
                                        value={subject} onChange={this.certificateChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter Certificate Subject" />
                                </Form.Group>
                                <Form.Group as={Col} controlId="formGridIssuer">
                                    <Form.Label>Issuer</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="test" name="issuer"
                                        value={issuer} onChange={this.certificateChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter Certificate Issuer" />
                                </Form.Group>
                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridCoverStartDate">
                                    <Form.Label>Start date</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="test" name="startDate"
                                        value={startDate} onChange={this.certificateChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter Certifiate start date" />
                                </Form.Group>
                                <Form.Group as={Col} controlId="formGridEndDate">
                                    <Form.Label>End date</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="test" name="endDate"
                                        value={endDate} onChange={this.certificateChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter Certificate ISBN Number" />
                                </Form.Group>
                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridName">
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="test" name="name"
                                        value={name} onChange={this.certificateChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter certificate name" />
                                </Form.Group>
                                <Form.Group as={Col} controlId="formGridSurname">
                                    <Form.Label>Surname</Form.Label>
                                    <Form.Control required autoComplete="off"
                                                  type="test" name="surname"
                                                  value={surname} onChange={this.certificateChange}
                                                  className={"bg-dark text-white"}
                                                  placeholder="Enter certificate surname" />
                                </Form.Group>
                            </Form.Row>
                                <Form.Row>
                                <Form.Group as={Col} controlId="formGridEmail">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control required autoComplete="off"
                                                  type="test" name="email"
                                                  value={email} onChange={this.certificateChange}
                                                  className={"bg-dark text-white"}
                                                  placeholder="Enter certificate name" />
                                </Form.Group>
                                    <Form.Group as={Col} controlId="formGridType">
                                        <Form.Label>Type</Form.Label>
                                        <Form.Control required autoComplete="off"
                                                      type="test" name="type"
                                                      value={type} onChange={this.certificateChange}
                                                      className={"bg-dark text-white"}
                                                      placeholder="Enter certificate type" />
                                    </Form.Group>
                            <Form.Group as={Col} controlId="formGridExtension">
                                <Form.Label>Extension</Form.Label>
                                <Form.Control required autoComplete="off"
                                              type="test" name="extension"
                                              value={extension} onChange={this.certificateChange}
                                              className={"bg-dark text-white"}
                                              placeholder="Enter certificate extension" />
                            </Form.Group>
                                </Form.Row>

                        </Card.Body>
                        <Card.Footer style={{"textAlign":"right"}}>
                            <Button size="sm" variant="success" type="submit">
                                <FontAwesomeIcon icon={faSave} /> {this.state.id ? "Update" : "Save"}
                            </Button>{' '}
                            <Button size="sm" variant="info" type="reset">
                                <FontAwesomeIcon icon={faUndo} /> Reset
                            </Button>{' '}
                            <Button size="sm" variant="info" type="button" onClick={this.certificateList.bind()}>
                                <FontAwesomeIcon icon={faList} /> Certificate List
                            </Button>
                        </Card.Footer>
                    </Form>
                </Card>
            </div>
        );
    }
}