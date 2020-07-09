import React from 'react';

export default class ReceiptInput extends React.Component {
  constructor(props) {
    super(props);
    this.state = {items: [], itemName: '', itemPrice: 0.0, itemQuantity: 1};
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit(e) {
    e.preventDefault();

    if (this.state.itemName.length === 0) {
      return;
    }
    const newItem = {
      itemName: this.state.itemName,
      itemPrice: this.state.itemPrice,
      itemQuantity: this.state.itemQuantity,
      id: Date.now()
    };
    
    const axios = require('axios')
    axios({
      method: 'post',
      url: '/receipt-deals',
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
      <div class="container-fluid">
      <h3>Grocery Items</h3>
      <form onSubmit={this.handleSubmit}>
        <div class="form-row">
          <div class="col-auto">
            <div class="input-group mb-2">
              <div class="input-group-prepend">
                <div class="input-group-text">Item</div>
              </div>
              <input 
                type="text" 
                class="form-control"
                name="itemName"
                id="name"
                value={this.state.itemName} 
                onChange={this.handleChange} />
            </div>
          </div>
          <div class="col-auto">
            <div class="input-group mb-2">
              <div class="input-group-prepend">
                <div class="input-group-text">Price</div>
              </div>
              <input
                type="number" 
                class="form-control"
                name="itemPrice"
                id="price"
                step="0.01"
                value={this.state.itemPrice} 
                onChange={this.handleChange} />
            </div>
          </div>
          <div class="col-auto">
            <div class="input-group mb-2">
              <div class="input-group-prepend">
                <div class="input-group-text">Quantity</div>
              </div>
              <input
                type="number"
                class="form-control"
                name="itemQuantity"
                id="quantity"
                step="1"
                value={this.state.itemQuantity}
                onChange={this.handleChange} />
            </div>
          </div>
          <div class="col-auto">
            <button class="btn btn-primary"
                id="submit"
                type="submit" 
                value="Submit">Add Item</button>
          </div>
        </div>
      </form>
      {this.state.items.length > 0 &&
        <GroceryList items={this.state.items} />
      }
      </div>
    );
  }
}

var GroceryList = (props) => {
  return (
    <div id="grocery-list">
      <ul class="list-group col-lg-3">
        <li class="h-50 list-group-item d-flex justify-content-between align-items-center">
            <span class="col-lg-1">Item</span>
            <span class="badge badge-pill col-lg-1">Price</span>
            <span class="badge badge-pill col-lg-1">#</span>
          </li>
        {props.items.map(item => (
          <li class="h-50 list-group-item d-flex justify-content-between align-items-center" key={item.id}>
            <span class="item-name col-lg-1">{item.itemName}</span>
            <span class="item-price badge badge-pill col-lg-1">{item.itemPrice}</span>
            <span class="item-quantity badge badge-pill col-lg-1">{item.itemQuantity}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}
