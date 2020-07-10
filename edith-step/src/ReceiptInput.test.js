import React from 'react';
import { mount, flushPromises } from 'enzyme';
import ReceiptInput from './ReceiptInput';
import { screen, wait, getByText } from '@testing-library/react'
import './setupTests.js'

let component;
let handleChange;
let handleSubmit;

beforeEach(() => {
  handleSubmit = jest.spyOn(ReceiptInput.prototype, 'handleSubmit').mockResolvedValue();
  handleChange = jest.spyOn(ReceiptInput.prototype, 'handleChange');
  component = mount(<ReceiptInput onSubmit={ handleSubmit } onChange={ handleChange }/>);
})

afterEach(() => {
  component.unmount();
});

// ReceiptInput component renders properly.
it('renders properly', () => {
  expect(component.exists()).toBe(true);
});

// Submit button calls handleSubmit when clicked.
it('should call handleSumbit when Submit button is clicked', () => {
  component.find('form').simulate('submit');
  expect(handleSubmit).toBeCalled();
});

// handleSubmit resets state.
it('should update form submitted state with button click', async () => {
  component.setState({ itemName: "bread", itemPrice: 5.6, itemQuantity: 3 });
  const promise = new Promise(handleSubmit);
  component.find('form').simulate('submit');
  promise.then(() => {
    expect(component.state('itemName')).toBe('');
    expect(component.state('itemPrice')).toBe(0.0);
    expect(component.state('itemQuantity')).toBe(1);
  });
});

// handleChange is called on change.
it('should call handleChange on form change', () => {
  component.find('#name').simulate('change');
  expect(handleChange).toBeCalled();
  component.find('#price').simulate('change');
  expect(handleChange).toBeCalled();
  component.find('#quantity').simulate('change');
  expect(handleChange).toBeCalled();
});

// handleChange updates state.
it('should change state when handleChange is called', () => {
  component.setState({ itemName: "", itemPrice: 0, itemQuantity: 1 });
  expect(component.state('itemName')).toBe('');
  expect(component.state('itemPrice')).toBe(0.0);
  expect(component.state('itemQuantity')).toBe(1);

  const textEvent = {target: { name: "itemName", value: "bread" }};
  const priceEvent = {target: { name: "itemPrice", value: 5.6 }};
  const quantityEvent = {target: {name: "itemQuantity", value: 3}};

  component.find('#name').simulate('change', textEvent);
  expect(component.state('itemName')).toBe('bread');
  component.find('#price').simulate('change', priceEvent);
  expect(component.state('itemPrice')).toBe(5.6);
  component.find('#quantity').simulate('change', quantityEvent);
  expect(component.state('itemQuantity')).toBe(3);
});

// GroceryList renders correct text on submit.
it('should display item when form submitted', async () => {
  const textFieldBefore = component.find('.item-name');
  expect(textFieldBefore.exists()).toBe(false);
  const priceFieldBefore = component.find('.item-price');
  expect(priceFieldBefore.exists()).toBe(false);
  const quantityFieldBefore = component.find('.item-quantity');
  expect(quantityFieldBefore.exists()).toBe(false);

  component.setState({ itemName: "bread", itemPrice: 5.6, itemQuantity: 3 });

  const promise = new Promise(handleSubmit);
  component.find('form').simulate('submit');
  promise.then(() => {
    const textFieldAfter = component.find('.item-name').text();
    expect(textFieldAfter).toBe('bread');
    const priceFieldAfter = component.find('.item-price').text();
    expect(priceFieldAfter).toBe("5.6");
    const quantityFieldAfter = component.find('.item-quantity').text();
    expect(quantityFieldAfter).toBe('3');
  });
});

it('should display item deal when form submitted', async () => {
  const dealFieldBefore = component.find('.item-deal');
  expect(dealFieldBefore.exists()).toBe(false);

  component.setState({ itemName: "Apple Juice", itemPrice: 5.6, itemQuantity: 3 });

  const promise = new Promise(handleSubmit);
  component.find('form').simulate('submit');
  promise.then(() => {
    const dealFieldAfter = component.find('item-deal');
    expect(dealFieldAfter).toBe("Purchase at Kroger for $1.5");
  });
});
