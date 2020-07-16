import React, {Component} from 'react';
import ReceiptInput from './ReceiptInput';
import UserChart from './UserChart';
import axios from 'axios';
//import "./App.css";


/** Main webpage for the website. */
class App extends Component {
  constructor() {
    super();
    
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
