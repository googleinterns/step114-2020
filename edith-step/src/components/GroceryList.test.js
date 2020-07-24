import React from 'react';
import {mount} from 'enzyme';
import GroceryList from './GroceryList.js';
import './setupTests.js';

let component;
let getItemData;

beforeEach(() => {
  component = mount(<GroceryList />);
  getItemData = jest.spyOn(GroceryList.prototype, 'getItemData');
});

afterEach(() => {
  component.unmount();
});

it('mounts properly', () => {
  expect(component.exists()).toBe(true);
});

it('calls getItemData on mount', () => {
  expect(getItemData).toBeCalled();
});

it('displays items', () => {
  expect(component.find('.item-name').exists()).toBe(false);
  const newItem = {
    name: '',
    price: 0.0,
    quantity: 1,
  };
  component.setState({items: newItem});
  expect(component.state('items')).toBe(newItem);

  const promise = new Promise(getItemData);
  promise.then(() => {
    expect(component.find('.item-name').exists()).toBe(true);
  });
});
