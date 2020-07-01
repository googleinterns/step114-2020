import React from 'react';

import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Row from 'react-bootstrap/Row';
class UserInfoModalBox extends React.Component {
  
  constructor(props) {
    super(props);
  }
  
  render() {
    return (
      <Modal
        show={this.props.show}
        onHide={this.props.handleUserInfoModalBoxClose}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title id="contained-modal-title-vcenter">
            Please Complete The Form
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form method="POST" action="/login">
            <Row>
              <Form.Group as={Col}>
                <Form.Label>First Name</Form.Label>
                <Form.Control placeholder="First Name" name="first-name" required />
              </Form.Group>
              <Form.Group as={Col}>
                <Form.Label>Last Name</Form.Label>
                <Form.Control placeholder="Last Name" name="last-name" required />
              </Form.Group>
            </Row>
            <Row>
              <Form.Group as={Col}>
                <Form.Label>Username</Form.Label>
                <Form.Control placeholder="Username" name="username" required />
              </Form.Group>
              <Form.Group as={Col}>
                <Form.Label>Favorite Store</Form.Label>
                <Form.Control placeholder="Favorite Store" name="favorite-store" required />
              </Form.Group>
            </Row>
            <Button variant="primary" type="submit" >
              Submit
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    );
  }
}

export default UserInfoModalBox;
