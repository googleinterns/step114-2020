import React from 'react';

import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Row from 'react-bootstrap/Row';
import PropTypes from 'prop-types';

/**
 * A react-bootstrap modal component with first name, last name, user name
 * and favorite store field.
 */
class UserInfoModalBox extends React.Component {
  /**
  * @constructor
  * @param {Object}  props for React component.
  */
  constructor(props) {
    super(props);
  }

  /**
   * Renders a modal box.
   *  @return { React.ReactNode } React virtual DOM.
   */
  render() {
    return (
      <Modal
        show={this.props.show}
        onHide={this.props.handleUserInfoModalBoxClose}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title id='contained-modal-title-vcenter'>
            Please Complete The Form
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form method='POST' action='/login'>
            <Row>
              <Form.Group as={Col}>
                <Form.Label>First Name</Form.Label>
                <Form.Control
                  placeholder='First Name'
                  className='first-name'
                  name='first-name'
                  required
                />
              </Form.Group>
              <Form.Group as={Col}>
                <Form.Label>Last Name</Form.Label>
                <Form.Control
                  placeholder='Last Name'
                  className='last-name'
                  name='last-name'
                  required
                />
              </Form.Group>
            </Row>
            <Row>
              <Form.Group as={Col}>
                <Form.Label>Username</Form.Label>
                <Form.Control
                  placeholder='Username'
                  className='username'
                  name='username'
                  required
                />
              </Form.Group>
              <Form.Group as={Col}>
                <Form.Label>Favorite Store</Form.Label>
                <Form.Control
                  placeholder='Favorite Store'
                  className='favorite-store'
                  name='favorite-store'
                  required
                />
              </Form.Group>
            </Row>
            <Button
              variant='primary'
              type='submit'>
              Submit
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    );
  }
}

UserInfoModalBox.propTypes = {
  show: PropTypes.bool,
  handleUserInfoModalBoxClose: PropTypes.func,
};

export default UserInfoModalBox;
