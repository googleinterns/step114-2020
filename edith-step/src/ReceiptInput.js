import React from 'react';
import axios from 'axios';

export default class ReceiptInput extends React.Component {
  constructor(props) {
    super(props);
    this.state = {items: [], itemName: '', itemPrice: 0.0, itemQuantity: 1,
                  itemCategory: 'category', itemReceiptId: 'receiptId' };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
    this.getDate = this.getDate.bind(this);
  }

  handleSubmit(e) {
    e.preventDefault();

    if (this.state.itemName.length === 0) {
      return;
    }
  
    axios({
      method: 'post',
      url: '/user-stats-servlet',
      data: {
        itemName: this.state.itemName,
        itemCategory: this.state.itemCategory,
        itemPrice: this.state.itemPrice,
        itemQuantity: this.state.itemQuantity,
        itemDate: this.getDate(),
        itemReceiptId: this.state.itemReceiptId
      }
    }).catch((err) => {
      console.log(err);
    });

    const newItem = {
      itemName: this.state.itemName,
      itemPrice: this.state.itemPrice,
      itemQuantity: this.state.itemQuantity,
      id: Date.now()
    };
    
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
      <div>
      <h3>Grocery Items</h3>
      <GroceryList items={this.state.items} />
      <form onSubmit={this.handleSubmit}>
        <table>
        <tbody>
          <tr>
            <td>
              <input 
                type="text" 
                name="itemName"
                id="name"
                value={this.state.itemName} 
                onChange={this.handleChange} />
            </td>
            <td>
              <input
                type="number" 
                name="itemPrice"
                id="price"
                step="0.01"
                value={this.state.itemPrice} 
                onChange={this.handleChange} />
            </td>
            <td>
              <input
                type="number"
                name="itemQuantity"
                id="quantity"
                value={this.state.itemQuantity}
                onChange={this.handleChange} />
            </td>
          </tr>
          <tr>
            <td>
              <input
                id="submit"
                type="submit" 
                value="Submit" />
            </td>
          </tr>
        </tbody>
        </table>
      </form>
      </div>
    );
  }
}

var GroceryList = (props) => {
  return (
    <table id="grocery-list">
      <tbody>
        {props.items.map(item => (
          <tr className="item" key={item.id}>
            <td className="item-name">{item.itemName}</td>
            <td className="item-price">{item.itemPrice}</td>
            <td className="item-quantity">{item.itemQuantity}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
