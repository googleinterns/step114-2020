import React from 'react';
import { shallow } from 'enzyme';

import { enableFetchMocks } from 'jest-fetch-mock'
enableFetchMocks();

import UserInfoModalBox from './UserInfoModalBox';

import '../setupTests.js'

let component;
let form;

describe('UserInfoModalBox must', () => {
  
  beforeEach(() => {
    component = shallow(<UserInfoModalBox />);
    form = component.find('Form');
  });

  afterEach(() => {
    component.unmount();
  });

  it('render', () => {
    expect(component.exists()).toBe(true);
  });

  it('have post method', () => {
    expect(form.props().method).toBe('POST');
  });

  it('be directed to login endpoint', () => {
    expect(form.props().action).toBe('/login');
  });

  it('have first name field', () => {
    expect(form.find('.first-name').exists()).toBe(true);
  });

  it('have last name field', () => {
    expect(form.find('.last-name').exists()).toBe(true);
  });

  it('have username field', () => {
    expect(form.find('.username').exists()).toBe(true);
  });

  it('have favorite-store field', () => {
    expect(form.find('.favorite-store').exists()).toBe(true);
  });
});
