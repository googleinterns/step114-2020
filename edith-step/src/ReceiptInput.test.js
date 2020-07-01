import React from 'react';
import { mount } from 'enzyme';
import ReceiptInput from './ReceiptInput';
import './setupTests.js'

let component;
let handleChange;
let handleSubmit;

beforeEach(() => {
  handleSubmit = jest.spyOn(ReceiptInput.prototype, 'handleSubmit');
  handleChange = jest.spyOn(ReceiptInput.prototype, 'handleChange');
  component = mount(<ReceiptInput onSubmit={ handleSubmit } onChange={ handleChange }/>);
})

afterEach(() => {
  component.unmount();
});

// Submit button calls handleSubmit when clicked.
it('should call handleSumbit when Submit button is clicked', () => {
  component.find('form').simulate('submit');
  expect(handleSubmit).toBeCalled();
});

// handleSubmit resets state.
it('should update form submitted state with button click', () => {
  component.setState({ itemName: "bread", itemPrice: 5.6 });
  component.find('form').simulate('submit');
  
  expect(component.state('itemName')).toBe('');
  expect(component.state('itemPrice')).toBe(0.0);
});

// handleChange is called on change.
it('should call handleChange on form change', () => {
  component.find('#name').simulate('change');
  expect(handleChange).toBeCalled();
  component.find('#price').simulate('change');
  expect(handleChange).toBeCalled();;
});

// handleChange updates state.
it('should change state when handleChange is called', () => {
  component.setState({ itemName: "", itemPrice: 0 });
  expect(component.state('itemName')).toBe('');
  expect(component.state('itemPrice')).toBe(0.0);

  const textEvent = {target: { name: "itemName", value: "bread" }};
  const priceEvent = {target: { name: "itemPrice", value: 5.6 }};

  component.find('#name').simulate('change', textEvent);
  expect(component.state('itemName')).toBe('bread');
  component.find('#price').simulate('change', priceEvent);
  expect(component.state('itemPrice')).toBe(5.6);
});

// GroceryList renders correct text on submit.
it('should display item when form submitted', () => {
  component.setState({ itemName: "bread", itemPrice: 5.6 });
  component.find('form').simulate('submit');

  const textField = component.find('.item-name').text();
  expect(textField).toBe('bread');
  const priceField = component.find('.item-price').text();
  expect(priceField).toBe("5.6");
});
