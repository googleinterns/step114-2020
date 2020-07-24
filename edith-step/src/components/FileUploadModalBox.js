import React from 'react';

import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal'

class FileUploadModalBox extends React.Component {
  
  constructor(props) {
    super(props);
    this.state = {
      uploadUrl: '',
    };
  }
  
  componentDidMount() {
    this.getFileUploadUrl();
  }
  
  componentWillUnmount() {
    this.setState = () =>{
      return;
    };
  }

  async getFileUploadUrl() {
    await fetch('/blobstore-upload-url')
      .then(response => response.text())
      .then(uploadUrl => {
        this.setState({uploadUrl: uploadUrl});
      })
      .catch((error) => {
        console.error(error);
    });
  }

  render() {
    return (
      <Modal
        show={this.props.show}
        onHide={this.props.handleUploadModalClose}
        centered
      >
        <Modal.Header closeButton className='modal-header'>
          <Modal.Title id="contained-modal-title-vcenter">
            Please Upload Your Receipt File With An Expense Name
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form method="POST" action={this.state.uploadUrl} encType="multipart/form-data">
            <Form.Group>
              <Form.Control type="text" name="expense-name" placeholder="Expense Name" />
              <Form.File required className="receipt-file" id="receipt-file" label="Receipt file input" name="receipt-file" />
            </Form.Group>
            <Button variant="primary" type="submit" >
              Submit
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    );
  }
}

export default FileUploadModalBox;
