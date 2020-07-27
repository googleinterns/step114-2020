import React from 'react';
import axios from 'axios';

/**
 * Displays items from past receipts that have expired by the
 * time the user is ready for another shopping trip.
 */
export default class GroceryList extends React.Component {
  /**
   * Constructor.
   * @param {Props} props
   */
  constructor(props) {
    super(props);
    this.state = {items: []};
  }

  /** Retrieves expired items when component is mounted. */
  componentDidMount() {
    console.log('grocery list mount');
    this.getItemData();
  }

  /** Gets expired items from grocery-list-query servlet. */
  async getItemData() {
    const response = await axios({
      method: 'get',
      url: '/grocery-list-query',
      responseType: 'json',
    });
    const itemsList = response.data;
    console.log(itemsList);
    this.setState({items: itemsList});
  }

  /**
   * Displays a list of the grocery items to be purchased.
   * @return {html} html for grocery item list
   */
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
            {this.state.items.map((item, i) => (
              <div key={i} className="row">
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
