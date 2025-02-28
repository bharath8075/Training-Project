import { useState } from "react";
import Navbar from "./Navbar";
import axios from "axios";
import chechAuth from "../auth/checkAuth";
import { useNavigate } from "react-router-dom";

function AddRecipe() {
  const [title, setTittle] = useState("");
  const [ingridients, setIngridients] = useState("");
  const [procedures, setProcedures] = useState("");
  const [about, setAbout] = useState("");
  const [difficulty, setDifficulty] = useState("");
  const [image, setImage] = useState(null);
  const [cookingTime, setCookingTime] = useState("");
  const [error, setError] = useState("");

  const navigate = useNavigate();

  function handleAdd() {
    let token = localStorage.getItem("authToken");
    const formData = new FormData();
    formData.append(
      "recipe",
      new Blob(
        [
          JSON.stringify({
            title,
            ingridients,
            procedures,
            about,
            difficulty,
            cookingTime,
          }),
        ],
        {
          type: "application/json",
        }
      )
    );
    formData.append("image", image);
    axios
      .post("http://localhost:8080/recipebook/add", formData, {
        headers: {
          Authorization: "Bearer " + token,
          "Content-Type": "multipart/form-data",
        },
      })
      .then((res) => {
        alert("Recipe added");
        console.log(formData);
        navigate("/listingPage");
      })
      .catch((err) => {
        console.error(err);
      });
  }

  return (
    <>
      <Navbar />
      <div className="container">
        <div className="card mt-2">
          <h1 className="card-title text-center mt-2">Add Recipe</h1>

          {error && (
            <div className="bg-danger">
              <p className="text-danger">{error}</p>
            </div>
          )}
          <div className="card-body">
            {/* First Row */}
            <div className="row p-2">
              <div className="col-md-6">
                <label className="form-label fw-bold">Title</label>
                <input
                  className="form-control shadow-sm"
                  type="text"
                  placeholder="Enter Recipe Title"
                  value={title}
                  onInput={(e) => setTittle(e.target.value)}
                />
              </div>
              <div className="col-md-6">
                <label className="form-label fw-bold">Difficulty</label>
                <div className="dropdown">
                  <button
                    className="btn btn-outline-secondary dropdown-toggle shadow-sm w-100"
                    data-bs-toggle="dropdown"
                  >
                    {difficulty ? difficulty : "Select"}
                  </button>
                  <ul className="dropdown-menu">
                    <li
                      className="dropdown-item"
                      // value={Easy}
                      onClick={() => setDifficulty("Easy")}
                    >
                      Easy
                    </li>
                    <li
                      className="dropdown-item"
                      // value={Medium}
                      onClick={() => setDifficulty("Medium")}
                    >
                      Medium
                    </li>
                    <li
                      className="dropdown-item"
                      // value={Hard}
                      onClick={() => setDifficulty("Hard")}
                    >
                      Hard
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>

          {/* Second Row */}
          <div className="row p-2">
            <div className="col-md-6">
              <div className="mb-3">
                <label className="form-label fw-bold">Ingredient</label>
                <input
                  className="form-control shadow-sm"
                  type="text"
                  placeholder="Use (,) after each ingridient"
                  value={ingridients}
                  onChange={(e) => setIngridients(e.target.value)}
                />
              </div>
            </div>
          </div>
        </div>

        <div className="col-md-6">
          <label className="form-label fw-bold">Duration</label>
          <input
            className="form-control shadow-sm"
            type="time"
            style={{ maxWidth: "150px" }}
            value={cookingTime}
            onInput={(e) => setCookingTime(e.target.value)}
          />
        </div>

        {/* Third Row */}
        <div className="row p-2">
          <div className="col-md-6">
            <label className="form-label fw-bold">Procedures</label>
            <textarea
              className="form-control shadow-sm"
              rows="3"
              placeholder="Use (,) after each step"
              value={procedures}
              onInput={(e) => setProcedures(e.target.value)}
            ></textarea>
          </div>

          <div className="col-md-6">
            <label className="form-label fw-bold">About the Recipe</label>
            <textarea
              className="form-control shadow-sm"
              rows="3"
              placeholder="Add a brief description"
              value={about}
              onInput={(e) => setAbout(e.target.value)}
            ></textarea>
          </div>
        </div>

        {/* Recipe Image */}
        <div className="mb-4">
          <label className="form-label fw-bold">Add Recipe's Image</label>
          <input
            className="form-control shadow-sm "
            type="file"
            onChange={(e) => setImage(e.target.files[0])}
          />
        </div>

        {/* Submit Button */}
        <div className="d-grid">
          <button
            className="btn btn-primary shadow-sm"
            style={{ borderRadius: "10px" }}
            onClick={handleAdd}
          >
            Add Recipe
          </button>
        </div>
      </div>
      {/* </div> */}
    </>
  );
}

export default chechAuth(AddRecipe);
