import { NavLink, useNavigate, useParams } from "react-router-dom";
import Navbar from "./Navbar";
import { useEffect, useState } from "react";
import axios from "axios";
import chechAuth from "../auth/checkAuth";

function ViewRecipe() {
  const { id } = useParams();
  let [recipe, setRecipe] = useState({});

  const navigate = useNavigate();

  useEffect(() => {
    fetchRecipe();
  }, []); // Ensure fetchRecipe is called only once when the component is mounted

  function fetchRecipe() {
    console.log("Api fetfhe");

    let token = localStorage.getItem("authToken");
    axios
      .get(`http://localhost:8080/recipebook/recipes/${id}`, {
        headers: { Authorization: "Bearer " + token },
      })
      .then((res) => {
        let rec = res.data.recipe;
        setRecipe(res.data.recipe);
        console.log(rec.viewCount);
      })
      .catch((err) => {
        console.error(err);
      });
  }

  return (
    <>
      <Navbar />
      <div className="container mt-4">
        <div className="row justify-content-center">
          <div className="col-md-8">
            <span className="position-absolute top-10 end-90 pt-2">
              <button
                className="btn btn-dark"
                onClick={() => navigate("/listingPage")}
              >
                <i class="bi bi-arrow-left"></i> Back
              </button>
            </span>
            {/* Recipe Title and About */}
            <h1 className="text-center text-dark mb-3">{recipe.title}</h1>
            <p className="lead text-muted text-center">{recipe.about}</p>

            {/* Difficulty and Cooking Time */}
            <div className="d-flex justify-content-between my-4">
              <div>
                <h5>Difficulty: {recipe.difficulty}</h5>
              </div>
              <div>
                <h5>
                  Cooking Time: {recipe.cookingTime}{" "}
                  <i className="bi bi-clock"></i>
                </h5>
              </div>
            </div>

            {/* Tabs for Ingredients and Preparation */}
            <ul className="nav nav-tabs" id="recipeTab" role="tablist">
              <li className="nav-item" role="presentation">
                <a
                  className="nav-link active"
                  id="ingredients-tab"
                  data-bs-toggle="tab"
                  href="#ingredients"
                  role="tab"
                  aria-controls="ingredients"
                  aria-selected="true"
                >
                  Ingredients
                </a>
              </li>
              <li className="nav-item" role="presentation">
                <a
                  className="nav-link"
                  id="preparation-tab"
                  data-bs-toggle="tab"
                  href="#preparation"
                  role="tab"
                  aria-controls="preparation"
                  aria-selected="false"
                >
                  Preparation
                </a>
              </li>
            </ul>
            <div className="tab-content mt-3" id="recipeTabContent">
              {/* Ingredients Tab */}
              <div
                className="tab-pane fade show active"
                id="ingredients"
                role="tabpanel"
                aria-labelledby="ingredients-tab"
              >
                <ul className="list-group">
                  {/* Assuming ingredients are stored as a comma-separated string */}
                  {recipe.ingridients &&
                    recipe.ingridients.split(",").map((ingridient, index) => (
                      <li key={index} className="list-group-item">
                        {ingridient.trim()}
                      </li>
                    ))}
                </ul>
              </div>

              {/* Preparation Tab */}
              <div
                className="tab-pane fade"
                id="preparation"
                role="tabpanel"
                aria-labelledby="preparation-tab"
              >
                <ol>
                  {/* Assuming preparation steps are stored as a comma-separated string */}
                  {recipe.procedures &&
                    recipe.procedures
                      .split(",")
                      .map((step, index) => <li key={index}>{step.trim()}</li>)}
                </ol>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default chechAuth(ViewRecipe);
