import React, {Component} from 'react';
import ReceiptInput from './ReceiptInput';
import LineChart, {BarGraph, CategoryDoughnutChart, ItemDoughnutChart} from './UserChart';
import axios from 'axios';

 const chart = {
    'LINE': 'LINE',
    'BAR': 'BAR',
    'DOUGHNUT':  'DOUGHNUT' 
  }

/** Main webpage for the website. */
class App extends Component {
  constructor() {
    super();
    this.state = {'chartType': LineChart, 'dateSelection': '', 'categorySelection': ''}; 
    this.updateChartType = this.updateChartType.bind(this); 
    this.showItemChart = this.showItemChart.bind(this);  
  } 

  updateChartType(event) {
    switch(event.target.value) {
      case 'line':
        this.setState({'chartType': LineChart});
        break;
      case 'bar':
        this.setState({'chartType': BarGraph});
        break;
      case 'doughnut':
        this.setState({'chartType': CategoryDoughnutChart});
        break;
    }
  }

  showItemChart(chartType, dateSelection, categorySelection) {
    if (chartType === 'category') { 
      this.setState({'chartType':  CategoryDoughnutChart, 
                     'dateSelection': dateSelection, 'categorySelection': categorySelection});
    } else {
      this.setState({'chartType':  ItemDoughnutChart, 
                     'dateSelection': dateSelection, 'categorySelection': categorySelection});
    }
  }
  
  render() {
    const Chart = this.state.chartType;
    return (
      <div className='App'>
        <div id='chart-selector' onChange={this.updateChartType}>
          <input defaultChecked type='radio' value='line' name='chart-selector' id='line' /> Line
          <input type='radio' value='bar' name='chart-selector' id='bar' /> Bar
          <input type='radio' value='doughnut' name='chart-selector' id='doughnut' /> Doughnut
        </div>
        <Chart action={this.showItemChart} dateSelection={this.state.dateSelection}
               categorySelection={this.state.categorySelection} />
        <ReceiptInput />
      </div>
    );
  }
}

export default App;
