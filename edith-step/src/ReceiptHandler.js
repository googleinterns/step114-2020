import React from 'react';
import axios from 'axios';

export default class ReceiptHandler extends React.Component {

  constructor(props) {
    super(props);
    this.state = { userId: '', storeName: '', date: '', name: '', fileUrl: '', totalPrice: 0.0, items: [] };
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
      url: '/receipt-file-handler',
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
      items: itemList
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
        data: receiptData
      }
    });
    const deals = response.data;
  }

  render() {
    return(
      <div className="container">
        {this.state.items.length > 0 &&
        <form onSubmit={this.handleSubmit}>
        <div className="form-row">
          <div className="col auto">
            <span>Item</span>
          </div>
          <div className="col auto">
            <span>Price</span>
          </div>
          <div className="col auto">
            <span>Quantity</span>
          </div>
        </div>
        {this.state.items.map((item, i) => (
        <div className="form-row" key={i}>
          <div className="col auto">
            <input type="text" 
                className="name"
                name="name"
                value={item.name} 
                onChange={this.handleNameChange.bind(this, i)}/>
          </div>
          <div className="col auto">
            <input type="number" 
                className="price"
                name="price"
                value={item.price} 
                onChange={this.handlePriceChange.bind(this, i)}/>
          </div>
          <div className="col auto">
            <input type="number" 
                className="quantity"
                name="quantity"
                value={item.quantity} 
                onChange={this.handleQuantityChange.bind(this, i)}/>
          </div>
        </div>
        ))}
        <div className="form-row">
          <div className="col-auto">
            <input type="text"
                className="store-name"
                name="store-name"
                placeholder="Store"
                onChange={this.handleStoreChange}/>
          </div>
        </div>
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
    );
  }
}
