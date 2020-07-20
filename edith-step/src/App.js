import React, { Component } from 'react';
import TopNavbar from './components/TopNavbar';
import ReceiptHandler from './ReceiptHandler.js';

import './App.css';

class App extends Component {

  render() {
 return (
      <div className='App'>
        <div className='App-header'>
          <TopNavbar />
        </div>
        <div className='background-image-0'>
          <div className="scroll-down border">
            Scroll Down
          </div>
          <div className='app-describe'>
            <span className='border'>Welcome To Edith: The Best Expenditure Analyzer</span>
          </div>
        </div>
        <div className="receipt-info">
          <ReceiptHandler />
        </div>
      </div>
    );
  }
}

export default App;
