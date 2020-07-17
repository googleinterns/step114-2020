import React, {useState, useEffect} from 'react';
import {Line, Bar, Pie} from 'react-chartjs-2';
import axios from "axios";

const LineChart = () => {
    
  const [chartData, setChartData] = useState({});
  const chart = () => {
    axios
      .get("/user-stats-servlet")
      .then(response => {
        console.log(response);
        let weekDates = [];
        let values = [];  
        const weeklyAggregate = JSON.parse(response.data.weeklyAggregate)
        weekDates = Object.keys(weeklyAggregate);
        for (let i = 0; i < weekDates.length; i++) {
          values.push(weeklyAggregate[weekDates[i]]);
        }
        setChartData({
          labels: weekDates,
          datasets: [
            {
              label: "Week Total",
              data: values,
              backgroundColor: ["rgba(75, 192, 192, 0.6)"],
              borderWidth: 4
            }
          ]
        });
        console.log(weekDates, values);
      })
      .catch(err => {
        console.log(err);
      });
    };

  useEffect(() => {
    chart();
  }, []);

  return (
      <Line
        data={chartData}
        width={100}
        height={100}
        options={{
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
                labelString: 'Dollars'
              },
              ticks: {
                beginAtZero: true
              }
            }],
            xAxes: [{
              scaleLabel: {
                display: true,
                labelString: 'Week'
              }
            }],
          }    
        }}
      />
    );
  }

const BarGraph = () => {
    
  const [chartData, setChartData] = useState({});
  const chart = () => {
    axios
      .get("/user-stats-servlet")
      .then(response => {
        console.log(response);
        let weekDates = [];
        let values = [];  
        const weeklyAggregate = JSON.parse(response.data.weeklyAggregate)
        weekDates = Object.keys(weeklyAggregate);
        for (let i = 0; i < weekDates.length; i++) {
          values.push(weeklyAggregate[weekDates[i]]);
        }
        setChartData({
          labels: weekDates,
          datasets: [
            {
              label: "Weeek Total",
              data: values,
              backgroundColor: ["rgba(75, 192, 192, 0.6)"],
              borderWidth: 4
            }
          ]
        });
        console.log(weekDates, values);
      })
      .catch(err => {
        console.log(err);
      });
    };

  useEffect(() => {
    chart();
  }, []);

  return (
      <Bar
        data={chartData}
        width={100}
        height={100}
        options={{
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
                labelString: 'Dollars'
              },
              ticks: {
                beginAtZero: true
              }
            }],
            xAxes: [{
              scaleLabel: {
                display: true,
                labelString: 'Week'
              }
            }],
          }    
        }}
      />
    );
  }


const PieChart = () => {
    
  const [chartData, setChartData] = useState({});
  const chart = () => {
    axios
      .get("/user-stats-servlet")
      .then(response => {
        console.log(response);
        let itemNames = [];
        let itemValues = [];  
        const items = JSON.parse(response.data.items);
        console.log(items);
        for (let i = 0; i < items.length; i++) {
            itemNames.push(items[i].name);
        }
        for (let i = 0; i < items.length; i++) {
          const item = items[i];
          itemValues.push(items[i].quantity);
        }
        setChartData({
          labels: itemNames,
          datasets: [
            {
              label: "Weeek Total",
              data: itemValues,
              backgroundColor: ["rgba(75, 192, 192, 0.6)"],
              borderWidth: 4
            }
          ]
        });
        console.log(itemNames, itemValues);
      })
      .catch(err => {
        console.log(err);
      });
    };

  useEffect(() => {
    chart();
  }, []);

  return (
      <Pie
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
          legend:{
            display: true,
            position:'right'
          },
        }}
      />
    );
  }

export default LineChart
export { BarGraph, PieChart }
