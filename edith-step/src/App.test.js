import React from 'react';
import Enzyme, {shallow, mount} from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';

import App from './App';

Enzyme.configure({ adapter: new Adapter() });

describe('App component', () => {
  // Checks if the app is rendered.
  test('renders', () => {
    const app = shallow(<App />);

    expect(app.exists()).toBe(true);
  });
});
