import React from 'react';
import {mount} from 'enzyme';
import {enableFetchMocks} from 'jest-fetch-mock';
enableFetchMocks();

import App from './App';

import './setupTests.js';

let component;

beforeEach(() => {
  component = mount(<App />);
});

afterEach(() => {
  component.unmount();
});

describe('App component', () => {
  // Checks if the app is rendered.
  test('renders', () => {
    expect(component.exists()).toBe(true);
  });

  // Checks TopNavbar is in App.
  test('contains TopNavbar component', () => {
    const topNavBar = component.find('TopNavbar');
    expect(topNavBar.exists()).toBe(true);
  });

  // Checks ReceiptInput is in App.
  test('contains ReceiptInput component', () => {
    const ReceiptInput = component.find('ReceiptInput');
    expect(ReceiptInput.exists()).toBe(true);
  });

  // Checks Background image is in App.
  test('contains background image', () => {
    const backgroundImage = component.find('.background-image-0');
    expect(backgroundImage.exists()).toBe(true);
  });

  // Checks app description is in App.
  test('contains app description', () => {
    const appDescribe = component.find('.app-describe');
    expect(appDescribe.exists()).toBe(true);
  });
});
