import React from 'react';
import Button from 'react-bootstrap/Button';

/**
 * Component that shows video from the device's camera
 * and grabs a snapshot of the image to be sent to Blobstore API.
 */
class DeviceCamera extends React.Component {
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
   * show video as soons as the component is mounted.
   */
  componentDidMount() {
    this.showVideo();
    this.getFileUploadUrl();
  }
  /**
   * Change the video output, when the video feedback changes.
   */
  componentDidUpdate() {
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
          this.setState({uploadUrl: uploadUrl});
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
   */
  convertFromBlobToFile(blob, fileName){
    const lastModifiedInSeconds = new Date().getTime();
    const file = new File([blob], fileName, {lastModified: lastModifiedInSeconds});
    return file;
  }

  /**
   * Grabs a snapshot of the video being rendered.
   */
  capturePhoto = () => {
    const canvas = this.refs.canvas;
    let ctx = this.refs.canvas.getContext('2d');
    const player = this.refs.player;
    ctx.drawImage(player, 0, 0, canvas.width, canvas.height);
  }

  /**
   * Uploads the photo taken from the video.
   */
  uploadPhoto = () => {
    const canvas = this.refs.canvas;
    const player = this.refs.player;
    // Stop capturing the video.
    player.srcObject.getVideoTracks().forEach(track => track.stop());
    canvas.toBlob(
      blob => {
        const imageFile = this.convertFromBlobToFile(blob, 'receipt-file')
        const formData = new FormData();
        formData.append('receipt-file', imageFile);
        fetch(this.state.uploadUrl, {
          method: 'POST',
          body: formData,
          headers: {
            'Content-Type': 'multipart/form-data'
          },
        });
      },
      'image/jpeg',
      0.9,
    );
  }

  /**
   * Shows a video from the device's camera.
   */
  showVideo() {
    const constraints = {
      video: true,
      audio: false,
    };
    const player = this.refs.player;
    navigator.mediaDevices.getUserMedia(constraints)
        .then((stream) => {
          player.srcObject = stream;
          return player.play();
        })
        .catch(e => {
          console.error(e);
        });
  }

  /**
   * Renders react-bootstrap modal box component.
   *  @return { React.ReactNode } React virtual DOM.
   */
  render() {
    return (
    <>
      <video
        ref='player'
        controls
        autoplay>
      </video>
      <canvas
        ref='canvas'
        width={640}
        height={480}
      />
      <button
        onClick={this.capturePhoto}>
        Capture
      </button>
      <Button
        onClick={this.uploadPhoto}
        variant="primary"
        type="submit"
      >
        Use this picture
      </Button>
    </>
    )
  }
}

export default DeviceCamera;