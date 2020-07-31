/**
 * This module contains three charts/graphs used to display user information
 * and the helper methods used within them.
 */
import React, {useState, useEffect} from 'react';
import {Line, Bar, Doughnut} from 'react-chartjs-2';
require("regenerator-runtime/runtime")

/**
 * Makes a get request to "/user-stats-servlet"to receieve week information
 * used in all three charts in this module.
 */
async function retrieveWeekData() {
  const response = await fetch("/user-stats-servlet");
  const responseJson = await response.json();
  let weekDates = [];
  let values = [];  
  const weeklyAggregate = JSON.parse(responseJson.weeklyAggregate);
  weeklyAggregate.forEach((week) => {
      weekDates.push(week.date);
      values.push(week.total);
  });
  return [weekDates, values];
}

/**
 * Determines if {@code itemDate} is in the same as {@code dateSelection}
 * (a week starts on Monday and ends on Sunday)
 * @param itemDate - the date the specific item was purchased on
 * @param dateSelection - the date to compare itemDate to
 * @return a boolean representing whether itemDate is in the same week as dateSelection
 */
const inSameWeek = (itemDate, dateSelection) => {
  itemDate = new Date(itemDate);
  dateSelection = new Date(dateSelection);
  const diffTime = dateSelection - itemDate;
  // Then length of a day is 1000ms * 60 seconds * 60 minutues * 24 hours
  const diffDays = diffTime / (1000 * 60 * 60 * 24);
  return diffDays < 6 && diffDays > 0;
}

/**
 * Populates the chart data using information pulled from retrieveWeekData()
 * and sets other phsyical attributes  
 */
async function setChart(setChartData) {
  const fetchData = await retrieveWeekData();
  setChartData({
    labels: fetchData[0],
    datasets: [
    {
      label: "Week Total",
      data: fetchData[1],
      backgroundColor: ['rgb(0, 191, 255, 0.6)', 
                        'rgb(100, 191, 255, 0.6)',
                        'rgb(200, 191, 255, 0.6)',
                        'rgb(0, 0, 255, 0.6)' ],
      borderWidth: 4
    }
    ]
  });
}

/**
 * LineChart that relates weeks with the trailing total calculated for that
 * week. Each data point can be clicked on to show a more in-depth spending
 * for that week.
 * @param props - contains the methods used to update/revert the state to 
 *                display a drill-downed chart and a dateSelection variable
 * @return React node object containing a canvas and a LineChart.
 */
const LineChart = (props) => {
    
  const [chartData, setChartData] = useState({});
  setChart(setChartData);

  useEffect(() => {
    setChart();
  }, []);
   
  /** returns the react node */ 
  return (
      <Line
        data={chartData}
        width={100}
        height={100}
        options={{
          onClick: (event, element) => {
            const dateSelection = element[0]._chart.config.data.labels[element[0]._index];
            props.action("category", dateSelection, '');
          },
          maintainAspectRatio: false,
          title:{
            display: true,
            text: 'Weekly Aggregate (trailing total based on week date)',
            fontSize: 20
          }, 
          legend:{
            display: true,
            position:'right'
          },
          scales: {
            yAxes: [{
              scaleLabel: {
                display: true,
                fontSize: 20,
                labelString: 'Dollars'
              },
              ticks: {
                beginAtZero: true,
              }
            }],
            xAxes: [{
              scaleLabel: {
                display: true,
                fontSize: 20,
                labelString: 'Week'
              },
            }],
          }    
        }}
      />
    );
}

/**
 * BarGraph that relates weeks with the trailing total calculated for that
 * week. Each data point can be clicked on to show a more in-depth spending
 * for that week.
 * @param props - contains the methods used to update/revert the state to 
 *                display a drill-downed chart and a dateSelection variable
 * @return React node object containing a canvas and a BarGraph.
 */
const BarGraph = (props) => {
    
  const [chartData, setChartData] = useState({});
  setChart(setChartData);

  useEffect(() => {
    setChart();
  }, []);

  return (
      <Bar
        data={chartData}
        width={100}
        height={100}
        options={{
          onClick: (event, element) => {
            const dateSelection = element[0]._chart.config.data.labels[element[0]._index];
            props.action("category", dateSelection, '');      
          },  
          maintainAspectRatio: false,
          title:{
            display: true,
            text: 'Weekly Aggregate (trailing total based on week date)',
            fontSize: 20
          }, 
          legend:{
            display: true,
            position:'right'
          },
          scales: {
            yAxes: [{
              scaleLabel: {
                display: true,
                fontSize: 20,
                labelString: 'Dollars'
              },
              ticks: {
                beginAtZero: true,
              }
            }],
            xAxes: [{
              scaleLabel: {
                display: true,
                fontSize: 20,
                labelString: 'Week'
              },
            }],
          }    
        }}
      />
    );
}

/**
 * DoughnutChart that shows the total amount of spending for each item.
 * @param props - contains the methods used to update/revert the state to 
 *                display a drill-downed chart and a dateSelection variable
 * @return React node object containing a canvas and a DoughnutChart.
 */
const CategoryDoughnutChart = (props) => {
    
  const [chartData, setChartData] = useState({});
  const chart = () => {
    fetch("/user-stats-servlet")
      .then((response) => response.json())
      .then((responseJson) => {
        let itemNames = [];
        let itemValues = [];  
        const itemsJson = JSON.parse(responseJson.items);
        let items = {};
        if (props.dateSelection.length > 0) {
          itemsJson.forEach((item) => {
              if(inSameWeek(item.date, props.dateSelection)) {
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
              label: "Week Total",
              data: itemValues,
              backgroundColor: ["rgba(75, 192, 192, 0.6)"],
              borderWidth: 4
            }
          ]
        });
      })
      .catch(err => {
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
        width={100}
        height={100}
        options={{
          onClick: (event, element) => {
            const categorySelection = element[0]._chart.config.data.labels[element[0]._index];
            props.action("item", props.dateSelection, categorySelection);      
          },
          maintainAspectRatio: false,
          title:{
            display: true,
            text: 'Item Aggregate',
            fontSize: 20
          }, 
          legend: {
            display: true,
            position:'bottom',
          }
        }}
      />
  );
}

const ItemDoughnutChart = (props) => {
const [chartData, setChartData] = useState({});
  const chart = () => {
    fetch("/user-stats-servlet")
      .then((response) => response.json())
      .then((responseJson) => {
        let itemNames = [];
        let itemValues = [];  
        const itemsJson = JSON.parse(responseJson.items);
        let items = {};
        itemsJson.forEach((item) => {
          console.log(item);
          if((props.dateSelection === '' || inSameWeek(item.date, props.dateSelection))
              && (props.categorySelection === '' || item.category === props.categorySelection)) {
            if (!(item.name in itemNames)) {
                itemNames.push(item.name);
                itemValues.push(item.quantity);
              }
          }
        });
        console.log(itemNames, itemValues);
        setChartData({
          labels: itemNames,
          datasets: [
            {
              label: "Week Total",
              data: itemValues,
              backgroundColor: ["rgba(75, 192, 192, 0.6)"],
              borderWidth: 4
            }
          ]
        });
      })
      .catch(err => {
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
        width={100}
        height={100}
        options={{
          maintainAspectRatio: false,
          title:{
            display: true,
            text: 'Item Aggregate',
            fontSize: 20
          }, 
          legend: {
            display: true,
            position:'bottom',
          }
        }}
      />
  );
}

export default LineChart
export { BarGraph, CategoryDoughnutChart, ItemDoughnutChart, 
         retrieveWeekData, inSameWeek }
