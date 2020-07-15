import React from 'react';
import axios from 'axios';

export default class ReceiptHandler extends React.Component {

  constructor(props) {
    super(props);
    this.state = { items: [] };
    this.getReceiptData = this.getReceiptData.bind(this);
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

  render() {
    return(
      <div className="receipt-list">
        {this.state.items.length > 0 &&
          <List items={this.state.items} />
        }
      </div>
    );
  }

}

const List = (props) => {
  return (
    <form>
      {props.items.map(item => (
        <div className="form-row">
          <div className="col auto">
            <input type="text" defaultValue={item.name}/>
          </div>
          <div className="col auto">
            <input type="number" defaultValue={item.price}/>
          </div>
          <div className="col auto">
            <input type="number" defaultValue={item.quantity}/>
          </div>
        </div>
      ))}
    </form>
  );
}
