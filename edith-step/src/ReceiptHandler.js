import React from 'react';
import axios from 'axios';

export default class ReceiptHandler extends React.Component {

  constructor(props) {
    super(props);
    this.state = { userId: '', storeName: '', date: '', name: '', fileUrl: '', totalPrice: 0.0, items: [] };
    this.getReceiptData = this.getReceiptData.bind(this);
    this.handleItemChange = this.handleItemChange.bind(this);
    this.handleStoreChange = this.handleStoreChange.bind(this);
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
      userId: recepit.userId,
      storeName: receipt.storeName,
      date: receipt.date,
      name: receipt.name,
      fileUrl: receipt.fileUrl,
      totalPrice: receipt.totalPrice,
      items: itemList
    }));
  }

  handleItemChange(i, e, property) {
    let itemsList = [...this.state.items];
    itemsList[i].property = e.target.value;
    this.setState({ items: itemsList });
  }

  handleStoreChange(e) {
    this.setState({ storeName: e.target.value });
  }

  async handleSubmit(e) {
    e.preventDefault();
    let totalPrice;
    this.state.items.forEach(item => {
      totalPrice += item.price;
    })
    this.setState({ totalPrice: totalPrice });
    const receiptData = JSON.stringify(this.state);
    const response = await axios({
      method: 'post',
      url: '/receipt-deals',
      data: {
        userId: this.state.userId,
        storeName: this.state.storeName,
        date: this.state.date,
        name: this.state.name,
        fileUrl: this.state.fileUrl,
        totalPrice: this.state.price,
        items: receiptData
      }
    });
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
                defaultValue={item.name} 
                onChange={this.handleItemChange.bind(this, i, "name")}/>
          </div>
          <div className="col auto">
            <input type="number" 
                className="price"
                defaultValue={item.price} 
                onChange={this.handleItemChange.bind(this, i, "price")}/>
          </div>
          <div className="col auto">
            <input type="number" 
                className="quantity"
                defaultValue={item.quantity} 
                onChange={this.handleItemChange.bind(this, i, "quantity")}/>
          </div>
        </div>
        ))}
        <div className="form-row">
          <div className="col-auto">
            <input type="text"
                className="store-name"
                placeholder="Store"
                onChange={this.handleStoreChange}/>
          </div>
        </div>
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
