import React from 'react';
import {mount} from 'enzyme';
import TopNavbar from './TopNavbar';
import '../setupTests.js';

let component;

beforeEach(() => {
  component = mount(<TopNavbar />);
})

afterEach(() => {
  component.unmount();
});

// Checks if the Navigation Bar is rendered.
test('renders', () => {
  expect(component.exists()).toBe(true);
});

// Checks if the navigation Bar provides three different options.
test('has three options', () => {
  expect(component.find('.links').length).toBe(3);
});

// Checks if there is Home option.
test('has Home option', () => {
  expect(component.find('.home').exists()).toBe(true);
});

// Checks if there is Features option.
test('has Features option', () => {
  expect(component.find('.features').exists()).toBe(true);
});

// Checks if there is Login option.
test('has Login option', () => {
  expect(component.find('.login-button').exists()).toBe(true);
});
