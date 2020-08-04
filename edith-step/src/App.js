import React, {Component} from 'react';
import LineChart, {BarGraph, CategoryDoughnutChart, ItemDoughnutChart} from './UserChart';
import ReceiptInput from './components/ReceiptInput';
import TopNavbar from './components/TopNavbar';
import './App.css';

/** Corresponds to the different chart types. */
 const chart = {
    'LINE': 'LINE',
    'BAR': 'BAR',
    'DOUGHNUT':  'DOUGHNUT' 
}


/** Main webpage for the website. */
class App extends Component {
  constructor() {
    super();
    this.state = {'chartType': LineChart, 'dateFilter': '', 'categoryFilter': ''}; 
    this.updateChartType = this.updateChartType.bind(this); 
    this.showItemChart = this.showItemChart.bind(this);  
  } 

  /**
   * Updates the value of chartType in state 
   * based on the value of {@code event}.
   *
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
   * @param dateFilter if this value is not empty, only items/categories 
   *                      bought in the same week will be displayed
   * @param categoryFilter if this value is not empty, only items of this
   *                          category will be displayed in a 
   *                          {@code ItemDoughnut} chart
   */
  showItemChart(doughnutType, dateFilter, categoryFilter) {
    if (doughnutType === 'category') { 
      this.setState({'chartType':  CategoryDoughnutChart, 
                     'dateFilter': dateFilter, 'categoryFilter': categoryFilter});
    } else if (doughnutType === 'item') {
      this.setState({'chartType':  ItemDoughnutChart, 
                     'dateFilter': dateFilter, 'categoryFilter': categoryFilter});
    }
  }

  /**
   * Renders TopNavbar, ReceiptInput component.
   * @return { React.ReactNode } React virtual DOM.
   */
  render() {
    const Chart = this.state.chartType;
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
        <div>
          <div id='chart-selector' onChange={this.updateChartType}>
            <input defaultChecked type='radio' value={chart.LINE} name='chart-selector' id='line' /> Line
            <input type='radio' value={chart.BAR} name='chart-selector' id='bar' /> Bar
            <input type='radio' value={chart.DOUGHNUT} name='chart-selector' id='doughnut' /> Doughnut
          </div>
          <Chart action={this.showItemChart} dateFilter={this.state.dateFilter}
               categoryFilter={this.state.categoryFilter} />
        </div>
        <ReceiptInput />
      </div>
    );
  }
}

export default App;
