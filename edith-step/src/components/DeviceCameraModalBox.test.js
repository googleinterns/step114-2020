import React from 'react';
import {enableFetchMocks} from 'jest-fetch-mock';
enableFetchMocks();
import DeviceCameraModalBox from './DeviceCameraModalBox';
import {shallow} from 'enzyme';

import '../setupTests.js';

let component;
const url = 'aUrl';

describe('DeviceCameraModalBox calls', () => {
  test('getFileUploadUrl method when mounted', () => {
    const getFileUploadUrl =
            jest.spyOn(DeviceCameraModalBox.prototype, 'getFileUploadUrl');
    component = shallow(<DeviceCameraModalBox />);
    expect(getFileUploadUrl).toBeCalled();
    component.unmount();
  });
});

describe('DeviceCameraModalBox must', () => {
  beforeEach(() => {
    fetch.resetMocks();
    fetch.mockResponse(JSON.stringify(url));
    component = shallow(<DeviceCameraModalBox />);
  });

  afterEach(() => {
    component.unmount();
  });

  it('changes the state of the url', () => {
    expect(component.state('uploadUrl')).toBe('\"aUrl\"');
  });

});