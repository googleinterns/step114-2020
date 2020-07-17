import React, {Component} from 'react';
import ReceiptInput from './ReceiptInput';
import LineChart, {BarChart} from './UserChart';
import axios from 'axios';
//import "./App.css";


/** Main webpage for the website. */
class App extends Component {
  constructor() {
    super();
    this.state = { "chartType" : LineChart }; 
    this.updateChartType = this.updateChartType.bind(this);   
  }

  updateChartType(event) {
    switch(event.target.value) {
      case "LineChart": 
        this.setState({"chartType": LineChart});
        break;
      case "BarChart":
        this.setState({"chartType": BarChart});
        break;
    }
  }

  render() {
    const Chart = this.state.chartType;
    return (
      
      <div className="App">
        <div onChange={this.updateChartType}>
          <input defaultChecked type="radio" value="LineChart" name="gender" /> Line
          <input type="radio" value="BarChart" name="gender" /> Bar
        </div>
        <Chart />
        <ReceiptInput />
      </div>
    );
  }
}




export default App;
