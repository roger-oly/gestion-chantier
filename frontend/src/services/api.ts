import axios from "axios";

const api = axios.create({
  //baseURL: "http://localhost:8080/api",
  baseURL: "https://fluffy-computing-machine-xrw4qpjx949jh6p5j-8080.app.github.dev/api",
  headers: {
    "Content-Type": "application/json",
  },
});

export default api;