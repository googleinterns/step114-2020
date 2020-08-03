import React from 'react';

class DeviceCamera extends React.Component {
  constructor() {
    super();
  }

  componentDidMount() {
    this.showVideo();
  }
  
  componentDidUpdate() {
    this.showVideo();
  }

  capturePhoto = () => {
    const canvas = this.refs.canvas;
    let ctx = this.refs.canvas.getContext('2d');
    const player = this.refs.player;
    ctx.drawImage(player, 0, 0, canvas.width, canvas.height);
    const imageUrl = this.canvas.toDataURL('image/png');
  }

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
        width={300}
        height={300}
      />
      <button
        onClick={this.capturePhoto}>
        Capture
      </button>
    </>
    )
  }
}

export default DeviceCamera;
