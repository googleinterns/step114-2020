import React from 'react';
import { mount, unmount } from 'enzyme';
import ReceiptInput from './ReceiptInput';
import axios from 'axios';
jest.mock('axios');
import './setupTests.js';
import App from './App';
import UserChart from "./UserChart";

it('UserChart should be mounted to App', () => {
  HTMLCanvasElement.prototype.getContext = jest.fn()
  jest.mock('react-chartjs-2', () => ({
      UserChart: () => null,
    }))
  axios.get.mockResolvedValue({
      data: {
        labels: [],
        datasets: [
          {
            label: "Weeek Total",
            data: [],
            backgroundColor: ["rgba(75, 192, 192, 0.6)"],
            borderWidth: 4
          }
        ]
      }
  });  
  const component = mount(<App />);
  expect(component.contains(<UserChart />)).toBeTruthy();
});

it('Axios test', () => {
  axios.get.mockResolvedValue({
      data: {
          items: ["item1"]
      } 
  });

  return axios.get("/test-url")
            .then((response) => expect(response.data.items)
            .toEqual(["item1"]));
});
