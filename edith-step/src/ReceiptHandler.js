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
      userId: data.userId,
      storeName: data.storeName,
      date: data.date,
      name: data.name,
      fileUrl: data.fileUrl,
      totalPrice: data.totalPrice,
      items: data.items,
      deals: data.deals
    }));
  }

  async handleExpirationSubmit(e) {
    e.preventDefault();
    let price = 0;
    this.state.items.forEach(item => {
      price += item.price * item.quantity;
    })
    this.setState({ totalPrice: price });
    const receiptData = JSON.stringify(this.state);
    const response = await axios({
      method: 'post',
      url: '/receipt-output',
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
  }

  render() {
    return(
      <div class="receipt-handler">
      <div className="container">
        {this.state.items.length > 0 &&
        <form onSubmit={this.handleSubmit}>
          <div className="form-row">
            <div className="col auto input-group-text">
              <span>Item</span>
            </div>
            <div className="col auto input-group-text">
              <span>Price</span>
            </div>
            <div className="col auto input-group-text">
              <span>Quantity</span>
            </div>
            <div className="col auto input-group-text">
              <span>Store</span>
            </div>
          </div>
        {this.state.items.map((item, i) => (
          <div className="form-row" key={i}>
            <div className="col auto">
              <input type="text" 
                className="name form-control"
                name="name"
                value={item.name} 
                onChange={this.handleNameChange.bind(this, i)}/>
            </div>
            <div className="col auto">
              <input type="number" 
                className="price form-control"
                name="price"
                value={item.price} 
                onChange={this.handlePriceChange.bind(this, i)}/>
            </div>
            <div className="col auto">
              <input type="number" 
                className="quantity form-control"
                name="quantity"
                value={item.quantity} 
                onChange={this.handleQuantityChange.bind(this, i)}/>
            </div>
            {i==0 && 
            <div className="col-auto">
              <input type="text"
                className="store-name form-control"
                name="store-name"
                placeholder="Store"
                onChange={this.handleStoreChange}/>
             </div>
            }
          </div>
        ))}
        <button className="btn btn-primary"
            id="add"
            type="button"
            value="Add item"
            onClick={this.addItem}>Add Item</button>
        <button className="btn btn-primary"
              id="submit"
              type="submit" 
              value="Submit">Submit</button>
        </form>
        }
      </div>

      <div class="deals-container">
        <form onSubmit={this.expirationSubmit}>
        {this.state.deals.map((deal, i) => (
          <div className="form-row" key={i}>
            <div className="col auto">
              <span>{deal.store}</span>
            </div>
            <div className="col auto">
              <input type="number" 
                  className="item-expiration form-control"
                  name="expiration"
                  value={deal.expiration} 
                  onChange={this.handleExpirationChange.bind(this, i)}/>
            </div>
          </div>))}

          <button className="btn btn-primary"
              id="submit"
              type="submit" 
              value="Submit">Submit</button>
        </form>
      </div>
      </div>
    );
  }
}
