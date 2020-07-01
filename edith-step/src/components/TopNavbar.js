import React from 'react';

import FileUploadModalBox from './FileUploadModalBox';
import UserInfoModalBox from './UserInfoModalBox';

import Dropdown from 'react-bootstrap/Dropdown';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

class TopNavbar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      user: null,
      uploadModalBoxShow: false,
      userInfoModalBoxShow: false
    };
    this.handleUploadModalClose = this.handleUploadModalClose.bind(this);
    this.handleUserInfoModalBoxClose = this.handleUserInfoModalBoxClose.bind(this);
  }
  
  handleUploadModalClose() {
    this.setState({uploadModalBoxShow: false });
  }

  handleUserInfoModalBoxClose() {
    this.setState({userInfoModalBoxShow: false });
  }

  componentDidMount() {
    this.login();
  }
  
  displayName() {
    const fullName = this.state.user.lastName + this.state.user.firstName
    if ((this.state.user.lastName + this.state.user.firstName).length > 0) {
      return fullName;
    } else {
      return this.state.user.email;
    }
    
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
                    {this.displayName()}
                  </Dropdown.Toggle>
                  <Dropdown.Menu>
                    <Dropdown.Item onClick={() => this.setState({uploadModalBoxShow: true})}>Upload Receipt</Dropdown.Item>
                    <FileUploadModalBox
                      show={this.state.uploadModalBoxShow}
                      handleUploadModalClose={this.handleUploadModalClose}
                    />
                    <Dropdown.Item onClick={() => this.setState({userInfoModalBoxShow: true})}>Update Information</Dropdown.Item>
                    <UserInfoModalBox
                      show={this.state.userInfoModalBoxShow}
                      handleUserInfoModalBoxClose={this.handleUserInfoModalBoxClose}
                    />
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
