import React from 'react';
import axios from 'axios';

export default class GroceryList extends React.Component {
  constructor(props) {
    super(props);
    this.state = { items: [] };
  }

  componentDidMount() {
    this.getItemData();
  }

  async getItemData() {
    const response = await axios({
      method: 'get',
      url: '/grocery-list-query',
      responseType: 'json'
    });
    const itemsList = response.data;
    this.setState({ items: itemsList });
  }

  render() {
    return (
      <div className="container">
        {this.state.items.length > 0 &&
        <>
        <div className="row">
          <div className="col-lg-2 item-header">
            <span>Item</span>
          </div>
        </div>
        <>
        {this.state.items.map((item) => (
        <div className="row">
          <div className="col-lg-2 item-name">
            <span>{item.name}</span>
          </div>
        </div>
        ))}
        </>
        </>
        }
      </div>
    );
  }
}
