import React from 'react';
import { render } from '@testing-library/react';
import { shallow, configure, mount } from 'enzyme';
import ReceiptInput from './ReceiptInput';
import './setupTests.js'

// Submit button calls handleSubmit when clicked
it('should call handleSumbit when Submit button is clicked', () => {
  const handleSubmit = jest.spyOn(ReceiptInput.prototype, 'handleSubmit');
  const component = mount(<ReceiptInput onSubmit={ handleSubmit }/>);

  component.find('form').simulate('submit');
  expect(handleSubmit).toBeCalled();
  component.unmount();
});

// handleSubmit resets state
it('should update form submitted state with button click', () => {
  const component = mount(<ReceiptInput />);
  component.setState({ text: "bread", price: 5.6 });
  component.find('form').simulate('submit');
  
  expect(component.state('text')).toBe('');
  expect(component.state('price')).toBe(0.0);
  component.unmount();
});

// handleChange is called on change
it('should call handleChange on form change', () => {
  const handleChange = jest.spyOn(ReceiptInput.prototype, 'handleChange');
  const component = mount(<ReceiptInput onChange={ handleChange }/>);
  
  component.find('#text').simulate('change');
  expect(handleChange).toBeCalled();
  component.find('#price').simulate('change');
  expect(handleChange).toBeCalled();
  component.unmount();
});

// handleChange updates state
it('should change state when handleChange is called', () => {
  const component = mount(<ReceiptInput />);
  component.setState({ text: "", price: 0 });
  expect(component.state('text')).toBe('');
  expect(component.state('price')).toBe(0.0);

  const textEvent = {target: { name: "text", value: "bread" }};
  const priceEvent = {target: { name: "price", value: 5.6 }};

  component.find('#text').simulate('change', textEvent);
  expect(component.state('text')).toBe('bread');
  component.find('#price').simulate('change', priceEvent);
  expect(component.state('price')).toBe(5.6);
  component.unmount();
});
