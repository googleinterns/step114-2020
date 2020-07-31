import {enableFetchMocks} from 'jest-fetch-mock';
enableFetchMocks();
import React from 'react';
import {mount} from 'enzyme';
import TopNavbar from './TopNavbar';

import '../setupTests.js';

let component;
let handleUploadModalClose;
let handleUserInfoModalBoxClose;
const userObj = {'email': 'email'};

describe('TopNavbar calls', () => {
  test('login method when mounted', () => {
    const login = jest.spyOn(TopNavbar.prototype, 'login');
    component = mount(<TopNavbar />);
    expect(login).toBeCalled();
  });
});

describe('When not logged in, Top Navigation Bar', () => {
  beforeEach(() => {
    component = mount(<TopNavbar />);
    component.setState({user: null});
  });

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

  // Checks if there is Dashboard option.
  test('has Dashboard section', () => {
    expect(component.find('.dashboard').exists()).toBe(false);
  });

  // Checks if dropdown is shown.
  test('should not display dropdown', () => {
    expect(component.find('.dropdowns').exists()).toBe(false);
  });
});

describe('When logged in, Top Navigation Bar', () => {
  beforeEach(() => {
    handleUploadModalClose = jest
        .spyOn(TopNavbar.prototype, 'handleUploadModalClose');
    handleUserInfoModalBoxClose = jest
        .spyOn(TopNavbar.prototype, 'handleUserInfoModalBoxClose');
    component = mount(<TopNavbar />);
    component.setState({user: userObj});
  });

  afterEach(() => {
    component.unmount();
  });

  // Checks if the Navigation Bar is rendered.
  test('renders', () => {
    expect(component.exists()).toBe(true);
  });

  // Checks if there is Dashboard option.
  test('has Dashboard section', () => {
    expect(component.find('.dashboard').exists()).toBe(true);
  });

  // Checks if there is Login option.
  test('does not have Login option', () => {
    expect(component.find('.login-button').exists()).toBe(false);
  });

  // Checks if there is Dropdown menu.
  test('has Dropdown menu', () => {
    expect(component.find('.dropdowns').exists()).toBe(true);
  });

  // Checks uploadModalBoxShow only opens when Upload Receipt
  // button is clicked from Dropdown.
  test('opens upload modal box when upload receipt button is clicked', () => {
    expect(component.state('uploadModalBoxShow')).toBe(false);
    component.find('.dropdown-toggle').at(0).simulate('click');
    component.find('.upload-receipt').at(0).simulate('click');
    expect(component.state('uploadModalBoxShow')).toBe(true);
  });

  // Checks uploadModalBoxShow closes when close button is clicked.
  test('should close upload modal box when close button is clicked', () => {
    component.find('.dropdown-toggle').at(0).simulate('click');
    component.find('.upload-receipt').at(0).simulate('click');
    component.find('.close').at(0).simulate('click');
    expect(handleUploadModalClose).toBeCalled();
  });

  // Checks handleUserInfoModalBoxClose only opens when Upload
  // Receipt button is clicked from Dropdown.
  test('opens user info modal box when update information button is clicked',
      () => {
        expect(component.state('userInfoModalBoxShow')).toBe(false);
        component.find('.dropdown-toggle').at(0).simulate('click');
        component.find('.update-info').at(0).simulate('click');
        expect(component.state('userInfoModalBoxShow')).toBe(true);
      });

  // Checks handleUserInfoModalBoxClose closes when close button is clicked.
  test('should close upload modal box when close button is clicked', () => {
    component.find('.dropdown-toggle').at(0).simulate('click');
    component.find('.update-info').at(1).simulate('click');
    component.find('.close').at(0).simulate('click');
    expect(handleUserInfoModalBoxClose).toBeCalled();
    expect(component.state('userInfoModalBoxShow')).toBe(false);
  });
});
