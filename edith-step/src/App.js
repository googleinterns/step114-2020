import React, { Component } from 'react';
import Button from 'react-bootstrap/Button';
import TopNavbar from './components/TopNavbar';
import ReceiptHandler from './ReceiptHandler.js';
import SearchResult from './components/SearchResult';
import './App.css';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      showSearchResults: false,
    }
  };

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
            <span className='border'>Welcome To Edith: The Best Expenditure Analyzer</span>
          </div>
        </div>
        <div className='receipt-info'>
          <ReceiptHandler />
        </div>
        <div className='search-results' id='search-results'>
          <Button
            type='submit'
            variant='primary'
            onClick={() => this.setState({showSearchResults: true})}
          >
            Show Search Results
          </Button>
          {this.state.showSearchResults && 
            <SearchResult />
          }
        </div>
      </div>
    );
  }
}

export default App;
