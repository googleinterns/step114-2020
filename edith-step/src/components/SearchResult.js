import React from 'react';

import Card from 'react-bootstrap/Card';

const Receipt = (props) => {
  return (
    <Card
      className='text-center'
    >
      <Card.Title>
        Receipt Name: {props.name}
      </Card.Title>
      <Card.Subtitle
        className="mb-2 text-muted">
        Store Name: {props.storeName}
      </Card.Subtitle>
      <Card.Link
        href={props.fileUrl}>
        View Receipt Image
      </Card.Link>
      <Card.Body>
        <Card.Text>
         Total Price: {props.totalPrice}
        </Card.Text>
      </Card.Body>
    </Card>
  );
}

const Item = (props) => {
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
              this.setState({items: entities})
            } else {
              this.setState({kind: 'receipt'});
              this.setState({receipts: entities});
            }
          }
        })
        .catch((error) => {
          console.log(error);
        });
  }

  createItems(itemEntity) {
    let itemElements = [];
    itemEntity.forEach((item) => {
      itemElements.push(itemElements,
                    <Item
                      name={item.name}
                      price={item.price}
                      quantity={item.quantity}
                      category={item.category}
                      expireDate={item.expireDate}
                    />);
    })
    return itemElements;
  }

  createReceipts(receiptsEntity) {
    let receiptElements = [];
    receiptsEntity.forEach((receipt) => {
      receiptElements.push(
                      <Receipt
                        name={receipt.name}
                        fileUrl={receipt.fileUrl}
                        storeName={receipt.storeName}
                        totalPrice={receipt.totalPrice}
                      />);
    });
    return receiptElements;
  }

  render() {
    return (
      <>
        {this.state.kind === 'receipt' &&
          this.createReceipts(this.state.receipts)
        }
        {this.state.kind === 'item' &&
          this.createItems(this.state.items)
        }
      </>
    );
  }
}

export default SearchResult;
