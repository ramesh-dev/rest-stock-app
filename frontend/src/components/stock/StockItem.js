import React, { Component } from "react";

export default class StockItem extends Component {
  render() {
    const { id, name, currentPrice } = this.props.stock;
    return (
      <tr>
        <td>{id}</td>
        <td>{name}</td>
        <td>{currentPrice}</td>
      </tr>
    );
  }
}
