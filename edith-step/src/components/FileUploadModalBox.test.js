import React from 'react';

import FileUploadModalBox from './FileUploadModalBox';
import {enableFetchMocks} from 'jest-fetch-mock';
import {shallow} from 'enzyme';

import '../setupTests.js'

let component;

describe('FileUploadModalBox calls', () => {
  test('getFileUploadUrl method when mounted', () => {
    const getFileUploadUrl =
            jest.spyOn(FileUploadModalBox.prototype, 'getFileUploadUrl');
    enableFetchMocks();
    component = shallow(<FileUploadModalBox />);
    expect(getFileUploadUrl).toBeCalled();
    component.unmount();
  });
});

describe('FileUploadModalBox must', () => {
  beforeEach(() => {
    enableFetchMocks();
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
