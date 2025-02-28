import { useEffect, useState } from "react";
import Navbar from "./Navbar";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";
import chechAuth from "../auth/checkAuth";

function EditRecipe() {
  let { id } = useParams();
  const [title, setTitle] = useState("");
  const [about, setAbout] = useState("");
  const [ingridients, setIngridients] = useState("");
  const [procedures, setProcedures] = useState("");
  const [cookingTime, setCookingTime] = useState("");
  const [difficulty, setDifficulty] = useState("");
  const [image, setImage] = useState(null);

  const [error, setError] = useState("");
  let navigate = useNavigate();

  let token = localStorage.getItem("authToken");

  useEffect(() => {
    fetchRecipe();
  }, [id]); // Re-run when 'id' changes

  const fetchRecipe = async () => {
    console.log("Fetching recipe id - " + id);

    try {
      const res = await axios.get(
        `http://localhost:8080/recipebook/recipes/${id}`,
        {
          headers: { Authorization: "Bearer " + token },
        }
      );

      const data = res.data;
      console.log("Fetched API Response:", data); // ✅ Corrected logging

      // ✅ Ensure the data is available before setting state
      if (data) {
        setTitle(data.recipe.title); // Use default empty string if null
        setAbout(data.recipe.about || "");
        setIngridients(data.recipe.ingridients || "");
        setProcedures(data.recipe.procedures || "");
        setCookingTime(data.recipe.cookingTime || "");
        setDifficulty(data.recipe.difficulty || "");
      }
    } catch (err) {
      console.error("Fetching error -", err);
      setError("Failed to fetch recipe details");
    }
  };

  function handleSave() {
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
      .put(`http://localhost:8080/recipebook/edit/${id}`, formData, {
        headers: {
          Authorization: "Bearer " + token,
          "Content-Type": "multipart/form-data",
        },
      })
      .then((res) => {
        alert("Saved");
      })
      .catch((err) => {
        console.log(err);
      });
  }

  return (
    <>
      <Navbar />
      <div className="container">
        <div className="card mt-2">
          <h1 className="card-title text-center mt-2">Edit Recipe</h1>

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
                  onInput={(e) => setTitle(e.target.value)}
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
              onClick={handleSave}
            >
              Save
            </button>
          </div>
          <div className="d-grid">
            <button
              className="btn btn-success shadow-sm"
              style={{ borderRadius: "10px" }}
              onClick={() => navigate("/profile")}
            >
              cansel
            </button>
          </div>
        </div>
      </div>
    </>
  );
}

export default chechAuth(EditRecipe);
