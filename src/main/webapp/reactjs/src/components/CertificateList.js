import React, {Component} from 'react';

import './Style.css';
import {Card, Table, ButtonGroup, Button, InputGroup, FormControl} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faList,faDownload, faEdit, faStepBackward, faFastBackward, faStepForward, faFastForward, faSearch, faTimes} from '@fortawesome/free-solid-svg-icons';
import {Link} from 'react-router-dom';
import MyToast from './MyToast';
import axios from 'axios';
import moment from 'moment'

export default class CertificateList extends Component {



    constructor(props) {
        super(props);
        this.state = {
            certificates : [],
            search : '',
            currentPage : 1,
            certificatesPerPage : 5,
            sortToggle : true
        };
    }

    sortData = () => {
        this.setState(state => ({
            sortToggle : !state.sortToggle
        }));
        this.findAllCertificates(this.state.currentPage);
    }

    componentDidMount() {
        this.findAllCertificates(this.state.currentPage);
    }

    findAllCertificates(currentPage) {
        currentPage -= 1;
        let sortDir = this.state.sortToggle ? "asc" : "desc";
        axios.get("http://localhost:8081/rest/certificates?pageNumber="+currentPage+"&pageSize="+this.state.certificatesPerPage+"&sortBy=price&sortDir="+sortDir)
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    certificates: data.content,
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    currentPage: data.number + 1
                });
            });
    };

    deleteCertificate = (certificateId) => {
        axios.delete("http://localhost:8081/rest/certificates/"+certificateId)
            .then(response => {
                if(response.data != null) {
                    this.setState({"show":true});
                    setTimeout(() => this.setState({"show":false}), 3000);
                    this.setState({
                        certificates: this.state.certificates.filter(certificate => certificate.id !== certificateId)
                    });
                } else {
                    this.setState({"show":false});
                }
            });
    };

    withdrawCertificate = (certificateId) => {
        axios.withdraw("http://localhost:8081/rest/certificates/"+certificateId)
            .then(response => {
                if(response.data != null) {
                    this.setState({"show":true});
                    setTimeout(() => this.setState({"show":false}), 3000);
                    this.setState({
                        certificates: this.state.certificates.filter(certificate => certificate.id !== certificateId)
                    });
                } else {
                    this.setState({"show":false});
                }
            });
    };

    withdraw(s) {
        
    }


    changePage = event => {
        let targetPage = parseInt(event.target.value);
        if(this.state.search) {
            this.searchData(targetPage);
        } else {
            this.findAllCertificates(targetPage);
        }
        this.setState({
            [event.target.name]: targetPage
        });
    };

    firstPage = () => {
        let firstPage = 1;
        if(this.state.currentPage > firstPage) {
            if(this.state.search) {
                this.searchData(firstPage);
            } else {
                this.findAllCertificates(firstPage);
            }
        }
    };

    prevPage = () => {
        let prevPage = 1;
        if(this.state.currentPage > prevPage) {
            if(this.state.search) {
                this.searchData(this.state.currentPage - prevPage);
            } else {
                this.findAllCertificates(this.state.currentPage - prevPage);
            }
        }
    };

    lastPage = () => {
        let condition = Math.ceil(this.state.totalElements / this.state.certificatesPerPage);
        if(this.state.currentPage < condition) {
            if(this.state.search) {
                this.searchData(condition);
            } else {
                this.findAllCertificates(condition);
            }
        }
    };

    nextPage = () => {
        if(this.state.currentPage < Math.ceil(this.state.totalElements / this.state.certificatesPerPage)) {
            if(this.state.search) {
                this.searchData(this.state.currentPage + 1);
            } else {
                this.findAllCertificates(this.state.currentPage + 1);
            }
        }
    };

    searchChange = event => {
        this.setState({
            [event.target.name] : event.target.value
        });
    };

    cancelSearch = () => {
        this.setState({"search" : ''});
        this.findAllCertificates(this.state.currentPage);
    };


    findCertificateByIdDownload = (certificateId) => {
        console.log('Click happened');
        axios.get("http://localhost:8081/rest/certificates/download/"+certificateId)
            .then(response => {
                if(response.data != null)
                {
                    this.setState({
                        id: response.data.id,
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

                    });

                }
            }).catch((error) => {
                console.error("Error - "+error);
            });
        };

    searchData = (currentPage) => {
        currentPage -= 1;
        axios.get("http://localhost:8081/rest/certificates/search/"+this.state.search+"?page="+currentPage+"&size="+this.state.certificatesPerPage)
            .then(response => response.data)
            .then((data) => {
                this.setState({
                    certificates: data.content,
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    currentPage: data.number + 1
                });
            });
    };

    render() {
        const {certificates, currentPage, totalPages, search} = this.state;

        return (
            <div>
                <div style={{"display":this.state.show ? "block" : "none"}}>
                    <MyToast show = {this.state.show} message = {"Certificate Deleted Successfully."} type = {"danger"}/>
                </div>
                <Card className={"border border-dark bg-dark text-white"}>
                    <Card.Header>
                        <div style={{"float":"left"}}>
                            <FontAwesomeIcon icon={faList} /> Certificate List
                        </div>
                        <div style={{"float":"right"}}>
                             <InputGroup size="sm">
                                <FormControl placeholder="Search" name="search" value={search}
                                    className={"info-border bg-dark text-white"}
                                    onChange={this.searchChange}/>
                                <InputGroup.Append>
                                    <Button size="sm" variant="outline-info" type="button" onClick={this.searchData}>
                                        <FontAwesomeIcon icon={faSearch}/>
                                    </Button>
                                    <Button size="sm" variant="outline-danger" type="button" onClick={this.cancelSearch}>
                                        <FontAwesomeIcon icon={faTimes} />
                                    </Button>
                                </InputGroup.Append>
                             </InputGroup>
                        </div>
                    </Card.Header>
                    <Card.Body>
                        <Table bordered hover striped variant="dark" >
                            <thead>
                                <tr>
                                    <th>Subject</th>
                                    <th>Issuer</th>
                                    <th>Aim</th>
                                    <th>Start Date</th>
                                    <th >End Date </th>
                                    <th>Name</th>
                                    <th>Surname</th>
                                    <th>Email</th>
                                    <th>Type</th>
                                    <th>Extension</th>
                                    <th>Withdrawn</th>
                                </tr>
                              </thead>
                              <tbody >
                                {
                                    certificates.length === 0 ?
                                    <tr align="center">
                                      <td colSpan="7">No certificates Available.</td>
                                    </tr> :
                                        certificates.map((certificate) => (
                                    <tr key={certificate.id}>
                                        <td>{certificate.subject}</td>
                                        <td>{certificate.issuer}</td>
                                        <td>{certificate.aim}</td>
                                        <td>{moment(certificate.startDate).format('DD/MM/YYYY')}</td>
                                        <td>{moment(certificate.endDate).format('DD/MM/YYYY')}</td>
                                        <td>{certificate.name}</td>
                                        <td>{certificate.surname}</td>
                                        <td>{certificate.email}</td>
                                        <td>{certificate.type}</td>
                                        <td>{certificate.extension}</td>
                                        <td>{ String(certificate.withdrawn) }</td>
                                        <td>
                                            <ButtonGroup>
                                                <Link to={"edit/"+certificate.id} className="btn btn-sm btn-outline-primary"><FontAwesomeIcon icon={faEdit} /></Link>{' '}
                                                <Link  onClick={this.findCertificateByIdDownload} className="btn btn-sm btn-outline-primary"><FontAwesomeIcon icon={faDownload} /></Link>{' '}

                                            </ButtonGroup>
                                        </td>
                                    </tr>
                                    ))
                                }
                              </tbody>
                        </Table>
                    </Card.Body>
                    {certificates.length > 0 ?
                        <Card.Footer>
                            <div style={{"float":"left"}}>
                                Showing Page {currentPage} of {totalPages}
                            </div>
                            <div style={{"float":"right"}}>
                                <InputGroup size="sm">
                                    <InputGroup.Prepend>
                                        <Button type="button" variant="outline-info" disabled={currentPage === 1 ? true : false}
                                            onClick={this.firstPage}>
                                            <FontAwesomeIcon icon={faFastBackward} /> First
                                        </Button>
                                        <Button type="button" variant="outline-info" disabled={currentPage === 1 ? true : false}
                                            onClick={this.prevPage}>
                                            <FontAwesomeIcon icon={faStepBackward} /> Prev
                                        </Button>
                                    </InputGroup.Prepend>
                                    <FormControl className={"page-num bg-dark"} name="currentPage" value={currentPage}
                                        onChange={this.changePage}/>
                                    <InputGroup.Append>
                                        <Button type="button" variant="outline-info" disabled={currentPage === totalPages ? true : false}
                                            onClick={this.nextPage}>
                                            <FontAwesomeIcon icon={faStepForward} /> Next
                                        </Button>
                                        <Button type="button" variant="outline-info" disabled={currentPage === totalPages ? true : false}
                                            onClick={this.lastPage}>
                                            <FontAwesomeIcon icon={faFastForward} /> Last
                                        </Button>
                                    </InputGroup.Append>
                                </InputGroup>
                            </div>
                        </Card.Footer> : null
                     }
                </Card>
            </div>
        );
    }
}