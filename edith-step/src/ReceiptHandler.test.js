import React from 'react';
import { mount } from 'enzyme';
import ReceiptHandler from './ReceiptHandler';
import './setupTests.js'

let component;
let handleChange;

beforeEach(() => {
  handleChange = jest.spyOn(ReceiptInput.prototype, 'handleChange');
  component = mount(<ReceiptHandler onChange={ handleChange }/>);
})

afterEach(() => {
  component.unmount();
});

// ReceiptInput component renders properly.
it('renders properly', () => {
  expect(component.exists()).toBe(true);
});

// handleChange is called on change.
it('should call handleChange on form change', () => {
  component.find('.name').simulate('change');
  expect(handleChange).toBeCalled();
  component.find('.price').simulate('change');
  expect(handleChange).toBeCalled();
  component.find('.quantity').simulate('change');
  expect(handleChange).toBeCalled();
});

// handleChange updates state.
it('should change state when handleChange is called', () => {
  const newItem = {
    name: 'bread',
    price: 5.6,
    quantity: 3
  }
  component.setState({ items: newItem });
  expect(component.state('items')).toBe(newItem);

  const textEvent = {target: { name: "itemName", value: "bread" }};
  const priceEvent = {target: { name: "itemPrice", value: 5.6 }};
  const quantityEvent = {target: {name: "itemQuantity", value: 3}};

  component.find('.name').simulate('change', textEvent);
  expect(component.state('itemName')).toBe('bread');
  component.find('.price').simulate('change', priceEvent);
  expect(component.state('itemPrice')).toBe(5.6);
  component.find('.quantity').simulate('change', quantityEvent);
  expect(component.state('itemQuantity')).toBe(3);
});
