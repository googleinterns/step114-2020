import {enableFetchMocks} from 'jest-fetch-mock'
enableFetchMocks();
import React from 'react';
import { mount } from 'enzyme';
import App from './App';
import './setupTests.js'

let component;

beforeEach(() => {
  component = mount(<App />);
})

afterEach(() => {
  component.unmount();
});

describe('App component', () => {
  // Checks if the app is rendered.
  test('renders', () => {
    expect(component.exists()).toBe(true);
  });

  test('has showSearchResults state false', () => {
    expect(component.state('showSearchResults')).toBe(false);
  });

  test('has showSearchResults state true when button is clicked', () => {
    const showSearchResultButton = component.find('Button');
    console.log(showSearchResultButton.debug());
    showSearchResultButton.simulate('click');
    expect(component.state('showSearchResults')).toBe(true);
  });
});
