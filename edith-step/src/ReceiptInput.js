import React from 'react';
import axios from 'axios';

export default class ReceiptInput extends React.Component {
  constructor(props) {
    super(props);
    this.state = {items: [], itemName: '', itemPrice: 0.0, itemQuantity: 1};
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  async getDeal(name, price, quantity) {
    const axios = require('axios')
    const response = await axios({
      method: 'post',
      url: '/receipt-deals',
      data: {
        itemName: name,
        itemPrice: price,
        itemQuantity: quantity
      }
    });
    const dealItem = response.data;

    let newDeal;
    if (dealItem == "no deal found") {
      newDeal = {
        storeName: "no deal found",
        storePrice: 0
      }
    } else {
      newDeal = {
        storeName: dealItem.store,
        storePrice: dealItem.price
      };
    }

    return newDeal;
  }

  async handleSubmit(e) {
    e.preventDefault();
    if (this.state.itemName.length === 0) {
      return;
    }

    const newDeal = await this.getDeal(this.state.itemName, this.state.itemPrice, this.state.itemQuantity);
    let dealMessage;
    if (newDeal.storeName == "no deal found" || newDeal.storePrice > this.state.itemPrice) {
      dealMessage = "no deal found";
    } else {
      dealMessage = "Purchase at " + newDeal.storeName + " for $" + newDeal.storePrice + ".";
    }

    const newItem = {
      itemName: this.state.itemName,
      itemPrice: this.state.itemPrice,
      itemQuantity: this.state.itemQuantity,
      itemDeal: dealMessage,
      id: Date.now()
    };

    axios({
      method: 'post',
      url: '/receipt-data',
      data: {
        itemName: this.state.itemName,
        itemPrice: this.state.itemPrice,
        itemQuantity: this.state.itemQuantity
      }
    }).then((response) => {
      console.log(response);
    });

    this.setState(state => ({
      items: state.items.concat(newItem),
      itemName: '',
      itemPrice: 0.0,
      itemQuantity: 1
    }));
  }

  handleChange(e) {
    const value = e.target.value;
    this.setState({
      [e.target.name]: value
    });
  }

  render() {
    return(
      <div className="container-fluid">
      <h3>Grocery Items</h3>
      <form onSubmit={this.handleSubmit}>
        <div className="form-row">
          <div className="col-auto">
            <div className="input-group mb-2">
              <div className="input-group-prepend">
                <div className="input-group-text">Item</div>
              </div>
              <input 
                type="text" 
                className="form-control"
                name="itemName"
                id="name"
                value={this.state.itemName} 
                onChange={this.handleChange} />
            </div>
          </div>
          <div className="col-auto">
            <div className="input-group mb-2">
              <div className="input-group-prepend">
                <div className="input-group-text">Price</div>
              </div>
              <input
                type="number" 
                className="form-control"
                name="itemPrice"
                id="price"
                step="0.01"
                value={this.state.itemPrice} 
                onChange={this.handleChange} />
            </div>
          </div>
          <div className="col-auto">
            <div className="input-group mb-2">
              <div className="input-group-prepend">
                <div className="input-group-text">Quantity</div>
              </div>
              <input
                type="number"
                className="form-control"
                name="itemQuantity"
                id="quantity"
                step="1"
                value={this.state.itemQuantity}
                onChange={this.handleChange} />
            </div>
          </div>
          <div className="col-auto">
            <button className="btn btn-primary"
                id="submit"
                type="submit" 
                value="Submit">Add Item</button>
          </div>
        </div>
      </form>
      <div className="row">
        <div className="col-lg-5">
          {this.state.items.length > 0 &&
            <GroceryList items={this.state.items}/>
          }
        </div>
      </div>
      </div>
    );
  }
}

const GroceryList = (props) => {
  return (
    <div id="grocery-list">
      <ul className="list-group">
        <li className="h-50 list-group-item d-flex justify-content-between align-items-center">
            <span className="col-lg-2">Item</span>
            <span className="badge badge-pill col-lg-2">Price</span>
            <span className="badge badge-pill col-lg-2">#</span>
            <span className="badge badge-pill col-lg-2">Deal</span>
          </li>
        {props.items.map(item => (
          <li className="h-50 list-group-item d-flex justify-content-between align-items-center" key={item.id}>
            <span className="item-name col-lg-2">{item.itemName}</span>
            <span className="item-price badge badge-pill col-lg-2">{item.itemPrice}</span>
            <span className="item-quantity badge badge-pill col-lg-2">{item.itemQuantity}</span>
            <span className="item-deal badge badge-pill col-lg-2">{item.itemDeal}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
