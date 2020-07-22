import React from 'react';
import axios from 'axios';

export default class ReceiptHandler extends React.Component {

  constructor(props) {
    super(props);
    this.state = { userId: '', storeName: '', date: '', name: '', fileUrl: '', totalPrice: 0.0, items: [], deals: [] };
    this.getReceiptData = this.getReceiptData.bind(this);
    this.handleStoreChange = this.handleStoreChange.bind(this);
    this.addItem = this.addItem.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  componentDidMount() {
    this.getReceiptData();
  }

  async getReceiptData() {
    const response = await axios({
      method: 'get',
      url: '/receipt-file-handle',
      responseType: 'json'
    });
    const receipt = response.data;
    const itemList = receipt.items;
    this.setState(state => ({
      userId: receipt.userId,
      storeName: receipt.storeName,
      date: receipt.date,
      name: receipt.name,
      fileUrl: receipt.fileUrl,
      totalPrice: receipt.totalPrice,
      items: itemList,
      deals: []
    }));
  }

  handleNameChange(i, e) {
    let itemsList = [...this.state.items];
    itemsList[i].name = e.target.value;
    this.setState({ items: itemsList });
  }

  handlePriceChange(i, e) {
    let itemsList = [...this.state.items];
    itemsList[i].price = e.target.value;
    this.setState({ items: itemsList });
  }

  handleQuantityChange(i, e) {
    let itemsList = [...this.state.items];
    itemsList[i].quantity = e.target.value;
    this.setState({ items: itemsList });
  }

  handleStoreChange(e) {
    this.setState({ storeName: e.target.value });
  }

  handleExpirationChange(i, e) {
    let dealsList = [...this.state.deals];
    dealsList[i].expiration = e.target.value;
    this.setState({ deals : dealList });
  }

  addItem(e) {
    const newItem = {
      userId: this.state.userId,
      name: '',
      price: 0.0,
      quantity: 0,
      category: '',
      expireDate: ''
    }
    this.setState({ items: this.state.items.concat(newItem) });
  }

  async handleSubmit(e) {
    e.preventDefault();
    let price = 0;
    this.state.items.forEach(item => {
      price += item.price * item.quantity;
    })
    this.setState({ totalPrice: price });
    const receiptData = JSON.stringify(this.state);
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
      }
    });
    const data = response.data;
    this.setState(state => ({
      deals: data
    }));
  }

  async handleExpirationSubmit(e) {
    let price = 0;
    this.state.items.forEach(item => {
      price += item.price * item.quantity;
    })
    this.setState({ totalPrice: price });
    const response = await axios({
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
        deals: this.state.deals
      }
    });
    this.setState({ items: [], deals: [] });
    console.log(this.state);
  }

  render() {
    return(
      <div class="receipt-handler">
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
                <div className="col-lg-2">
                  <span>{this.state.deals[i].store}</span>
                </div>
                <div className="col-lg-2">
                  <input type="text" 
                    className="item-expiration form-control"
                    name="expiration"
                    value={this.state.deals[i].expiration} 
                    onChange={this.handleExpirationChange.bind(this, i)}/>
                </div>
              </>
            }
          </div>
        ))}
        {this.state.deals.length == 0 &&
        <div class="buttons">
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
          <button className="btn btn-primary"
              id="submit"
              onClick={this.handleExpirationSubmit}
              value="Submit">Submit</button>
        }
        </form>
        }
      </div>
      </div>
    );
  }
}
