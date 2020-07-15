import React from 'react';
import axios from 'axios';

export default class ReceiptHandler extends React.Component {

  constructor(props) {
    super(props);
    this.state = { items: [] };
    this.getReceiptData = this.getReceiptData.bind(this);
    this.handleChange = this.handleChange.bind(this);
    //this.handleSubmit = this.handleSubmit.bind(this);
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
    console.log(itemList);
    this.setState(state => ({
      items: itemList
    }));
    console.log(this.state.items);
  }

  handleChange(i, e, property) {
    let itemsList = [...this.state.items];
    itemsList[i].property = event.target.value;
    console.log(itemsList[i].property);
    this.setState({ items: itemsList });
  }
/** 
  handleSubmit(e) {
    
  }*/

  render() {
    return(
      <div className="container">
        <form /** onSubmit={this.handleSubmit}*/>
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
            <input type="text" defaultValue={item.name} onChange={this.handleChange.bind(this, i, "name")}/>
          </div>
          <div className="col auto">
            <input type="number" defaultValue={item.price} onChange={this.handleChange.bind(this, i, "price")}/>
          </div>
          <div className="col auto">
            <input type="number" defaultValue={item.quantity} onChange={this.handleChange.bind(this, i, "quantity")}/>
          </div>
        </div>
        ))}
        <button className="btn btn-primary"
              id="submit"
              type="submit" 
              value="Submit">Submit</button>
        </form>
      </div>
    );
  }

}
/** 
const List = (props) => {
  return (
    <form>
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
      {props.items.map((item, i) => (
        <div className="form-row" key={i}>
          <div className="col auto">
            <input type="text" defaultValue={item.name} value={this.state.items[i].name} onChange={this.handleChange.bind(this, i, name)}/>
          </div>
          <div className="col auto">
            <input type="number" defaultValue={item.price} value={this.state.items[i].price} onChange={this.handleChange.bind(this, i, price)}/>
          </div>
          <div className="col auto">
            <input type="number" defaultValue={item.quantity} value={this.state.items[i].quantity} onChange={this.handleChange.bind(this, i, quantity)}/>
          </div>
        </div>
      ))}
      <button className="btn btn-primary"
              id="submit"
              type="submit" 
              value="Submit">Submit</button>
    </form>
  );
}*/
