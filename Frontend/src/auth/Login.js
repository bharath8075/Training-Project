import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const navigate = useNavigate();

  function handleLogin() {
    if (!email || !password) {
      setError("All fields are required");
      return;
    }
    let user = {
      email: email,
      password: password,
    };
    axios
      .post("http://localhost:8080/recipebook/login", user)
      .then((res) => {
        let token = res.data.token;
        if (token) {
          localStorage.setItem("authToken", token);
        }
        setError("");
        navigate("/listingPage");
      })
      .catch((err) => {
        const { status, data } = err.response;

        if (status === 401) {
          setError(status);
        }
        if (data) {
          setError(data);
        }
      });
  }
  return (
    <div
      className="container d-flex justify-content-center align-items-center"
      n
      style={{ minHeight: "100vh" }}
    >
      <div className="card" style={{ width: "400px" }}>
        {error && (
          <div className="alert alert-danger text-center">
            <p>{error}</p>
          </div>
        )}
        <h1 className="card-title d-flex justify-content-center mt-4">Login</h1>
        <div className="card-body">
          <div className="form-group">
            <label>Email</label>
            <input
              className="form-control"
              type="email"
              value={email}
              onInput={(e) => setEmail(e.target.value)}
            />
            <label>Password</label>
            <input
              className="form-control"
              type="password"
              value={password}
              onInput={(e) => setPassword(e.target.value)}
            />
            &nbsp;
            <div className="d-grid">
              <button className="btn btn-secondary" onClick={handleLogin}>
                Login
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
