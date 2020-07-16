import React from 'react';
import { mount } from 'enzyme';
import ReceiptHandler from './ReceiptHandler';
import './setupTests.js'

let component;
let handleItemChange;
let getReceiptData;

beforeEach(() => {
  handleItemChange = jest.spyOn(ReceiptHandler.prototype, 'handleItemChange');
  getReceiptData = jest.spyOn(ReceiptHandler.prototype, 'getReceiptData');
  component = mount(<ReceiptHandler onChange={ handleItemChange }/>);
})

afterEach(() => {
  component.unmount();
});

// ReceiptHandler component renders properly.
it('renders properly', () => {
  expect(component.exists()).toBe(true);
});

// ReceiptHandler component renders properly.
it('calls getReceiptData on mount', () => {
  expect(getReceiptData).toBeCalled();
});

// handleItemChange is called on change.
it('should call handleItemChange on form change', () => {
  const newItem = {
    name: '',
    price: 0.0,
    quantity: 1
  }
  component.setState({ items: newItem });
  expect(component.state('items')).toBe(newItem);

  const promise = new Promise(getReceiptData);
  promise.then(() => {
    component.find('.name').simulate('change');
    expect(handleItemChange).toBeCalled();
    component.find('.price').simulate('change');
    expect(handleItemChange).toBeCalled();
    component.find('.quantity').simulate('change');
    expect(handleItemChange).toBeCalled();
  });
});

// handleItemChange updates items list in state.
it('should change state when handleItemChange is called', () => {
  const newItem = {
    name: '',
    price: 0.0,
    quantity: 1
  }
  component.setState({ items: newItem });
  expect(component.state('items')).toBe(newItem);

  const textEvent = {target: { name: "items[0].name", value: 'bread' }};
  const priceEvent = {target: { name: "items[0].price", value: 5.6 }};
  const quantityEvent = {target: {name: "items[0].quantity", value: 3}};

  const targetItem = {
    name: 'bread',
    price: 5.6,
    quantity: 3
  }
  const promise = new Promise(getReceiptData);
  promise.then(() => {
    component.find('.name').simulate('change', textEvent);
    component.find('.price').simulate('change', priceEvent);
    component.find('.quantity').simulate('change', quantityEvent);
    expect(component.state('items')).toBe(targetItem);
  });
});
