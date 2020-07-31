import React from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Modal from 'react-bootstrap/Modal';
import PropTypes from 'prop-types';

/**
 * Modal Box where user can select a file to upload.
 */
class FileUploadModalBox extends React.Component {
  /**
   * @constructor
   * @param {Object}  props for React component.
   */
  constructor(props) {
    super(props);
    this.state = {
      uploadUrl: '',
    };
  }

  /**
   * After the component did mount, get URL to
   * to direct the file upload.
   */
  componentDidMount() {
    this.getFileUploadUrl();
  }

  /**
   * After the component unmounts, reset the state.
   */
  componentWillUnmount() {
    this.setState = () => {
      return;
    };
  }

  /**
   * Calls servlet and retrieves where the file should be uploaded
   * to be handled by the Blobstore API.
   */
  getFileUploadUrl() {
    fetch('/blobstore-upload-url')
        .then((response) => response.text())
        .then((uploadUrl) => {
          this.setState({uploadUrl: uploadUrl});
        })
        .catch((error) => {
          console.error(error);
        });
  }

  /**
   * Renders react-bootstrap modal box component.
   *  @return { React.ReactNode } React virtual DOM.
   */
  render() {
    return (
      <Modal
        show={this.props.show}
        onHide={this.props.handleUploadModalClose}
        centered
      >
        <Modal.Header
          closeButton
          className='modal-header'>
          <Modal.Title
            id="contained-modal-title-vcenter">
            Please Upload Your Receipt File
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form
            method="POST"
            action={this.state.uploadUrl}
            encType="multipart/form-data"
          >
            <Form.Group>
              <Form.File
                required
                className="receipt-file"
                label="Receipt file input"
                name="receipt-file"
              />
            </Form.Group>
            <Button
              variant="primary"
              type="submit"
            >
              Submit
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    );
  }
}

FileUploadModalBox.propTypes = {
  show: PropTypes.bool,
  handleModalClose: PropTypes.func,
};

export default FileUploadModalBox;
