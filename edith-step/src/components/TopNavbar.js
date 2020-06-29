import React from 'react';

import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';

class TopNavbar extends React.Component {

  render() {
    return (
      <Navbar bg="dark" variant="dark">
        <Navbar.Brand href="/">Edith</Navbar.Brand>
        <Nav className="ml-auto">
          <Nav.Link href="#home">Home</Nav.Link>
          <Nav.Link href="#features">Features</Nav.Link>
          <Nav.Link href="/">Log In</Nav.Link>
        </Nav>
      </Navbar>
    );
  }
}

export default TopNavbar;
