import axios from "axios";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Signup() {
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confPass, setConfPass] = useState("");
  const [error, setError] = useState("");

  function handleSignup() {
    if (!name || !email || !password || !confPass) {
      setError("All fields are required");
      return;
    }

    if (password !== confPass) {
      setError("Password does not match");
      return;
    }

    var user = {
      name: name,
      email: email,
      password: password,
    };

    axios
      .post("http://localhost:8080/recipebook/register", user)
      .then((response) => {
        setError("");
        navigate("/login");
      })
      .catch((error) => {
        if (error.response.data.errors) {
          setError(Object.values(error.response.data.errors).join(" "));
          console.log(error.response.data);
        } else {
          setError("Soemthing went wrong");
        }
      });
  }
  return (
    <div className="container d-flex justify-content-center align-items-center vh-100">
      <div className="card " id="card" style={{ width: "400px" }}>
        {error && (
          <div>
            <p className="text-danger"> {error} </p>
          </div>
        )}
        <h1 className="card-title d-flex justify-content-center mt-4">
          Signup
        </h1>
        <div className="card-body">
          <div className="form-group">
            <label>Name</label>
            <input
              className="form-control"
              type="text"
              value={name}
              onInput={(e) => setName(e.target.value)}
            />
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
            <label>Confirm Password</label>
            <input
              className="form-control"
              type="password"
              value={confPass}
              onInput={(e) => setConfPass(e.target.value)}
            />
            &nbsp;
            <div className="d-grid">
              <button className="btn btn-secondary" onClick={handleSignup}>
                Signup
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Signup;
