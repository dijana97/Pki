import React, {Component} from 'react';

import {Card, Form, Button, Col} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faSave, faPlusSquare, faUndo, faList, faEdit} from '@fortawesome/free-solid-svg-icons';
import MyToast from './MyToast';
import axios from 'axios';
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

export default class Certificate extends Component {

    constructor(props) {
        super(props);
        this.state = this.initialState;
        this.state = {
            genres: [],
            types : [],
            aims:[],
            show : false
        };
        this.certificateChange = this.certificateChange.bind(this);
        this.submitCertificate = this.submitCertificate.bind(this);
    }

    initialState = {
        id:'', subject:'', issuer:'',aim:'',aimroot:'', startDate:'', endDate:'', name:'', surname:'',email:'',extension:'',type:'',withdrawn:''
    };

    componentDidMount() {
        const certificateId = +this.props.match.params.id;
        if(certificateId) {
            this.findCertificateById(certificateId);
        }
        this.findAllTypes();
        this.aimRoot();
    }

    findAllTypes = () => {
        axios.get("http://localhost:8081/rest/certificates/type")
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    types: [{value:'', display:'Select Certificate Type'}]
                        .concat(data.map(type => {
                            return {value:type, display:type}
                        }))
                });
            });
    };

    aimRoot = () => {
        axios.get("http://localhost:8081/rest/certificates/aimroot")
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    aims: [{value:'', display:'Select Root Aim'}]
                        .concat(data.map(aimroot => {
                            return {value:aimroot, display:aimroot}
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
                        aim: response.data.aim,
                        aimroot: response.data.aimroot,
                        startDate: response.data.startDate,
                        endDate: response.data.endDate,
                        language: response.data.language,
                        name: response.data.name,
                        surname: response.data.surname,
                        email: response.data.email,
                        type: response.data.type,
                        extension:response.data.extension,
                        withdrawn:response.data.withdrawn

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
            aim: this.state.aim,
            aimroot: this.state.aimroot,
            startDate: this.state.startDate,
            endDate: this.state.endDate,
            extension: this.state.extension,
            name: this.state.name,
            surname: this.state.surname,
            email: this.state.email,
            type: this.state.type,
            withdrawn: false



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
        console.log(this.refs.check_me.checked);
        const certificate = {
            id: this.state.id,
            subject: this.state.subject,
            issuer: this.state.issuer,
            aim: this.state.aim,
            aimroot: this.state.aimroot,
            startDate: this.state.startDate,
            endDate: this.state.endDate,
            name: this.state.name,
            surname: this.state.surname,
            email: this.state.email,
            type: this.state.type,
            extension: this.state.extension,
            withdrawn: this.refs.check_me.checked
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


    state = {
        startDate: new Date(),
        endDate:new Date()
    };

    handleChange = date => {
        this.setState({
            startDate: date
        });
    };

    handleChangeEnd = date => {
        this.setState({
            endDate: date
        });
    };

    render() {
        const {subject, issuer,aim,aimroot,startDate, endDate, name, surname, email,type,extension} = this.state;
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
                                <Form.Group as={Col} controlId="formGridType">
                                    <Form.Label>Type</Form.Label>
                                    <Form.Control required as="select"
                                                  custom onChange={this.certificateChange}
                                                  name="type" value={type}
                                                  className={"bg-dark text-white"}>
                                        {this.state.types.map(type =>
                                            <option key={type.value} value={type.value}>
                                                {type.display}
                                            </option>
                                        )}
                                    </Form.Control>
                                </Form.Group>
                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridSubject">
                                    <Form.Label>Subject</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="text" name="subject"
                                        value={subject} onChange={this.certificateChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter Certificate Subject" />
                                </Form.Group>
                                <Form.Group as={Col} controlId="formGridIssuer">
                                    <Form.Label>Issuer</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="text" name="issuer"
                                        value={issuer} onChange={this.certificateChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter Certificate Issuer" />
                                </Form.Group>
                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridAimRoot">
                                    <Form.Label>Aim-Root</Form.Label>
                                    <Form.Control required as="select"
                                                  custom onChange={this.certificateChange}
                                                  name="aimroot" value={aimroot}
                                                  className={"bg-dark text-white"}>
                                        {this.state.aims.map(aimroot =>
                                            <option key={aimroot.value} value={aimroot.value}>
                                                {aimroot.display}
                                            </option>
                                        )}
                                    </Form.Control>
                                </Form.Group>

                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridAim">
                                    <Form.Label>Aim</Form.Label>
                                    <Form.Control autoComplete="off"
                                                  type="text" name="aim"
                                                  value={aim} onChange={this.certificateChange}
                                                  className={"bg-dark text-white"}
                                                  placeholder="Enter Certificate Aim" />
                                </Form.Group>

                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridCoverStartDate">
                                    <Form.Label>Start date  &nbsp;</Form.Label>
                                    <DatePicker
                                        selected={this.state.startDate}
                                        onChange={this.handleChange}
                                        name="startDate"
                                        value={startDate}
                                    />


                                </Form.Group>
                                <Form.Group as={Col} controlId="formGridEndDate">
                                    <Form.Label>End date  &nbsp;</Form.Label>
                                    <DatePicker
                                        selected={this.state.endDate}
                                        onChange={this.handleChangeEnd}
                                        name="endDate"
                                        value={endDate}
                                    />
                                </Form.Group>
                            </Form.Row>
                            <Form.Row>
                                <Form.Group as={Col} controlId="formGridName">
                                    <Form.Label>Name</Form.Label>
                                    <Form.Control required autoComplete="off"
                                        type="text" name="name"
                                        value={name} onChange={this.certificateChange}
                                        className={"bg-dark text-white"}
                                        placeholder="Enter certificate name" />
                                </Form.Group>
                                <Form.Group as={Col} controlId="formGridSurname">
                                    <Form.Label>Surname</Form.Label>
                                    <Form.Control required autoComplete="off"
                                                  type="text" name="surname"
                                                  value={surname} onChange={this.certificateChange}
                                                  className={"bg-dark text-white"}
                                                  placeholder="Enter certificate surname" />
                                </Form.Group>
                            </Form.Row>
                                <Form.Row>
                                <Form.Group as={Col} controlId="formGridEmail">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control required autoComplete="off"
                                                  type="email" name="email"
                                                  value={email} onChange={this.certificateChange}
                                                  className={"bg-dark text-white"}
                                                  placeholder="Enter certificate name" />
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
                                <Form.Row>
                                    <Form>
                                        {['checkbox'].map((checked) => (
                                            <div key={`default-${checked}`} className="mb-3">
                                                {this.state.id ? <Form.Check
                                                    type={checked}
                                                    id={`default-${checked}`}
                                                    label={`whitdraw`}
                                                    ref="check_me"

                                                /> :  <Form.Check
                                                    disabled
                                                    type={checked}
                                                    label={`whitdraw`}
                                                    id={`disabled-default-${checked}`}
                                                />}



                                            </div>
                                        ))}
                                    </Form>
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