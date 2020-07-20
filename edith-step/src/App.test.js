import React from 'react';
import {mount} from 'enzyme';

import App from './App';

import './setupTests.js';
import { enableFetchMocks } from 'jest-fetch-mock'

let component;

beforeEach(() => {
  component = mount(<App />);
  enableFetchMocks();
});

afterEach(() => {
  component.unmount();
});

describe('App component', () => {
  // Checks if the app is rendered.
  test('renders', () => {
    expect(component.exists()).toBe(true);
  });
});
