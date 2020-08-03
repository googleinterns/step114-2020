import React from 'react';
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';

/**
 * Component that shows video from the device's camera
 * and grabs a snapshot of the image to be sent to Blobstore API.
 */
class DeviceCamera extends React.Component {
  /**
   * @constructor
   * @param {Object}  props for React component.
   */
  constructor() {
    super();
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
    player.srcObject.getVideoTracks().forEach(track => track.stop());
    const imageUrl = canvas.toDataURL('image/gif');
    console.log(imageUrl);
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
          player.play();
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
