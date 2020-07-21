import React, {Component} from 'react';
import ReceiptInput from './ReceiptInput';
import LineChart, {BarGraph, DoughnutChart} from './UserChart';
import axios from 'axios';
//import "./App.css";


/** Main webpage for the website. */
class App extends Component {
  constructor() {
    super();
    this.state = { "chartType" : LineChart, "dateSelection": "" }; 
    this.updateChartType = this.updateChartType.bind(this); 
    this.showWeeklyChart = this.showWeeklyChart.bind(this);  
    this.revertChart = this.revertChart.bind(this);
  }

  updateChartType(event) {
    switch(event.target.value) {
      case "Line": 
        this.setState({"chartType": LineChart});
        break;
      case "Bar":
        this.setState({"chartType": BarGraph});
        break;
      case "Doughnut":
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

  render() {
    const Chart = this.state.chartType;
    return (
      <div className="App">
        <div onChange={this.updateChartType}>
          <input defaultChecked type="radio" value="Line" name="Line" /> Line
          <input type="radio" value="Bar" name="Bar" /> Bar
          <input type="radio" value="Doughnut" name="Doughnut" /> Doughnut
        </div>
        <Chart action={this.showWeeklyChart} revertAction={this.revertChart} dateSelection={this.state.dateSelection}/>
        <ReceiptInput />
      </div>
    );
  }
}

export default App;
