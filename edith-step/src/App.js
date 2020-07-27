import React, {Component} from 'react';
import ReceiptInput from './ReceiptInput';
import LineChart, {BarGraph, DoughnutChart} from './UserChart';
import axios from 'axios';
//import "./App.css";


/** Main webpage for the website. */
class App extends Component {
  constructor() {
    super();
    this.state = { "chartType" : LineChart, "dateSelection": ""}; 
    this.updateChartType = this.updateChartType.bind(this); 
    this.showWeeklyChart = this.showWeeklyChart.bind(this);  
    this.revertChart = this.revertChart.bind(this);
  }

  updateChartType(event) {
    switch(event.target.value) {
      case "line":
        this.setState({"chartType": LineChart});
        break;
      case "bar":
        this.setState({"chartType": BarGraph});
        break;
      case "doughnut":
        this.setState({"chartType": DoughnutChart});
        break;
    }
  }

  showWeeklyChart(dateSelection) {
    this.setState({"chartType":  DoughnutChart, "dateSelection": dateSelection});
  }
  
  revertChart() {
    this.setState({"chartType":  DoughnutChart, "dateSelection": ""});
  }

jki9
  render() {
    const Chart = this.state.chartType;
    return (
      <div className="App">
        <div id="radio-selector" onChange={this.updateChartType}>
          <input defaultChecked type="radio" value="line" name="chart-selector" id="line" /> Line
          <input type="radio" value="bar" name="chart-selector" id="bar" /> Bar
          <input type="radio" value="doughnut" name="chart-selector" id="doughnut" /> Doughnut
        </div>
        <Chart action={this.showWeeklyChart} revertAction={this.revertChart} dateSelection={this.state.dateSelection}/>
        <ReceiptInput />
      </div>
    );
  }
}

export default App;
