import React, {Component} from 'react';
import ReceiptInput from './ReceiptInput';
// import "./App.css";


/**
 * Main webpage for the website.
 */
class App extends Component {
  /**
   * Renders the main webpage
   */
  render() {
    return (
      <div className='App'>
        <ReceiptInput />
      </div>
    );
  }
}

export default App;
