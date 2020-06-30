import React, { Component } from 'react';

export default class ReceiptInput extends Component {
  constructor(props) {
    super(props);
    this.state = {items: [], text: '', price: 0.0};
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  handleSubmit() {
    if (this.state.text.length === 0) {
      return;
    }
    const newItem = {
      id: Date.now(),
      text: this.state.text,
      price: this.state.price
    };
    this.setState(state => ({
      items: state.items.concat(newItem),
      text: '',
      price: 0.0,
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
                name="text"
                id="text"
                value={this.state.text} 
                onChange={this.handleChange} />
            </td>
            <td>
              <input
                type="number" 
                name="price"
                id="price"
                step="0.01"
                value={this.state.price} 
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

class GroceryList extends Component {
  render() {
    return (
    <table>
    <tbody>
      {this.props.items.map(item => (
        <tr key={item.id}>
          <td>{item.text}</td>
          <td>{item.price}</td>
        </tr>
      ))}
    </tbody>
    </table>
    );
  }
}
