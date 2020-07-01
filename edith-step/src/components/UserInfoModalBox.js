import React from 'react';

import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';

class UserInfoModalBox extends React.Component {
  
  constructor(props) {
    super(props);
  }
  
  render() {
    return (
      <Modal
        show={this.props.show}
        onHide={this.props.handleModalClose}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title id="contained-modal-title-vcenter">
            Please Complete The Form
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form method="POST" action="/login">
            <Form.Row>
              <Col>
                <Form.Control placeholder="First Name" name="first-name" />
              </Col>
              <Col>
                <Form.Control placeholder="Last Name" name="last-name" />
              </Col>
            </Form.Row>
            <Form.Row>
              <Col>
                <Form.Control placeholder="Username" name="username" />
              </Col>
              <Col>
                <Form.Control placeholder="Favorite Store" name="favorite-store" />
              </Col>
            </Form.Row>
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
