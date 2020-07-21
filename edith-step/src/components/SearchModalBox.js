import React from 'react';

import Button from 'react-bootstrap/Button';
import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import Row from 'react-bootstrap/Row';

class SearchModalBox extends React.Component {
  
  constructor(props) {
    super(props);
  }
  
  render() {
    return (
      <Modal
        show={this.props.show}
        onHide={this.props.handleSearchModalBoxClose}
        centered
      >
        <Modal.Header closeButton>
          <Modal.Title id='contained-modal-title-vcenter'>
            Select your search criteria.
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form method='GET' action='search-entity'>
            <Row>
              <Form.Group as={Col} controlId='formGridState'>
                <Form.Label>Select Kind</Form.Label>
                <Form.Control name='kind' as='select' defaultValue='Choose...'>
                  <option>Receipt</option>
                  <option>Item</option>
                </Form.Control>
                <Form.Label>Entity Name</Form.Label>
                <Form.Control name = 'name' placeholder='Name' />
                <Form.Label>Date</Form.Label>
                <Form.Control name = 'date' placeholder='Date' />
                <Form.Label>Sort Order</Form.Label>
                <Form.Control name='sort-order' as='select' defaultValue='Choose...'>
                  <option>Ascending</option>
                  <option>Descending</option>
                </Form.Control>
                <Form.Label>Sort on Property</Form.Label>
                <Form.Control name='sort-on' as='select' defaultValue='Choose...'>
                  <option>Entity Name</option>
                  <option>Date</option>
                </Form.Control>
              </Form.Group>
            </Row>
            <Button variant='primary' type='submit' >
              Submit
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    );
  }
}

export default SearchModalBox;
