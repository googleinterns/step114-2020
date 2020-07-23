import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks();
import {mount} from 'enzyme';
import React from 'react';
import ReceiptInput from './ReceiptInput';
import './setupTests.js';
import App from './App';
import LineChart, {retrieveData, BarGraph, DoughnutChart} from "./UserChart";


const component = mount(<App />);

it('UserChart should be mounted to App', () => {
  HTMLCanvasElement.prototype.getContext = jest.fn()
  jest.mock('react-chartjs-2', () => ({
      UserChart: () => null,
    }));
  const obj = { "weeklyAggregate": { w20200719: 25.0} };
  const response = JSON.stringify(obj);
  fetch.mockResponse(response);

  return retrieveData().then(response => {
      expect(response[0][0].toBe("w20200719"))
  });
});

// it('Axios test', () => {
//   axios.get.mockResolvedValue({
//       data: {
//           items: ["item1"]
//       } 
//   });

//   return axios.get("/test-url")
//             .then((response) => expect(response.data.items)
//             .toEqual(["item1"]));

// it("Should mount LineChart to App by default", () => {
//   HTMLCanvasElement.prototype.getContext = jest.fn()
//   jest.mock('react-chartjs-2', () => ({
//       UserChart: () => null,
//     }))
//   axios.get.mockResolvedValue({
//    component = mount(<App />);
//   const chartSelection = component.find("#radio-button");
//   expect(c  data: {
//       items: [{"name": "corn", "price": 5.0, "quantity": 5,"date": "2020-07-17"}],
//       weeklyAggregate: {"2020-07-19":"25.0"}
//     } 
//   });  
//   consthartSelection.exists()).toBeTruthy();
// });
