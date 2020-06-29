import React, { Component } from 'react';
import TopNavbar from './components/TopNavbar';

import landingImage from './receipt.jpg';
import './App.css';


class App extends Component {

  render() {
 return (
      <div className='App'>
        <div className='App-header'>
          <TopNavbar />
        </div>
        <div className='background-image-0'>
          <div className='app-describe'>
            <span className='describe-text'>Welcome To The Best Expenditure Analyzer</span>
          </div>
        </div>
      </div>
    );
  }
}

export default App;
