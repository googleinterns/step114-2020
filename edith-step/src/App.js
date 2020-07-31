import React, {Component} from 'react';
import ReceiptInput from './ReceiptInput';
import LineChart, {BarGraph, CategoryDoughnutChart, ItemDoughnutChart} from './UserChart';

/**
 * Corresponds to the different chart types.
 */
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
  /**
   * Updates the value of chartType in state 
   * based on the value of {@code event}.
   * @param event - HTML element that has been selected
   */
  updateChartType(event) {
    switch(event.target.value) {
      case chart.LINE:
        this.setState({'chartType': LineChart});
        break;
      case chart.BAR:
        this.setState({'chartType': BarGraph});
        break;
      case chart.DOUGHNUT:
        this.setState({'chartType': CategoryDoughnutChart});
        break;
    }
  }
  /**
   * Displays either a {@code CategoryDoughnutChart} or
   * {@code ItemDoughnutChart} based on the value of {@code doughnutType}
   * @param doughnutType can be eiter 'category' or 'item'
   * @param dateSelection if this value is not empty, only items/categories 
   *                      bought in the same week will be displayed
   * @param categroySelection if this value is not empty, only items of this
   *                          category will be displayed in a 
   *                          {@code ItemDoughnut} chart
   */
  showItemChart(doughnutType, dateSelection, categorySelection) {
    if (doughnutType === 'category') { 
      this.setState({'chartType':  CategoryDoughnutChart, 
                     'dateSelection': dateSelection, 'categorySelection': categorySelection});
    } else if (doughnutType === 'item') {
      this.setState({'chartType':  ItemDoughnutChart, 
                     'dateSelection': dateSelection, 'categorySelection': categorySelection});
    }
  }
  
  render() {
    const Chart = this.state.chartType;
    return (
      <div className='App'>
        <div id='chart-selector' onChange={this.updateChartType}>
          <input defaultChecked type='radio' value={chart.LINE} name='chart-selector' id='line' /> Line
          <input type='radio' value={chart.BAR} name='chart-selector' id='bar' /> Bar
          <input type='radio' value={chart.DOUGHNUT} name='chart-selector' id='doughnut' /> Doughnut
        </div>
        <Chart action={this.showItemChart} dateSelection={this.state.dateSelection}
               categorySelection={this.state.categorySelection} />
        <ReceiptInput />
      </div>
    );
  }
}

export default App;
