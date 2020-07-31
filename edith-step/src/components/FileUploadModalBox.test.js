import {enableFetchMocks} from 'jest-fetch-mock';
enableFetchMocks();

import React from 'react';

import FileUploadModalBox from './FileUploadModalBox';
import {shallow} from 'enzyme';

import '../setupTests.js';

let component;

describe('FileUploadModalBox calls', () => {
  test('getFileUploadUrl method when mounted', () => {
    const getFileUploadUrl =
            jest.spyOn(FileUploadModalBox.prototype, 'getFileUploadUrl');
    component = shallow(<FileUploadModalBox />);
    expect(getFileUploadUrl).toBeCalled();
    component.unmount();
  });
});

describe('FileUploadModalBox must', () => {
  beforeEach(() => {
    component = shallow(<FileUploadModalBox />);
  });

  afterEach(() => {
    component.unmount();
  });

  it('have input type file to upload file', () => {
    const fileUpload = component.find('.receipt-file');
    expect(fileUpload.exists()).toBe(true);
  });
});
