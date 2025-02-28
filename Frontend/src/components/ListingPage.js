import Navbar from "./Navbar";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { useEffect, useState } from "react";
import chechAuth from "../auth/checkAuth";

function ListingPage() {
  let [recipes, setRecipes] = useState([]);
  let [searchTerm, setSearchTerm] = useState("");
  let [filteredRecipeList, setFilteredRecipeList] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchRecipe();
  }, []);
  //Fetching the recipe
  let token = localStorage.getItem("authToken");
  const fetchRecipe = () => {
    // if (!token) {
    //   // alert("Please Login First");
    //   navigate("/login");
    // }
    axios
      .get("http://localhost:8080/recipebook/recipes", {
        headers: { Authorization: "Bearer " + token },
      })
      .then((res) => {
        console.log(recipes);
        setRecipes(res.data);
        setFilteredRecipeList(res.data);
      })
      .catch((err) => console.error(err));
  };

  //Function for Search
  const handleSearchTerm = (e) => {
    const term = e.target.value.toLowerCase();
    setSearchTerm(term);

    const filtered = recipes.filter((recipe) =>
      recipe.title.toLowerCase().includes(term)
    );

    setFilteredRecipeList(filtered);
  };

  return (
    <>
      <Navbar />
      <div className="container">
        {/* Search Bar */}
        <div className="input-group d-flex justify-content-center mt-4">
          <input
            type="search"
            className="form-control w-40"
            placeholder="Search for recipes..."
            value={searchTerm}
            onChange={handleSearchTerm}
          />
          <button className="btn btn-outline-dark">Search</button>
        </div>

        {/* Add Recipe Button */}
        <button
          className="btn btn-primary btn-sm position-fixed d-flex align-items-center gap-1"
          style={{
            bottom: "30px",
            right: "30px",
            zIndex: "1000",
            borderRadius: "50%",
            padding: "15px",
          }}
          onClick={() => navigate("/addRecipe")}
        >
          <i className="bi bi-plus-square fs-4"></i>
        </button>

        {/* Recipe Cards */}
        <div className="row mb-2 mt-2">
          {filteredRecipeList.length > 0 ? (
            filteredRecipeList.map((recipe) => (
              <div className="col-lg-4 col-md-6 col-sm-12" key={recipe.id}>
                <div
                  className="card shadow mt-3 border-0"
                  style={{ cursor: "pointer" }}
                  onClick={() => navigate(`/viewRecipe/${recipe.id}`)}
                >
                  <img
                    className="card-img-top img-fluid"
                    style={{
                      height: "200px",
                      objectFit: "cover",
                      borderTopLeftRadius: "8px",
                      borderTopRightRadius: "8px",
                    }}
                    src={
                      recipe.image
                        ? `data:image/jpeg;base64,${recipe.image}`
                        : "https://via.placeholder.com/200"
                    }
                    alt={recipe.title}
                  />
                  <div className="card-body">
                    <h5 className="card-title text-primary">{recipe.title}</h5>
                    <p className="card-text text-muted">
                      By {recipe.user}
                      <span className="d-flex justify-content-end">
                        <i class="bi bi-eye-fill"></i>&nbsp; {recipe.viewCount}
                      </span>
                    </p>
                  </div>
                </div>
              </div>
            ))
          ) : (
            <p className="text-center mt-4">No Recipes found</p>
          )}
        </div>
      </div>
    </>
  );
}

export default chechAuth(ListingPage);
