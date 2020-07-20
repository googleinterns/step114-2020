import React from 'react';

import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';

/**
 * Top navigation bar for user interaction with log-in, log-out
 * and other app features.
 */
class TopNavbar extends React.Component {
  /**
   * Renders navigation bar at the top of the webpage.
   *  @return { React.ReactNode } React virtual DOM.
   */
  render() {
    return (
      <Navbar bg='dark' variant='dark' fixed='top'>
        <Navbar.Brand href='/'>Edith</Navbar.Brand>
        <Nav className='ml-auto links'>
          <Nav.Link href='#home' className='home'>Home</Nav.Link>
          <Nav.Link href='#features' className='features'>Features</Nav.Link>
          <Nav.Link href='/' className='login-button'>Log In</Nav.Link>
        </Nav>
      </Navbar>
    );
  }
}

export default TopNavbar;
