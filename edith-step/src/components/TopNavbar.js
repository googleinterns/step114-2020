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
      userInfoModalBoxShow: false,
      groceryListShow: false,
    };
    this.handleUploadModalClose = this.handleUploadModalClose.bind(this);
    this.handleUserInfoModalBoxClose = this.handleUserInfoModalBoxClose.bind(this);
    this.handleGroceryListShow = this.handleGroceryListShow.bind(this);
  }
  
  handleUploadModalClose() {
    this.setState({uploadModalBoxShow: false });
  }

  handleUserInfoModalBoxClose() {
    this.setState({userInfoModalBoxShow: false });
  }

  handleGroceryListShow() {
    this.setState({groceryListShow: false});
  }

  componentDidMount() {
    this.login();
  }
  
  displayName() {
    const displayName = this.state.user.userName || this.state.user.email;
    return displayName;
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

    const params = new URLSearchParams();
    params.append('type', 'expirationQuery');
    fetch('/grocery-list-query')
      .then(response => response.json())
      .then((expirationInfo) => {
          params.append('body', expirationInfo);
    });
    fetch('/notifications', {
      body: params,
    };
  }

  render() {
    return (
      <Navbar bg='dark' variant='dark' fixed='top' expand='lg'>
        <Navbar.Brand href='/'>EDITH</Navbar.Brand>
          <Nav className='ml-auto links'>
            <Nav.Link href='#home' className='home'>HOME</Nav.Link>
            <Nav.Link href='#features' className='features'>FEATURES</Nav.Link>
            {this.state.user === null &&
              <Nav.Link href='/login' className='login-button'>LOG IN</Nav.Link>
            }
            {this.state.user &&
              <>
                <Nav.Link href='#dashboard' className='dashboard'>DASHBOARD</Nav.Link>
                <Dropdown className='dropdowns'>
                  <Dropdown.Toggle variant='dark' id='dropdown-basic' className='dropdown-toggle'>
                    {this.displayName().toUpperCase()}
                  </Dropdown.Toggle>
                  <Dropdown.Menu className='dropdown-menu'>
                    <Dropdown.Item onClick={() => this.setState({uploadModalBoxShow: true})} className='upload-receipt'>Upload Receipt</Dropdown.Item>
                    <FileUploadModalBox
                      show={this.state.uploadModalBoxShow}
                      handleUploadModalClose={this.handleUploadModalClose}
                    />
                    <Dropdown.Item onClick={() => this.setState({userInfoModalBoxShow: true})} className='update-info'>Update Information</Dropdown.Item>
                    <UserInfoModalBox
                      show={this.state.userInfoModalBoxShow}
                      handleUserInfoModalBoxClose={this.handleUserInfoModalBoxClose}
                    />
                    <Dropdown.Item className='invite-friends'>Invite Friends</Dropdown.Item>
                    <Dropdown.Item href={this.state.user.logOutUrl} className='log-out'>Log Out</Dropdown.Item>
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
