import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks();
import {mount} from 'enzyme';
import React from 'react';
import ReceiptInput from './ReceiptInput';
import './setupTests.js';
import App from './App';
import LineChart, {retrieveData, BarGraph, DoughnutChart} from "./UserChart";


const component = mount(<App />);

it('Should receive the correct week data from retrieveData', () => {
//   HTMLCanvasElement.prototype.getContext = jest.fn()
//   jest.mock('react-chartjs-2', () => ({
//       UserChart: () => null
//     }));
  const obj = { weeklyAggregate: '[{"date":"2020-07-19","total":"25.0"}]' };
  const response = JSON.stringify(obj);
  fetch.mockResponse(response);

  const data = retrieveData();
  return data.then((resultArray) => {
      expect(resultArray[0]).toStrictEqual(["2020-07-19"]);
      expect(resultArray[1]).toStrictEqual(["25.0"]);
  });
});

it('Should receive the correct week data from retrieveData', () => {
//   HTMLCanvasElement.prototype.getContext = jest.fn()
//   jest.mock('react-chartjs-2', () => ({
//       UserChart: () => null
//     }));
  const obj = { weeklyAggregate: '[{"date":"2020-07-19","total":"25.0"}]' };
  const response = JSON.stringify(obj);
  fetch.mockResponse(response);
  
  const data = retrieveData();
  return data.then((resultArray) => {
      expect(resultArray[0]).toStrictEqual(["2020-07-19"]);
      expect(resultArray[1]).toStrictEqual(["25.0"]);
  });
});



