import React, { Component } from 'react';
import ReceiptInput from './ReceiptInput';
import UserChart from './UserChart';
import axios from 'axios';
//import "./App.css";


class App extends Component {
  constructor() {
    super();
    
  }
  componentDidMount() {
    console.log("Inside componentDidMount!");
    fetch("/api/v1/data-servlet")
      .then((response) => response.text())
      .then((text) => {
        console.log("here is the text from servlet: ", text);
      });
  }

  render() {
   
   return (
      <div className="App">
        <UserChart />
        <ReceiptInput />
      </div>
    );
  }
}

export default App;
