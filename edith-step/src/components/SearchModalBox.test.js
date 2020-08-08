import React from 'react';
import {shallow} from 'enzyme';

import SearchModalBox from './SearchModalBox';

import '../setupTests.js';

let component;
let form;

describe('SearchModalBox must', () => {
  beforeEach(() => {
    component = shallow(<SearchModalBox />);
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
    expect(form.props().action).toBe('/search-entity');
  });

  it('have kind field', () => {
    expect(form.find('.kind').exists()).toBe(true);
  });

  it('have kind field with receipt and item options', () => {
    const kind = form.find('.kind');
    expect(kind.find('option').at(0).text()).toBe('Receipt');
    expect(kind.find('option').at(1).text()).toBe('Item');
  });

  it('have name field', () => {
    expect(form.find('.name').exists()).toBe(true);
  });

  it('have date field', () => {
    expect(form.find('.date').exists()).toBe(true);
  });

  it('have sort order field', () => {
    expect(form.find('.sort-order').exists()).toBe(true);
  });

  it('have sort order field with Ascending and Descending options', () => {
    const sortOrder = form.find('.sort-order');
    expect(sortOrder.find('option').at(0).text()).toBe('Ascending');
    expect(sortOrder.find('option').at(1).text()).toBe('Descending');
  });

  it('have sort on field', () => {
    expect(form.find('.sort-on').exists()).toBe(true);
  });

  it('have sort on field with Name and Price options', () => {
    const sortOn = form.find('.sort-on');
    expect(sortOn.find('option').at(0).text()).toBe('Name');
    expect(sortOn.find('option').at(1).text()).toBe('Price');
  });

  it('have submit button', () => {
    expect(form.find('.submit').exists()).toBe(true);
  });
});
