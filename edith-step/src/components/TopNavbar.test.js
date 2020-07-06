import React from 'react';
import Enzyme, {shallow, mount} from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';

import TopNavbar from './TopNavbar';

Enzyme.configure({ adapter: new Adapter() });

describe('TopNavbar component', () => {
    // Checks if the Navigation Bar is rendered correctly.
  test('renders', () => {
    const wrapper = shallow(<TopNavbar />);

    expect(wrapper.exists()).toBe(true);
  });
});
