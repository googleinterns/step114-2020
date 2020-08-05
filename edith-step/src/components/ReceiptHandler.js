import React from 'react';
import axios from 'axios';

/**
 * Displays receipt data from the document ai scan so that the
 * user can edit it and submit a more accurate copy of it.
 */
export default class ReceiptHandler extends React.Component {
  /**
   * Constructor
   * @param {Props} props Setup state.
   */
  constructor(props) {
    super(props);
    this.state = {userId: '', storeName: '', date: '',
      name: '', fileUrl: '', totalPrice: 0.0, items: [], deals: []};
    /**
     * Handles changes to the grocery store of the trip.
     * @param {Event} e change event
     */
    this.handleStoreChange = (e) => {
      this.setState({storeName: e.target.value});
    };

    /**
     * Adds a form row for the user to input additional grocery items
     * that weren't included in the initial scan.
     * @param {Event} e change event
     */
    this.addItem = (e) => {
      const newItem = {
        userId: this.state.userId,
        name: '',
        price: 0.0,
        quantity: 0,
        category: '',
        expiration: '',
      };
      this.setState({items: this.state.items.concat(newItem)});
    };

    /**
     * Handles primary submission to the form. Sends the grocery
     * data to the receipt-data servlet to find deals and expiration
     * dates.
     *
     * @param {Event} e change event
     */
    this.handleSubmit = async (e) => {
      e.preventDefault();
      let price = 0;
      this.state.items.forEach((item) => {
        price += item.price * item.quantity;
      });
      this.setState({totalPrice: price});
      const response = await axios({
        method: 'post',
        url: '/receipt-data',
        data: {
          userId: this.state.userId,
          storeName: this.state.storeName,
          date: this.state.date,
          name: this.state.name,
          fileUrl: this.state.fileUrl,
          totalPrice: price,
          items: this.state.items,
        },
      });

      const data = response.data;
      this.setState((state) => ({
        deals: data,
      }));
    };

    /**
     * Handles secondary submission to the form. Sends the data
     * to the store-receipt servlet to be stored in Datastore.
     * @param {Event} e change event
     */
    this.addExpirationAndSubmit = async (e) => {
      let price = 0;
      this.state.items.forEach((item) => {
        price += item.price * item.quantity;
      });
      this.setState({totalPrice: price});
      await axios({
        method: 'post',
        url: '/store-receipt',
        data: {
          userId: this.state.userId,
          storeName: this.state.storeName,
          date: this.state.date,
          name: this.state.name,
          fileUrl: this.state.fileUrl,
          totalPrice: price,
          items: this.state.items,
          deals: this.state.deals,
        },
      });
      axios({
        method: 'post',
        url: '/user-stats-servlet',
        data: {
          itemName: this.state.itemName,
          itemCategory: this.state.itemCategory,
          itemPrice: this.state.itemPrice,
          itemQuantity: this.state.itemQuantity,
          itemDate: this.getDate(),
          itemReceiptId: this.state.itemReceiptId,
          itemCategory: this.state.itemCategory,
        },
      }).catch((err) => {
        console.log(err);
      });
      this.setState((state) => ({
        userId: '',
        storeName: '',
        date: '',
        name: '',
        fileUrl: '',
        totalPrice: 0.0,
        items: [],
        deals: [],
        hidden: true,
      }));
    };
  }

  /** Calls function to get receipt data on mount. */
  async componentDidMount() {
    const response = await axios({
      method: 'get',
      url: '/receipt-file-handler',
      responseType: 'json',
    });
    const receipt = response.data;
    const itemList = receipt.items;
    this.setState((state) => ({
      userId: receipt.userId,
      storeName: receipt.storeName,
      date: receipt.date,
      name: receipt.name,
      fileUrl: receipt.fileUrl,
      totalPrice: receipt.totalPrice,
      items: itemList,
      deals: [],
    }));
  }

  /**
   * Handles changes to the name of a grocery item.
   * @param {number} i index of item
   * @param {Event} e change event
   */
  handleNameChange(i, e) {
    const itemsList = [...this.state.items];
    itemsList[i].name = e.target.value;
    this.setState({items: itemsList});
  }

  /**
   * Handles changes to the price of a grocery item.
   * @param {number} i index of item
   * @param {Event} e change event
   */
  handlePriceChange(i, e) {
    const itemsList = [...this.state.items];
    itemsList[i].price = e.target.value;
    this.setState({items: itemsList});
  }

