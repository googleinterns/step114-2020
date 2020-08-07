import React from 'react';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import PropTypes from 'prop-types';

/**
 * Component that shows video from the device's camera
 * and grabs a snapshot of the image to be sent to Blobstore API.
 */
class DeviceCameraModalBox extends React.Component {
  /**
   * @constructor
   * @param {Object}  props for React component.
   */
  constructor(props) {
    super(props);
    this.canvasRef;
    this.playerRef;
    this.state = {
      uploadUrl: '',
    };

    /**
     * Uploads the photo taken from the video.
     */
    this.uploadPhoto = () => {
      const canvas = this.canvasRef;
      const player = this.playerRef;
      // Stop capturing the video.
      player.srcObject.getVideoTracks().forEach((track) => track.stop());
      canvas.toBlob(
          (blob) => {
            const imageFile = this.convertFromBlobToFile(blob, 'receipt-file');
            const formData = new FormData();
            formData.append('receipt-file', imageFile);
            fetch(this.state.uploadUrl, {
              method: 'POST',
              body: formData,
            });
          },
          'image/jpeg',
          1,
      );
      this.props.handleTakePictureModalBoxClose();
    };

    /**
     * Grabs a snapshot of the video being rendered.
     */
    this.capturePhoto = () => {
      const canvas = this.canvasRef;
      const ctx = this.canvasRef.getContext('2d');
      const player = this.playerRef;
      ctx.drawImage(player, 0, 0, canvas.width, canvas.height);
    };
  }

  /**
   * Show video as soons as the component is mounted.
   */
  componentDidMount() {
    this._isMounted = true;
    this.showVideo();
    this.getFileUploadUrl();
  }

  /**
   * Change the video output, when the video feedback changes.
   */
  componentDidUpdate() {
    this._isMounted = false;
    this.showVideo();
  }

  /**
   * Calls servlet and retrieves where the file should be uploaded
   * to be handled by the Blobstore API.
   */
  getFileUploadUrl() {
    fetch('/blobstore-upload-url')
        .then((response) => response.text())
        .then((uploadUrl) => {
          if (this._isMounted) {
            this.setState({uploadUrl: uploadUrl});
          }
        })
        .catch((error) => {
          console.error(error);
        });
  }

  /**
   * Creates File from Blob.
   *
   * @param {Blob}  blob blob obtained from canvas.
   * @param {String}  fileName the name of the file.
   * @return {File}  file obtained from blob.
   */
  convertFromBlobToFile(blob, fileName) {
    const lastModifiedInSeconds = new Date().getTime();
    const file = new File([blob], fileName,
        {lastModified: lastModifiedInSeconds});
    return file;
  }

  /**
   * Shows a video from the device's camera.
   */
  showVideo() {
    const constraints = {
      video: true,
      audio: false,
    };
    const player = this.playerRef;
    navigator.mediaDevices.getUserMedia(constraints)
        .then((stream) => {
          player.srcObject = stream;
          return player.play();
        })
        .catch((e) => {
          console.error(e);
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
        onHide={this.props.handleTakePictureModalBoxClose}
        centered
      >
        <Modal.Header
          closeButton
          className='modal-header'>
          <Modal.Title
            id="contained-modal-title-vcenter">
            Take a picture of the receipt.
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <video
            className='videoPlayer'
            ref={(el) => this.playerRef = el}
            controls
            autoPlay>
          </video>
          <canvas
            className='imageCanvas'
            ref={(el) => this.canvasRef = el}
          />
          <Button
            onClick={this.capturePhoto}
            variant="primary"
          >
            Capture
          </Button>
          <Button
            onClick={this.uploadPhoto}
            variant="primary"
            type="submit"
          >
            Use this picture
          </Button>
        </Modal.Body>
      </Modal>
    );
  }
}

DeviceCameraModalBox.propTypes = {
  show: PropTypes.bool,
  handleTakePictureModalBoxClose: PropTypes.func,
};

export default DeviceCameraModalBox;
