import React from 'react';

class DeviceCamera extends React.Component {
  canvas;
  player;
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
    console.log('i am here');
    this.canvas = this.refs.canvas;
    let ctx = this.refs.canvas.getContext('2d');
    this.player = this.refs.player;
    ctx.drawImage(this.player, 0, 0, this.canvas.width, this.canvas.height);
    const imageUrl = this.canvas.toDataURL('image/png');
    console.log(imageUrl);
    // canvas.toBlob() = (blob) => {
    //   const img = new Image();
    //   img.src = window.URL.createObjectUrl(blob);
    // };
    this.player.srcObject.getVideoTracks().forEach(track => track.stop());
  }

  showVideo() {
    const constraints = {
      video: true,
      audio: false,
    };
    this.player = this.refs.player;
    navigator.mediaDevices.getUserMedia(constraints)
        .then((stream) => {
          this.player.srcObject = stream;
          this.player.play();
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
