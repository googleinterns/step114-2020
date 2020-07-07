import React from 'react';

import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';

class TopNavbar extends React.Component {

  render() {
    return (
      <Navbar bg='dark' variant='dark' fixed='top'>
        <Navbar.Brand href='/'>Edith</Navbar.Brand>
        <Nav className='ml-auto links'>
          <Nav.Link href='#home' className="home">Home</Nav.Link>
          <Nav.Link href='#features' className="features">Features</Nav.Link>
          <Nav.Link href='/' className="login-button">Log In</Nav.Link>
        </Nav>
      </Navbar>
    );
  }
}

export default TopNavbar;
