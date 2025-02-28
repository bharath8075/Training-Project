import axios from "axios";
import { useState } from "react";
import chechAuth from "./checkAuth";

function PasswordSet() {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confPass, setConfPass] = useState("");
  const [error, setError] = useState("");

  const handleSave = () => {
    let token = localStorage.getItem("authToken");

    if (!currentPassword || !newPassword || !confPass) {
      setError("All fields are required!");
      return;
    }

    if (newPassword !== confPass) {
      setError("Password does not Match");
      return;
    }

    axios
      .put(
        "http://localhost:8080/recipebook/change_password",
        {
          newPassword: newPassword,
          currentPassword: currentPassword,
        },
        {
          headers: { Authorization: `Bearer ` + token },
        }
      )
      .then((res) => {
        alert(res.data);
        // console.log("Success");
      })
      .catch((err) => {
        console.log(error);
        console.log(err);
      });
  };

  return (
    <div
      className="container d-flex justify-content-center align-items-center"
      n
      style={{ minHeight: "100vh" }}
    >
      <div className="card" style={{ width: "400px" }}>
        <h1 className="card-title d-flex justify-content-center mt-4">
          Change Password
        </h1>
        <div className="card-body">
          {error && (
            <div className="alert alert-danger">
              <p>{error}</p>
            </div>
          )}

          <div className="form-group">
            <label>Current Password</label>
            <input
              className="form-control"
              type="password"
              value={currentPassword}
              onInput={(e) => setCurrentPassword(e.target.value)}
            />
            <label className="mt-3">New Password</label>
            <input
              className="form-control"
              type="password"
              value={newPassword}
              onInput={(e) => setNewPassword(e.target.value)}
            />
            <label className="mt-3">Confirm Password</label>
            <input
              className="form-control"
              type="password"
              value={confPass}
              onInput={(e) => setConfPass(e.target.value)}
            />
            &nbsp;
            <div className="d-grid">
              <button className="btn btn-secondary" onClick={handleSave}>
                Save
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default chechAuth(PasswordSet);
