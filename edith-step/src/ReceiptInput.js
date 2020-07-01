import React from 'react';

export default class ReceiptInput extends React.Component {
  constructor(props) {
    super(props);
    this.state = {items: [], itemName: '', itemPrice: 0.0};
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
      id: Date.now()
    };

    const params = new URLSearchParams();
    params.append('itemName', this.state.itemName);
    params.append('itemPrice', this.state.itemPrice);

    const request = {
      method: 'post',
      headers: {'Content-Type': 'application/json'},
      body: params
    }
    fetch('/receipt', request);

    this.setState(state => ({
      items: state.items.concat(newItem),
      itemName: '',
      itemPrice: 0.0,
    }));
  }

  handleChange(e) {
    const value = e.target.value;
    this.setState({
      [e.target.name]: value
    });
  }

  finishReceipt(e) {
    //send get request to indicate receipt is finished
    e.preventDefault();
    fetch('/deals');
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
                id="name"
                value={this.state.itemName} 
                onChange={this.handleChange} />
            </td>
            <td>
              <input
                type="number" 
                name="price"
                id="price"
                step="0.01"
                value={this.state.itemPrice} 
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
      <button
        type="button" 
        onClick={this.finishReceipt}>
        Finished</button>
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
          </tr>
        ))}
      </tbody>
    </table>
  );
}
