import React from 'react';
import "jest-fetch-mock";
enableFetchMocks();
import ReceiptInput from './ReceiptInput';
import './setupTests.js';
import App from './App';
import UserChart from "./UserChart";


it('UserChart should be mounted to App', () => {
  HTMLCanvasElement.prototype.getContext = jest.fn()
  jest.mock('react-chartjs-2', () => ({
      UserChart: () => null,
    }))
  const obj = {weeklyAggregate: } 

  const component = mount(<App />);
  expect(component.contains(<UserChart />)).toBeTruthy();
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
// });
