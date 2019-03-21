import axios from 'axios'

export class StockService {
  constructor() {
    this.baseUrl = 'http://localhost:3000/api/stocks'
  }

  getStocks() {
    return new Promise((resolve, reject) => {
      axios.get(this.baseUrl).then((res) => {
        resolve(res.data)
      }).catch(reject)
    })
  }

  addStock(stock) {
    return new Promise((resolve, reject) => {
      axios.post(this.baseUrl, stock)
      .then((res) => {
        resolve(res.data)
      }).catch(reject)
    })
  }
}

export default new StockService()