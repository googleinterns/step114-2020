import React, { Component } from 'react';
import TopNavbar from './components/TopNavbar';
import ReceiptHandler from './ReceiptHandler.js';

import './App.css';
import ReceiptInput from './ReceiptInput';
// import "./App.css";


/** Main webpage for the website. */
class App extends Component {
  constructor(props) {
    super(props)
    this.state = { renderReceiptForm: true };
    this.handleChildUnmount = this.handleChildUnmount.bind(this);
  }

  handleChildUnmount() {
    this.setState({renderReceiptForm: false});
  }
  
  render() {
    return (
      <div className='App'>
        <div className='App-header'>
          <TopNavbar />
        </div>
        <div className='background-image-0'>
          <div class="scroll-down border">
            Scroll Down
          </div>
          <div className='app-describe'>
            <span className='border'>Welcome To Edith: The Best Expenditure Analyzer</span>
          </div>
        </div>
        {this.state.renderReceiptForm ? <ReceiptHandler unmountMe={this.handleChildUnmount} /> : null}
      </div>
    );
  }
}

export default App;
