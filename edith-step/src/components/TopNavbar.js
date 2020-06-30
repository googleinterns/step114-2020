import React from 'react';

import FileUploadModalBox from './FileUploadModalBox';

import Button from 'react-bootstrap/Button'
import Dropdown from 'react-bootstrap/Dropdown';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';

class TopNavbar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      user: null,
      modalShow: false,
    };
    this.handleModalClose = this.handleModalClose.bind(this);
  }
  
  handleModalClose() {
    this.setState({modalShow: false });
  }

  componentDidMount() {
    this.login();
  }
  
  login() {
    fetch('/login')
      .then(response => response.json())
      .then(userInfo => {
        sessionStorage.setItem('logged-in', userInfo.email);
        this.setState({user: userInfo});
      })
      .catch(() => {
        sessionStorage.setItem('logged-in', '');
        this.setState({user: null});
    });
  }

  getFileUploadUrl() {
    fetch('/blobstore-upload-url')
      .then(response => response.text())
      .then(userInfo => {
        this.setState({user: userInfo});
      })
      .catch((error) => {
        console.error(error);
    });
  }

  render() {
    return (
      <Navbar bg='dark' variant='dark' fixed='top' expand="lg">
        <Navbar.Brand href='/'>EDITH</Navbar.Brand>
          <Nav className='ml-auto'>
            <Nav.Link href='#home'>HOME</Nav.Link>
            <Nav.Link href='#features'>FEATURES</Nav.Link>
            {this.state.user === null &&
              <Nav.Link href='/login'>LOG IN</Nav.Link>
            }
            {this.state.user &&
              <>
                <Nav.Link href='#dashboard'>DASHBOARD</Nav.Link>
                <Dropdown>
                  <Dropdown.Toggle variant="dark" id="dropdown-basic">
                    {this.state.user.email}
                  </Dropdown.Toggle>
                  <Dropdown.Menu>
                    <Dropdown.Item onClick={() => this.setState({modalShow: true})}>Upload Receipt</Dropdown.Item>
                    <FileUploadModalBox
                      show={this.state.modalShow}
                      handleModalClose={this.handleModalClose}
                    />
                    <Dropdown.Item>Set Nickname</Dropdown.Item>
                    <Dropdown.Item>Invite Friends</Dropdown.Item>
                    <Dropdown.Item href={this.state.user.logOutUrl}>Log Out</Dropdown.Item>
                  </Dropdown.Menu>
                </Dropdown>
              </>
            }
          </Nav>
      </Navbar>
    );
  }
}

export default TopNavbar;
