import React, {Component} from 'react';

import ReceiptInput from './components/ReceiptInput';
import TopNavbar from './components/TopNavbar';

import './App.css';

/**
 * Main webpage for the website.
 */
class App extends Component {
  /**
   * Renders TopNavbar, ReceiptInput component.
   * @return { React.ReactNode } React virtual DOM.
   */
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
              Welcome To Edith: Expenditure Analyzer.
            </span>
          </div>
        </div>
        <ReceiptInput />
      </div>
    );
  }
}

export default App;
