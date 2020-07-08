import React, { Component } from 'react';
import ReceiptInput from './ReceiptInput';
//import "./App.css";


class App extends Component {
  componentDidMount() {
    console.log("Inside componentDidMount!");
    fetch("/api/v1/test-servlet")
      .then((response) => response.text())
      .then((text) => {
        console.log("here is the text from servlet: ", text);
      });
  }

  render() {
   return (
      <div className="App">
        <ReceiptInput />
      </div>
    );
  }
}

export default App;