  /**
   * Handles changes to the quantity of a grocery item.
   * @param {number} i index of item
   * @param {Event} e change event
   */
  handleQuantityChange(i, e) {
    const itemsList = [...this.state.items];
    itemsList[i].quantity = e.target.value;
    this.setState({items: itemsList});
  }

  /**
   * Handles changes to the expiration of a grocery item.
   * @param {number} i index of item
   * @param {Event} e change event
   */
  handleExpirationChange(i, e) {
    const dealsList = [...this.state.deals];
    dealsList[i].expirationTime = e.target.value;
    this.setState({deals: dealList});
  }

  /**
   * Adds a form row for the user to input additional grocery items
   * that weren't included in the initial scan.
   * @param {Event} e change event
   */
  addItem(e) {
    const newItem = {
      userId: this.state.userId,
      name: '',
      price: 0.0,
      quantity: 0,
      category: '',
      expireDate: '',
    };
    this.setState({items: this.state.items.concat(newItem)});
  }

  /**
   * Calculates the current date in yyyy-mm-dd format
   * @return {string} The current date
   */
  getDate() {
    const date = new Date(Date.now());
    let month = date.getMonth() + 1;
    month < 10 ? month = '0' + month.toString() : month = month.toString();
    let day = date.getDate();
    day < 10 ? day = '0' + day.toString() : day = day.toString();
    const year = date.getFullYear();
    return [year, month, day].join('-');
  }

  /**
   * Render component.
   * @return {html} grocery data form
   */
  render() {
    return (
      <>
        {this.state.hidden == false &&
        <div className="container col-lg-8">
          {this.state.items.length > 0 &&
          <form onSubmit={this.handleSubmit}>
            <div className="form-row">
              <div className="col-lg-2 input-group-text">
                <span>Item</span>
              </div>
              <div className="col-lg-2 input-group-text">
                <span>Price</span>
              </div>
              <div className="col-lg-2 input-group-text">
                <span>Quantity</span>
              </div>
              {this.state.deals.length == 0 &&
              <div className="col-lg-2 input-group-text">
                <span>Store</span>
              </div>
              }
              {this.state.deals.length > 0 &&
            <>
              <div className="col-lg-2 input-group-text">
                <span>Cheapest Place to Buy</span>
              </div>
              <div className="col-lg-2 input-group-text">
                <span>Expiration</span>
              </div>
            </>
              }
            </div>
            {this.state.items.map((item, i) => (
              <div className="form-row" key={i}>
                <div className="col-lg-2">
                  <input type="text"
                    className="name form-control"
                    name="name"
                    value={item.name}
                    onChange={this.handleNameChange.bind(this, i)}/>
                </div>
                <div className="col-lg-2">
                  <input type="number"
                    className="price form-control"
                    name="price"
                    value={item.price}
                    onChange={this.handlePriceChange.bind(this, i)}/>
                </div>
                <div className="col-lg-2">
                  <input type="number"
                    className="quantity form-control"
                    name="quantity"
                    value={item.quantity}
                    onChange={this.handleQuantityChange.bind(this, i)}/>
                </div>
                {i==0 && this.state.deals.length == 0 &&
                <div className="col-lg-2">
                  <input type="text"
                    className="store-name form-control"
                    name="store-name"
                    placeholder="Store"
                    onChange={this.handleStoreChange}/>
                </div>
                }
                {this.state.deals.length > 0 &&
              <>
                <div className="col-lg-2 item-deal">
                  <span>{this.state.deals[i].storeName}</span>
                </div>
                <div className="col-lg-2">
                  <input type="text"
                    className="item-expiration form-control"
                    name="expiration"
                    value={this.state.deals[i].expirationTime}
                    onChange={this.handleExpirationChange.bind(this, i)}/>
                </div>
              </>
                }
              </div>
            ))}
            {this.state.deals.length == 0 &&
        <div className="buttons">
          <button className="btn btn-primary"
            id="add"
            type="button"
            value="Add item"
            onClick={this.addItem}>Add Item</button>
          <button className="btn btn-primary"
            id="submit"
            type="submit"
            value="Submit">Next</button>
        </div>}
            {this.state.deals.length > 0 &&
          <button className="btn btn-primary expiration-submit"
            onClick={this.addExpirationAndSubmit}
            value="Submit">Submit</button>
            }
          </form>
          }
        </div>
        }
      </>
    );
  }
}
