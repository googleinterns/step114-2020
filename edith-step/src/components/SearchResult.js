import React from 'react';

import Card from 'react-bootstrap/Card';
import PropTypes from 'prop-types';

/**
 * Component that renders information of receipts found from search result.
 * @param {Object}  props for React component.
 * @return {React.ReactNode} React virtual DOM.
 */
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
};

/**
 * Component that renders information of items found from search result.
 * @param {Object}  props for React component.
 * @return {React.ReactNode} React virtual DOM.
 */
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
};

/**
 * Gets the search results and displays the result based on the result type.
 */
class SearchResult extends React.Component {
  /**
   * @constructor
   * @param {Object}  props for React component.
   */
  constructor(props) {
    super(props);
    this.state = {
      kind: 'unknown',
      receipts: [],
      items: [],
    };
  };

  /**
   * After the component did mount, get entities found from the search result.
   */
  componentDidMount() {
    this.getEntity();
  }

  /**
   * Calls search-entity endpoint in backend and collects the search result
   * and updates the component state accordingly.
   */
  getEntity() {
    fetch('/search-entity')
        .then((response) => response.json())
        .then((entities) => {
          if (entities === undefined || entities.length == 0) {
            console.error('no result found');
          } else {
            // Item entity does not have items field.
            if (typeof entities[0].items === 'undefined') {
              this.setState({kind: 'item'});
              this.setState({items: entities});
            } else {
              this.setState({kind: 'receipt'});
              this.setState({receipts: entities});
            }
          }
        })
        .catch((error) => {
          console.error(error);
        });
  }

  /**
   * Creates different item components based on the number of search result.
   * @param {Array}  itemEntity array of json parsed item.
   * @return {Array} array of item components.
   */
  createItems(itemEntity) {
    const itemElements = [];
    itemEntity.forEach((item) => {
      itemElements.push(
          <Item
            key={`${item.name} ${item.price}`}
            name={item.name}
            price={item.price}
            quantity={item.quantity}
            category={item.category}
            expireDate={item.expireDate}
          />);
    });
    return itemElements;
  }

  /**
   * Creates different receipt components based on the number of search result.
   * @param {Array}  receiptsEntity array of json parsed receipt.
   * @return {Array} array of receipt components.
   */
  createReceipts(receiptsEntity) {
    const receiptElements = [];
    receiptsEntity.forEach((receipt) => {
      receiptElements.push(
          <Receipt
            // files stored in the same GCS bucket will have different url.
            key={receipt.fileUrl}
            name={receipt.name}
            fileUrl={receipt.fileUrl}
            storeName={receipt.storeName}
            totalPrice={receipt.totalPrice}
          />);
    });
    return receiptElements;
  }

  /**
   * Renders list of search result.
   *  @return { React.ReactNode } React virtual DOM.
   */
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

Receipt.propTypes = {
  name: PropTypes.string,
  storeName: PropTypes.string,
  fileUrl: PropTypes.string,
  totalPrice: PropTypes.number,
};

Item.propTypes = {
  name: PropTypes.string,
  price: PropTypes.number,
  quantity: PropTypes.number,
  fileUrl: PropTypes.string,
  category: PropTypes.string,
  expireDate: PropTypes.string,
};

export default SearchResult;
