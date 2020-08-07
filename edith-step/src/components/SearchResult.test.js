import {enableFetchMocks} from 'jest-fetch-mock';
enableFetchMocks();
import React from 'react';
import {mount} from 'enzyme';
import SearchResult from './SearchResult';
import '../setupTests.js';

let component;
const receipt = [
  {name: 'name', storeName: 'storeName',
    fileUrl: 'fileUrl', totalPrice: 'totalPrice', items: 'items'}];
const item = [
  {name: 'name', price: '5.5', quantity: '2',
    category: 'cate', expireDate: 'expireDate'}];

describe('SearchResult', () => {
  const getEntity = jest.spyOn(SearchResult.prototype, 'getEntity');

  beforeEach(() => {
    fetch.resetMocks();
    fetch.mockResponse(JSON.stringify(receipt));
    component = mount(<SearchResult />);
  });

  afterEach(() => {
    component.unmount();
  });

  test('renders', () => {
    expect(component.exists()).toBe(true);
  });

  test('calls getEntity when mounted', () => {
    expect(getEntity).toHaveBeenCalled();
  });
});

describe('When api call response is of receipt', () => {
  const createReceipts = jest.spyOn(SearchResult.prototype, 'createReceipts');

  beforeEach(() => {
    fetch.resetMocks();
    fetch.mockResponse(JSON.stringify(receipt));
    component = mount(<SearchResult />);
  });

  afterEach(() => {
    component.unmount();
  });

  test('createReceipts method is called', () => {
    expect(createReceipts).toHaveBeenCalled();
  });

  test('kind state is set to receipt', () => {
    expect(component.state('kind')).toBe('receipt');
  });

  test('receipts state is to be receipt object', () => {
    expect(component.state('receipts')).toStrictEqual(receipt);
    expect(component.state('items')).toStrictEqual([]);
  });
});

describe('When api call response is of item', () => {
  const createItems = jest.spyOn(SearchResult.prototype, 'createItems');

  beforeEach(() => {
    fetch.resetMocks();
    fetch.mockResponse(JSON.stringify(item));
    component = mount(<SearchResult />);
  });

  afterEach(() => {
    component.unmount();
  });

  test('createItems method is called', () => {
    expect(createItems).toHaveBeenCalled();
  });

  test('kind state is set to item', () => {
    expect(component.state('kind')).toBe('item');
  });

  test('items state is to be item object', () => {
    expect(component.state('receipts')).toStrictEqual([]);
    expect(component.state('items')).toStrictEqual(item);
  });
});
