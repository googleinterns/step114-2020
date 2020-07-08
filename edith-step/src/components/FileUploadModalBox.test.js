import React from 'react';
import { mount } from 'enzyme';
import FileUploadModalBox from './FileUploadModalBox';

import { enableFetchMocks } from 'jest-fetch-mock'
import '../setupTests.js'

describe('FileUploadModalBox calls', () => {
  test('getFileUploadUrl method when mounted', () => {
    const getFileUploadUrl = jest.spyOn(FileUploadModalBox.prototype, 'getFileUploadUrl');
    const component = mount(<FileUploadModalBox />);
    expect(getFileUploadUrl).toBeCalled();
  });
});
