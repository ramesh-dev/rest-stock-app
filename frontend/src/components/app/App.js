import React, { Component } from "react";
import Container from 'react-bootstrap/Container'

import "./App.css";
import AddStock from "../stock/AddStock";
import StockList from "../stock/StockList";

import stockService from "../../services/StockService";

export default class App extends Component {
  constructor(props) {
    super(props);
    this.state = {
      stocks: []
    };

    this.setStocks = this.setStocks.bind(this)
  }

  componentDidMount() {
    stockService
      .getStocks()
      .then(stocks => this.setStocks(stocks))
      .catch(() => alert("error fetching stocks"));
  }

  setStocks(stocks){
    stocks.sort((s1, s2) => {
      return parseFloat(s2.currentPrice) - parseFloat(s1.currentPrice) ;
    });
    this.setState({
      stocks: stocks
    });
  }

  render() {
    return (
      <div className="app">
        <header className="app-header">
          <h4>Stock App</h4>
        </header>
        <Container>
          <AddStock addStock={(stock) => this.setStocks([stock, ...this.state.stocks])} />
          <StockList stocks={this.state.stocks} />
        </Container>
      </div>
    );
  }
}