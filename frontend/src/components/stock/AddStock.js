import React, { Component } from "react";
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'

import stockService from "../../services/StockService";

export default class AddStock extends Component {
  constructor(props) {
    super(props);
    this.state = {
      stock: {name: '', currentPrice:''}
    };

    this.onChange = this.onChange.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  onChange(prop, event){
    var stock = this.state.stock;
    stock[prop] = event.target.value;
    this.setState({ stock: stock });
  }

  onSubmit(e){
    e.preventDefault();

    const stock = this.state.stock;
    if (!stock.name) {
      alert("please enter valid stock name");
      return;
    }

    this.setState({ isLoading: true });

    stockService
      .addStock(this.state.stock)
      .then(stock => {
        this.props.addStock(stock);
        this.setState({ stock: {name: '', currentPrice: ''} });
        this.setState({ isLoading: false });
      })
      .catch(error => {
        console.error(error)
        this.setState({ isLoading: false });
        var resp = error.response.data || {};
        var msg = resp.errorDesc || "Error adding stock";
        alert(msg);
      });
  }

  render() {
    return (
      <Row className="mt-5 mb-4 justify-content-center">
        <Col md={5} lg={5}>
          <Form onSubmit={this.onSubmit}>
            <Form.Row>
              <Col>
                <Form.Control
                  type="text"
                  value={this.state.stock.name}
                  placeholder="Stock name"
                  onChange={e => {
                    this.onChange("name", e);
                  }}
                />
              </Col>
              <Col>
                <Form.Control
                  type="number"
                  value={this.state.stock.currentPrice}
                  placeholder="Current price" min="0"  step=".01"
                  onChange={e => {
                    this.onChange("currentPrice", e);
                  }}
                />
              </Col>
              <Col>
                <Button variant="primary" size="md" type="submit" disabled={this.state.isLoading}>
                  {this.state.isLoading ? 'Adding...' : 'Add Stock'}
                </Button>
              </Col>
            </Form.Row>
          </Form>
        </Col>
      </Row>
    );
  }
}
