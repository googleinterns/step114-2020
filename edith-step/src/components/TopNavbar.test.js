import React from 'react';
import { mount } from 'enzyme';
import TopNavbar from './TopNavbar';

import '../setupTests.js'

let component;
let navLinks

beforeEach(() => {
  component = mount(<TopNavbar />);
})

afterEach(() => {
  component.unmount();
});

// Checks if the Navigation Bar is rendered correctly.
test('renders', () => {
  expect(component.exists()).toBe(true);
});

test('has three options', () => {
  expect(component.find('.links').length).toBe(3);
});


test('has Home option', () => {
  expect(component.find('.home').exists()).toBe(true);
});

test('has Features option', () => {
  expect(component.find('.features').exists()).toBe(true);
});

test('has Login option', () => {
  expect(component.find('.login-button').exists()).toBe(true);
});
