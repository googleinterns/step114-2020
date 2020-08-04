import {enableFetchMocks} from 'jest-fetch-mock'
enableFetchMocks();
import {mount} from 'enzyme';
import React from 'react';
import './setupTests.js';
import App from './App';
import LineChart, {retrieveWeekData, BarGraph, inSameWeek,
                   CategoryDoughnutChart, ItemDoughnutChart} from "./UserChart";

const component = mount(<App />);
const showItemChart = jest.spyOn(App.prototype, 'showItemChart');

it('Should parse the correct week data in retrieveWeekData', () => {
  const obj = {weeklyAggregate: '[{"date":"2020-07-19","total":"25.0"}]'};
  const response = JSON.stringify(obj);
  fetch.mockResponse(response);

  return retrieveWeekData().then((fetchData) => {
    expect(fetchData[0]).toStrictEqual(["2020-07-19"]);
    expect(fetchData[1]).toStrictEqual(["25.0"]);
  });
});

it('Should parse the correct week data with multiple weeks in retrieveWeekData', () => {
  const obj = {weeklyAggregate: '[{"date":"2020-07-19","total":"25.0"},'
                                + '{"date":"2020-07-26","total":"5.0"}]'};
  const response = JSON.stringify(obj);
  fetch.mockResponse(response);

  return retrieveWeekData().then((fetchData) => {
    expect(fetchData[0]).toStrictEqual(["2020-07-19", "2020-07-26"]);
    expect(fetchData[1]).toStrictEqual(["25.0", "5.0"]);
  });
});

it('Should return true when inSameWeek("2020-07-25", "2020-07-26") is called', () => {
  return expect(inSameWeek('2020-07-25', '2020-07-26')).toBe(true);
});

it('Should return true when inSameWeek("2020-07-26", "2020-07-26") is called', () => {
  return expect(inSameWeek('2020-07-26', '2020-07-26')).toBe(true);
});

it('Should return false when inSameWeek("2020-07-19", "2020-07-26") is called', () => {
  return expect(inSameWeek('2020-07-19', '2020-07-26')).toBe(false);
});

it('Should return false when inSameWeek("2020-07-27", "2020-07-26") is called', () => {
  return expect(inSameWeek('2020-07-27', '2020-07-26')).toBe(false);
});

it('Should mount radio-selector to App', () => {
  return expect(component.find("#chart-selector")).toBeTruthy();
});

it('Should mount LineChart to App', () => {
  return expect(component.find(<LineChart />)).toBeTruthy();
});

it('Should mount BarGraph to App when selected', () => {
  component.find("#bar").simulate('click', {target: {checked: true}})
  return expect(component.find(<BarGraph />)).toBeTruthy();
});

it('Should mount LineChart to App when selected', () => {
  component.find("#line").simulate('click', {target: {checked: true}})
  return expect(component.find(<LineChart />)).toBeTruthy();
});

it('Should mount CategoryDoughnutChart to App when selected', () => {
  component.find("#doughnut").simulate('click', {target: {checked: true}})
  return expect(component.find(<CategoryDoughnutChart />)).toBeTruthy();
});
