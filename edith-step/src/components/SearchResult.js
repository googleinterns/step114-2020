import React from 'react';

import Card from 'react-bootstrap/Card';

const Receipt = (props) => {
  console.log(props);
  return (
    <Card className="text-center">
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

class SearchResult extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showingItem: []
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
            console.log("there is nothing");
          } else {
            // Item entity does not have items field.
            if (typeof entities[0].items === 'undefined') {
              console.log("hey I am Item");
            } else {
              // console.log('hey i am here');
              // console.log(entities);
              this.createReceipts(entities);
            }
          }
        })
        .catch((error) => {
          console.log(error);
        });
  }

  createReceipts(receiptsEntity) {
    receiptsEntity.forEach((receipt) => {
      console.log(receipt);
      this.setState({
        showingItem: [...this.state.showingItem,
                        <Receipt
                          name={receipt.name}
                          fileUrl={receipt.fileUrl}
                          storeName={receipt.storeName}
                          totalPrice={receipt.totalPrice} />]
      })

      // this.setState(prevState => ({
      //   showingItem: [...prevState.showingItem, <Receipt receipt />]
      // }))
    });
  }

  render() {
    return (
      <>
        {this.state.showingItem}
      </>
    );
  }
}

export default SearchResult;
