/**
 * This module contains three charts/graphs used to display user information
 * and the helper methods used within them.
 */
import React, {useState, useEffect} from 'react';
import {Line, Bar, Doughnut} from 'react-chartjs-2';
import PropTypes from 'prop-types';
import 'regenerator-runtime/runtime';


/**
 * Makes a get request to '/user-stats-servlet'to receieve week information
 * used in all three charts in this module.
 */
async function retrieveWeekData() {
  const response = await fetch('/user-stats-servlet');
  const responseJson = await response.json();
  if (responseJson.weeklyAggregate.length === 0) {
    return;
  }
  const weekDates = [];
  const values = [];
  const weeklyAggregate = JSON.parse(responseJson.weeklyAggregate);
  weeklyAggregate.forEach((week) => {
    weekDates.push(week.date);
    values.push(week.total);
  });
  return [weekDates, values];
}

/**
 * Determines if {@code itemDate} is in the same as {@code dateFilter}
 * (a week starts on Monday and ends on Sunday). Dates are
 * in yyyy-mm-dd format.
 *
 * @param {String} itemDate - the date the specific item was purchased on
 * @param {String} dateFilter - the date to compare itemDate to
 * @return {Boolean} value representing whether itemDate
 *                   is in the same week as dateFilter
 */
const inSameWeek = (itemDate, dateFilter) => {
  itemDate = new Date(itemDate);
  dateFilter = new Date(dateFilter);
  const diffTime = dateFilter - itemDate;
  // Then length of a day is 1000ms * 60 seconds * 60 minutues * 24 hours
  const diffDays = diffTime / (1000 * 60 * 60 * 24);
  return diffDays < 7 && diffDays >= 0;
};

/**
 * Populates the chart data using information pulled from retrieveWeekData()
 * and sets other phsyical attributes.
 *
 * @param {function} setChartData function called to update the state of
 *                                {@code chartData} in a functional component
 *                                that sets {@code chartData} using a
 *                                state hook.
 */
async function setChart(setChartData) {
  const fetchData = await retrieveWeekData();
  setChartData({
    labels: fetchData[0],
    datasets: [
      {
        label: 'Week Total',
        data: fetchData[1],
        backgroundColor: ['rgb(0, 191, 255, 0.6)',
          'rgb(100, 191, 255, 0.6)',
          'rgb(300, 191, 255, 0.6)',
          'rgb(0, 0, 255, 0.6)'],
        borderWidth: 4,
      },
    ],
  });
}

/**
 * LineChart that relates weeks with the trailing total calculated for that
 * week. Each data point can be clicked on to show a more in-depth spending
 * for that week.
 *
 * @param {Props} props - contains the methods used to update/revert the state
 *                        to display a drill-downed chart and a
 *                        dateFilter variable
 * @return {React.ReactNode} React object containing a canvas
 *                           with a LineChart drawn onto it.
 */
const LineChart = (props) => {
  LineChart.propTypes = {
    action: PropTypes.func,
    dateFilter: PropTypes.string,
    categoryFilter: PropTypes.string,
  };

  const [chartData, setChartData] = useState({});
  setChart(setChartData);

  useEffect(() => {
    setChart();
  }, []);

  /** returns the react node */
  return (
    <Line
      data={chartData}
      width={300}
      height={300}
      options={{
        onClick: (event, element) => {
          const dateFilter =
            element[0]._chart.config.data.labels[element[0]._index];
          props.action('category', dateFilter, '');
        },
        maintainAspectRatio: false,
        title: {
          display: true,
          text: 'Weekly Aggregate (trailing total based on week date)',
          fontSize: 20,
        },
        legend: {
          display: true,
          position: 'right',
        },
        scales: {
          yAxes: [{
            scaleLabel: {
              display: true,
              fontSize: 20,
              labelString: 'Dollars',
            },
            ticks: {
              beginAtZero: true,
            },
          }],
          xAxes: [{
            scaleLabel: {
              display: true,
              fontSize: 20,
              labelString: 'Week',
            },
          }],
        },
      }}
    />
  );
};

/**
 * BarGraph that relates weeks with the trailing total calculated for that
 * week. Each data point can be clicked on to show a more in-depth spending
 * for that week.
 * @param {Props} props  contains the method used to update the state
 *                        to display a drill-downed chart
 *                        and a dateFilter variable
 * @return {React.ReactNode} React object containing a canvas
 *                           with a LineChart drawn onto it.
 */
