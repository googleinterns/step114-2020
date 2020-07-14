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
    //const receipt = response.data;
    const itemList = response.items;
    console.log(itemList);
    this.state.items = itemList;
  }

  render() {
    return(
      <div>
        <List items={this.state.items} />
      </div>
    )
  }

}

const List = (props) => {
  return (
    <form>
      {props.items.map(item => (
        <div>
          <input type="text" defaultValue={item.name}/>
          <input type="number" defaultValue={item.price}/>
        </div>
      ))}
    </form>
  );
}