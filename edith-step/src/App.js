import React, {Component} from 'react';
import LineChart, {BarGraph, CategoryDoughnutChart,
  ItemDoughnutChart} from './UserChart';
import ReceiptHandler from './components/ReceiptHandler';
import TopNavbar from './components/TopNavbar';
import GroceryList from './components/GroceryList';
import './App.css';

/** Corresponds to the different chart types. */
const chart = {
  'LINE': 'LINE',
  'BAR': 'BAR',
  'DOUGHNUT': 'DOUGHNUT',
};

/** Main webpage for the website. */
class App extends Component {
  /** Constructor */
  constructor() {
    super();
    this.state = {'chartType': LineChart, 'dateFilter': '',
      'categoryFilter': '', 'showGroceryList': false};

    this.handleGroceryListShow = () => {
      this.setState({'showGroceryList': true});
    };

    /**
    * Updates the value of chartType in state
    * based on the value of {@code event}.
    *
    * @param { Event } event HTML element that has been selected
    */
    this.updateChartType = (event) => {
      switch (event.target.value) {
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
    };

    /**
    * Displays either a {@code CategoryDoughnutChart} or
    * {@code ItemDoughnutChart} based on the value of {@code doughnutType}
    * @param {String} doughnutType can be eiter 'category' or 'item'
    * @param {String} dateFilter if this value is defined, only items/categories
    *                      bought in the same week will be displayed
    * @param {String} categoryFilter if this value is defined, only items of
    *                          this category will be displayed in a
    *                          {@code ItemDoughnut} chart
    */
    this.showItemChart = (doughnutType, dateFilter, categoryFilter) => {
      if (doughnutType === 'category') {
        this.setState({'chartType': CategoryDoughnutChart,
          'dateFilter': dateFilter, 'categoryFilter': categoryFilter});
      } else if (doughnutType === 'item') {
        this.setState({'chartType': ItemDoughnutChart,
          'dateFilter': dateFilter, 'categoryFilter': categoryFilter});
      }
    };

    /** Sets the state back to its default values. */
    this.revertCharts = () => {
      this.setState({'chartType': LineChart,
        'dateFilter': '', 'categoryFilter': ''});
    };
  };

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
          <div className='scroll-down default-border'>
            Scroll Down
          </div>
          <div className='app-describe'>
            <span className='default-border'>
              Welcome To Edith: Expenditure Analyzer.
            </span>
          </div>
        </div>
        <div>
          <button onClick={this.revertCharts}>Revert Charts</button>
          <div id='chart-selector' onChange={this.updateChartType}>
            <input defaultChecked type='radio' value={chart.LINE}
              name='chart-selector' id='line' /> Line-chart
            <input type='radio' value={chart.BAR}
              name='chart-selector' id='bar' /> Bar-graph
            <input type='radio' value={chart.DOUGHNUT}
              name='chart-selector' id='doughnut' /> Doughnut-chart
          </div>
          <Chart action={this.showItemChart}
            dateFilter={this.state.dateFilter}
            categoryFilter={this.state.categoryFilter} />
        </div>
        <div className='app-body'>
          <div id='receipt-input'>
            <ReceiptHandler />
          </div>
          <button
            onClick={this.handleGroceryListShow}
            className='show-list'>Generate grocery list.
          </button>
          <>
            {this.state.showGroceryList==true &&
            <GroceryList />
            }
          </>
        </div>
      </div>
    );
  }
}

export default App;
