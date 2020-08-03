import React, { Component } from 'react';

import ReceiptHandler from './components/ReceiptHandler';
import TopNavbar from './components/TopNavbar';
import GroceryList from './components/GroceryList';
import axios from 'axios';

import './App.css';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {showGroceryList: false};
    this.handleGroceryListShow = this.handleGroceryListShow.bind(this);
  }

  handleGroceryListShow() {
    console.log('show list');
    this.setState({showGroceryList: true});
  }

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
        <div className='app-body'>
          <div id='receipt-input'>
            <ReceiptHandler />
          </div>
          <button onClick={this.handleGroceryListShow} className='show-list'>Generate grocery list.</button>
          <>
          {this.state.showGroceryList==true &&
            <GroceryList />
          }
          </>
        </div>
      </div>
    );
  }
}

export default App;