const BarGraph = (props) => {
  BarGraph.propTypes = {
    action: PropTypes.func,
    dateFilter: PropTypes.string,
    categoryFilter: PropTypes.string,
  };

  const [chartData, setChartData] = useState({});
  setChart(setChartData);

  useEffect(() => {
    setChart();
  }, []);

  return (
    <Bar
      data={chartData}
      width={300}
      height={300}
      options={{
        onClick: (event, element) => {
          const dateFilter =
            element[0]._chart.config.data.labels[element[0]._index];
          props.action('category', dateFilter, '');
        },
        maintainAspectRatio: false,
        title: {
          display: true,
          text: 'Weekly Aggregate (trailing total based on week date)',
          fontSize: 20,
        },
        legend: {
          display: true,
          position: 'right',
        },
        scales: {
          yAxes: [{
            scaleLabel: {
              display: true,
              fontSize: 20,
              labelString: 'Dollars',
            },
            ticks: {
              beginAtZero: true,
            },
          }],
          xAxes: [{
            scaleLabel: {
              display: true,
              fontSize: 20,
              labelString: 'Week',
            },
          }],
        },
      }}
    />
  );
};

/**
 * DoughnutChart that shows the total amount of spending for each item.
 * @param {Props} props  contains the methods used to update/revert the state
 *                        to display a drill-downed chart
 *                        and a dateFilter variable
 * @return {React.ReactNode} React object containing a canvas
 *                           with a LineChart drawn onto it.
 */
const CategoryDoughnutChart = (props) => {
  CategoryDoughnutChart.propTypes = {
    action: PropTypes.func,
    dateFilter: PropTypes.string,
    categoryFilter: PropTypes.string,
  };

  const [chartData, setChartData] = useState({});
  const chart = () => {
    fetch('/user-stats-servlet')
        .then((response) => response.json())
        .then((responseJson) => {
          const itemNames = [];
          const itemValues = [];
          const itemsJson = JSON.parse(responseJson.items);
          const items = {};
          if (props.dateFilter.length > 0) {
            itemsJson.forEach((item) => {
              if (inSameWeek(item.date, props.dateFilter)) {
                if (items[item.category]) {
                  items[item.category] = items[item.category] + 1;
                } else {
                  items[item.category] = 1;
                }
              }
            });
          } else {
            itemsJson.forEach((item) => {
              if (items[item.category]) {
                items[item.category] = items[item.category] + 1;
              } else {
                items[item.category] = 1;
              }
            });
          }
          Object.keys(items).forEach((item) => {
            itemNames.push(item);
            itemValues.push(items[item]);
          });
          setChartData({
            labels: itemNames,
            datasets: [
              {
                label: 'Week Total',
                data: itemValues,
                backgroundColor: ['rgba(75, 192, 192, 0.6)'],
                borderWidth: 4,
              },
            ],
          });
        })
        .catch((err) => {
          console.log(err);
        });
  };

  useEffect(() => {
    chart();
  }, []);

  /** returns the react node */
  return (
    <Doughnut
      data={chartData}
      width={300}
      height={300}
      options={{
        onClick: (event, element) => {
          const categoryFilter =
            element[0]._chart.config.data.labels[element[0]._index];
          props.action('item', props.dateFilter, categoryFilter);
        },
        maintainAspectRatio: false,
        title: {
          display: true,
          text: 'Item Aggregate',
          fontSize: 20,
        },
        legend: {
          display: true,
          position: 'bottom',
        },
      }}
    />
  );
};

const ItemDoughnutChart = (props) => {
  ItemDoughnutChart.propTypes = {
    action: PropTypes.func,
    dateFilter: PropTypes.string,
    categoryFilter: PropTypes.string,
  };

  const [chartData, setChartData] = useState({});
  const chart = () => {
    fetch('/user-stats-servlet')
        .then((response) => response.json())
        .then((responseJson) => {
          const itemNames = [];
          const itemValues = [];
          const itemsJson = JSON.parse(responseJson.items);
          const items = {};
          itemsJson.forEach((item) => {
            if ((props.dateFilter === '' ||
                 inSameWeek(item.date, props.dateFilter)) &&
                (props.categoryFilter === '' ||
                  item.category === props.categoryFilter)) {
              if (items[item.name]) {
                items[item.name] = items[item.name] + item.quantity;
              } else {
                items[item.name] = item.quantity;
              }
            }
          });
          Object.keys(items).forEach((item) => {
            itemNames.push(item);
            itemValues.push(items[item]);
          });
          setChartData({
            labels: itemNames,
            datasets: [
              {
                label: 'Week Total',
                data: itemValues,
                backgroundColor: ['rgba(75, 192, 192, 0.6)'],
                borderWidth: 4,
              },
            ],
          });
        })
        .catch((err) => {
          console.log(err);
        });
  };

  useEffect(() => {
    chart();
  }, []);

  /** returns the react node */
  return (
    <Doughnut
      data={chartData}
      width={300}
      height={300}
      options={{
        maintainAspectRatio: false,
        title: {
          display: true,
          text: 'Item Aggregate',
          fontSize: 20,
        },
        legend: {
          display: true,
          position: 'bottom',
        },
      }}
    />
  );
};

export default LineChart;
export {BarGraph, CategoryDoughnutChart, ItemDoughnutChart,
  retrieveWeekData, inSameWeek};
