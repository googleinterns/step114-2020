import React from 'react';

import Card from 'react-bootstrap/Card';

const Receipt = (props) => {
  console.log(props);
  return (
    <Card className='text-center'>
      <Card.Header>Receipt Name: {props.name}</Card.Header>
      <Card.Link href={props.fileUrl}>View Receipt Image</Card.Link>
      <Card.Body>
        <Card.Text>
          Store Name: {props.storeName}
        </Card.Text>
      </Card.Body>
      <Card.Footer>Total Price: {props.totalPrice}</Card.Footer>
    </Card>
  );
}

const Item = (props) => {
  console.log(props);
  return (
    <Card className='text-center'>
      <Card.Body>
        <Card.Text>
          Name: {props.name}
          Price: {props.price}
          Quantity: {props.quantity}
          Category: {props.category}
          Expire Date: {props.expireDate}
        </Card.Text>
      </Card.Body>
    </Card>
  );
}

class SearchResult extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      kind: 'unknown',
      receipts: [],
      items: []
    };
  };

  componentDidMount() {
    this.getEntity();
  }

  getEntity() {
    fetch('/search-entity')
        .then((response) => response.json())
        .then((entities) => {
          if (entities === undefined || entities.length == 0) {
            console.log('there is nothing');
          } else {
            // Item entity does not have items field.
            if (typeof entities[0].items === 'undefined') {
              this.setState({kind: 'item'});
              this.createItems(entities);
            } else {
              this.setState({kind: 'receipt'});
              this.createReceipts(entities);
            }
          }
        })
        .catch((error) => {
          console.log(error);
        });
  }

  createItems(itemEntity) {
    itemEntity.forEach((item) => {
      console.log(item);
      this.setState({
        items: [... this.state.items,
                      <Item
                        name={item.name}
                        price={item.price}
                        quantity={item.quantity}
                        category={item.category}
                        expireDate={item.expireDate}
                      />]
      });
    })
  }

  createReceipts(receiptsEntity) {
    receiptsEntity.forEach((receipt) => {
      console.log(receipt);
      this.setState({
        receipts: [...this.state.receipts,
                        <Receipt
                          name={receipt.name}
                          fileUrl={receipt.fileUrl}
                          storeName={receipt.storeName}
                          totalPrice={receipt.totalPrice} />]
      });
    });
  }

  render() {
    return (
      <>
        {this.state.kind === 'receipt' &&
          this.state.receipts
        }
        {this.state.kind === 'item' &&
          this.state.items
        }
      </>
    );
  }
}

export default SearchResult;
