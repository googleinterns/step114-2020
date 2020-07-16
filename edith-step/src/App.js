import React, {Component} from 'react';

import ReceiptInput from './components/ReceiptInput';
import TopNavbar from './components/TopNavbar';

import './App.css';

/**
 * Main webpage for the website.
 */
class App extends Component {

  render() {
    return (
      <div className='App'>
        <div className='App-header'>
          <TopNavbar />
        </div>
        <div className='background-image-0'>
          <div className='scroll-down border'>
            Scroll Down
          </div>
          <div className='app-describe'>
            <span className='border'>
              Welcome To Edith: The Best Expenditure Analyzer.
            </span>
          </div>
        </div>
        <ReceiptInput />
      </div>
    );
  }
}

export default App;
