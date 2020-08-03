import React from 'react';

class DeviceCamera extends React.Component {
  
  componentDidMount() {
    this.updateCanvas();
  }
  
  componentDidUpdate() {
    this.updateCanvas();
  }

  capturePhoto() {
    const canvas = this.refs.canvas;
    const ctx = this.refs.canvas.getContext('2d');
    const player = this.refs.player;
    ctx.drawImage(player, 0, 0, canvas.width, canvas.height);
    player.srcObject.getVideoTracks().forEach(track => track.stop());
  }

  updateCanvas() {
    const constraints = {
      video: true,
    };
    const player = this.refs.player;
    navigator.mediaDevices.getUserMedia(constraints)
        .then((stream) => {
          player.srcObject = stream;
        });
  }

  render() {
    return (
    <>
      <canvas
        ref='canvas'
        width={300}
        height={300}
      />
      <video
        ref='player'
        controls
        autoplay>
      </video>
      <button
        onClick={() => this.capturePhoto}>
        Capture
      </button>
    </>
    )
  }
}

export default DeviceCamera;
