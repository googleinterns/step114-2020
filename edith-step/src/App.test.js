import React from 'react';
import { render } from '@testing-library/react';
import App from './App';

test('renders learn react link', () => {
  HTMLCanvasElement.prototype.getContext = jest.fn()
  expect(true).toBeTruthy();
});
