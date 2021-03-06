import React from 'react';
import axios from 'axios';

import FileUploadModalBox from './FileUploadModalBox';
import UserInfoModalBox from './UserInfoModalBox';
import DeviceCameraModalBox from './DeviceCameraModalBox';
import SearchModalBox from './SearchModalBox';
import Dropdown from 'react-bootstrap/Dropdown';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';

/**
 * Top navigation bar for user interaction with log-in, log-out
 * and other app features.
 */
class TopNavbar extends React.Component {
  /**
   * @constructor
   * @param {Object}  props for React component.
   */
  constructor(props) {
    super(props);
    this.state = {
      user: null,
      uploadModalBoxShow: false,
      userInfoModalBoxShow: false,
      takePictureModalBox: false,
      searchBoxShow: false,
    };

    /**
     * Callback function to close the file upload modal box.
     */
    this.handleUploadModalClose = () => {
      this.setState({uploadModalBoxShow: false});
    };

    /**
     * Callback function to close the user upload modal box.
     */
    this.handleUserInfoModalBoxClose = () => {
      this.setState({userInfoModalBoxShow: false});
    };

    /**
     * Callback function to close the picture upload modal box.
     */
    this.handleTakePictureModalBoxClose = () => {
      this.setState({takePictureModalBox: false});
    };

    /**
     * Callback function to close the search upload modal box.
     */
    this.handleSearchModalBoxClose = () => {
      this.setState({searchBoxShow: false});
    };
  }

  /**
   * After the component did mount, call login servlet to get the user status.
   */
  componentDidMount() {
    this.login();
  }

  /**
   * Returns display name as username if set other wise email.
   * @return {String}  displayName name to display for the user
   */
  displayName() {
    const displayName = this.state.user.userName || this.state.user.email;
    return displayName;
  }

  /**
   * Checks the user log-in status and sets
   * user email in sessionstorage.
   */
  async login() {
    await fetch('/login')
        .then((response) => response.json())
        .then((userInfo) => {
          this.setState({user: userInfo});
        })
        .catch(() => {
          this.setState({user: null});
        });
    let queryResponse = '';
    let render = true;
    await fetch('/grocery-list-query')
        .then((response) => response.json())
        .then((json) => {
          queryResponse = json;
          if (json.isEmpty()) {
            render = false;
          }
        })
        .catch(() => {
          render = false;
        });
    if (render) {
      await axios({
        method: 'post',
        url: '/notifications',
        data: {
          body: queryResponse,
        },
      });
    }
  }

  /**
   * Renders navigation bar at the top of the webpage.
   *  @return { React.ReactNode } React virtual DOM.
   */
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
              <Nav.Link
                href='#dashboard'
                className='dashboard'>
                DASHBOARD
              </Nav.Link>
              <Nav.Link
                href='#search'
                onClick={() => this.setState({searchBoxShow: true})}
                className='search'>
                SEARCH
              </Nav.Link>
              <SearchModalBox
                show={this.state.searchBoxShow}
                handleSearchModalBoxClose={this.handleSearchModalBoxClose}
              />
              <Dropdown className='dropdowns'>
                <Dropdown.Toggle
                  variant='dark'
                  id='dropdown-basic'
                  className='dropdown-toggle'>
                  {this.displayName().toUpperCase()}
                </Dropdown.Toggle>
                <Dropdown.Menu
                  className='dropdown-menu'>
                  <Dropdown.Item
                    onClick={() => this.setState({uploadModalBoxShow: true})}
                    className='upload-receipt'>
                    Upload Receipt
                  </Dropdown.Item>
                  <FileUploadModalBox
                    show={this.state.uploadModalBoxShow}
                    handleUploadModalClose={this.handleUploadModalClose}
                  />
                  <Dropdown.Item
                    onClick={() => this.setState({takePictureModalBox: true})}
                    className='take-receipt-picture'>
                    Take Receipt Picture
                  </Dropdown.Item>
                  <DeviceCameraModalBox
                    show={this.state.takePictureModalBox}
                    handleTakePictureModalBoxClose=
                      {this.handleTakePictureModalBoxClose}
                  />
                  <Dropdown.Item
                    onClick={() => this.setState({userInfoModalBoxShow: true})}
                    className='update-info'>
                    Update Information
                  </Dropdown.Item>
                  <UserInfoModalBox
                    show={this.state.userInfoModalBoxShow}
                    handleUserInfoModalBoxClose=
                      {this.handleUserInfoModalBoxClose}
                  />
                  <Dropdown.Item
                    className='invite-friends'>
                    Invite Friends
                  </Dropdown.Item>
                  <Dropdown.Item
                    href={this.state.user.logOutUrl}
                    className='log-out'>
                    Log Out
                  </Dropdown.Item>
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
