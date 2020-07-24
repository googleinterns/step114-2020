import React, {useState, useEffect} from 'react';
import {Line, Bar, Doughnut} from 'react-chartjs-2';
import axios from "axios";
require("regenerator-runtime/runtime")

async function retrieveData() {
    const response = await fetch("/user-stats-servlet");
    const responseJson = await response.json();
    console.log(responseJson);
    let weekDates = [];
    let values = [];  
    const weeklyAggregate = responseJson['weeklyAggregate'];
    weeklyAggregate.forEach(week => {
      weekDates.push(week.date);
      values.push(week.total);
    });
    
    return [weekDates, values];
}

const inSameWeek = (itemDate, dateSelection) => {
  itemDate = new Date(itemDate);
  dateSelection = new Date(dateSelection);
  const diffTime = dateSelection - itemDate;
  const diffDays = diffTime / (1000 * 60 * 60 * 24);
  return diffDays < 6 && diffDays > 0;
}

const LineChart = (props) => {
    
  const [chartData, setChartData] = useState({});
  const chart = () => {
    retrieveData().then(fetchData => {
    setChartData({
          labels: fetchData[0],
          datasets: [
            {
              label: "Week Total",
              data: fetchData[1],
              backgroundColor: ["rgba(75, 192, 192, 0.6)"],
              borderWidth: 4
            }
          ]
        });
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
          onClick: (event, element) => {
            const dateSelection = element[0]._chart.config.data.labels[element[0]._index];
            props.action(dateSelection);
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

const BarGraph = (props) => {
    
  const [chartData, setChartData] = useState({});
  const chart = () => {
    retrieveData(setChartData);
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
          onClick: (event, element) => {
            const dateSelection = element[0]._chart.config.data.labels[element[0]._index];
            props.action(dateSelection);
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

const DoughnutChart = (props) => {
    
  const [chartData, setChartData] = useState({});
  const chart = () => {
    axios
      .get("/user-stats-servlet")
      .then(response => {
        console.log(response);
        let itemNames = [];
        let itemValues = [];  
        let items = [];
        const itemsList = JSON.parse(response.data.items);
        if (props.dateSelection != "") {
          console.log(props.dateSelection);
          for (let i = 0; i < itemsList.length; i++) {
            inSameWeek(itemsList[i].date, props.dateSelection);
            console.log(itemsList[i]);
            if (inSameWeek(itemsList[i].date, props.dateSelection)) {
              items.push(itemsList[i]);
              console.log(items);
            }
          } 
          props.revertAction();
        } else {
            items = itemsList;
        }
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
export { BarGraph, DoughnutChart, retrieveData }
