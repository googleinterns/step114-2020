import React from 'react';

import Dropdown from 'react-bootstrap/Dropdown';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';

class TopNavbar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      user: null,
    };
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

  render() {
    return (
      <Navbar bg='dark' variant='dark' fixed='top' expand="lg">
        <Navbar.Brand href='/'>EDITH</Navbar.Brand>
          <Nav className='ml-auto links'>
            <Nav.Link href='#home' className="home">HOME</Nav.Link>
            <Nav.Link href='#features' className="features">FEATURES</Nav.Link>
            {this.state.user === null &&
              <Nav.Link href='/login' className="login-button">LOG IN</Nav.Link>
            }
            {this.state.user &&
              <>
                <Nav.Link href='#dashboard'>DASHBOARD</Nav.Link>
                <Dropdown>
                  <Dropdown.Toggle variant="dark" id="dropdown-basic">
                    {this.state.user.email}
                  </Dropdown.Toggle>
                  <Dropdown.Menu>
                    <Dropdown.Item>Upload Receipt</Dropdown.Item>
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
