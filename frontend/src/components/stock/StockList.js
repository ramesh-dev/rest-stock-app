import React, { Component } from "react";

import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Table from 'react-bootstrap/Table'

import StockItem from "./StockItem";

export default class StockList extends Component {
  render() {
    return (
      <Row className="mt-3 justify-content-center">
        <Col md={5} lg={5}>
          <Table responsive="md" size="md" hover>
            <thead>
              <tr>
                <th>ID</th>
                <th>Stock Name</th>
                <th>Current Price</th>
              </tr>
            </thead>
            <tbody>
              {this.props.stocks.map(stock => (
                <StockItem key={stock.id} stock={stock} />
              ))}
            </tbody>
          </Table>
        </Col>
      </Row>
    );
  }
}
