import React from 'react';
import {enableFetchMocks} from 'jest-fetch-mock';
enableFetchMocks();
import FileUploadModalBox from './FileUploadModalBox';

import {shallow} from 'enzyme';

import '../setupTests.js';

let component;
const url = 'aUrl';

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
    fetch.resetMocks();
    fetch.mockResponse(JSON.stringify(url));
    component = shallow(<FileUploadModalBox />);
  });

  afterEach(() => {
    component.unmount();
  });

  it('changes the state of the url', () => {
    expect(component.state('uploadUrl')).toBe('\"aUrl\"');
  });

  it('have input type file to upload file', () => {
    const fileUpload = component.find('.receipt-file');
    expect(fileUpload.exists()).toBe(true);
  });
});
