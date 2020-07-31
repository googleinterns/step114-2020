import {enableFetchMocks} from 'jest-fetch-mock'
enableFetchMocks();
import {renderHook} from '@testing-library/react-hooks/pure';
import {mount} from 'enzyme';
import React from 'react';
import ReceiptInput from './ReceiptInput';
import './setupTests.js';
import App from './App';
import LineChart, {retrieveWeekData, BarGraph, DoughnutChart} from "./UserChart";


it('Should parse the correct week data in retrieveWeekData', () => {
  const obj = { weeklyAggregate: '[{"date":"2020-07-19","total":"25.0"}]' };
  const response = JSON.stringify(obj);
  fetch.mockResponse(response);

  return retrieveWeekData().then(fetchData => {
    expect(fetchData[0]).toStrictEqual(["2020-07-19"]);
    expect(fetchData[1]).toStrictEqual(["25.0"]);
  });
});

it('Should mount radio-selector to App', () => {
  const component = mount(<App />);
  return expect(component.find("#chart-selector")).toBeTruthy();
});

it('Should mount LineChart to App', () => {
  const component = mount(<App />);
  console.log(component.debug());
  return expect(component.find(<LineChart />)).toBeTruthy();
});

it('Should mount BarGraph to App when selected', () => {
  const component = mount(<App />);
  return expect(component.find(<BarGraph />)).toBeTruthy();
});



