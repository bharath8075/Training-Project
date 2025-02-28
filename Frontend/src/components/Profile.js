import { Link, useNavigate, useParams } from "react-router-dom";
import Navbar from "./Navbar";
import { useEffect, useState } from "react";
import axios from "axios";
import chechAuth from "../auth/checkAuth";

function Profile() {
  let [recipes, setRecipes] = useState([]);
  let [user, setUser] = useState({});
  const navigate = useNavigate();
  var userId = useParams();

  useEffect(() => {
    fetchRecipe();
  }, [recipes]);
  let fetchRecipe = () => {
    let token = localStorage.getItem("authToken");
    axios
      .get("http://localhost:8080/recipebook/recipes/user", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        setRecipes(res.data.recipes);
        setUser(res.data.user);
        console.log(res.data.recipes);
      })
      .catch((err) => console.error(err));
  };

  function deleteRecipe(id) {
    let token = localStorage.getItem("authToken");
    axios.delete("http://localhost:8080/recipebook/delete/" + id, {
      headers: { Authorization: "Bearer " + token },
    });
    console.log(id);
  }

  const handleEdit = (id) => {
    // console.log("Recipe id - " + id);
    navigate(`/editRecipe/${id}`);
  };
  return (
    <>
      <Navbar />
      <div className="container my-4">
        {/* Profile Card */}
        <span className="position-absolute top-10 end-90 pt-2">
          <button
            className="btn btn-dark"
            onClick={() => navigate("/listingPage")}
          >
            <i class="bi bi-arrow-left"></i> Back
          </button>
        </span>
        <div className="d-flex justify-content-center">
          <div
            className="card shadow-lg bg-light"
            style={{
              width: "100%",
              maxWidth: "600px",
              borderRadius: "15px",
              padding: "20px",
            }}
          >
            <h1 className="card-title text-center">Profile</h1>
            <div className="card-body">
              <p className="card-text fw-bold">
                <i className="bi bi-person-circle me-2"></i>Name -
                <span className="text-muted ms-2">{user.name}</span>
              </p>
              <p className="card-text fw-bold">
                <i className="bi bi-envelope me-2"></i>Email -
                <span className="text-muted ms-2">{user.email}</span>
              </p>
              <p className="card-text fw-bold">
                <i className="bi bi-lock-fill me-2"></i>Password -
                <Link
                  to={"/changePassword"}
                  className="ms-2 text-decoration-none text-primary"
                >
                  Change Password
                </Link>
              </p>
              <p className="card-text fw-bold">
                <i className="bi bi-check-circle me-2 text-success"></i>Status -
                <span className="badge rounded-pill bg-success ms-2">
                  Active
                </span>
              </p>
            </div>
          </div>
        </div>

        {/* My Recipes Section */}
        <h2 className="text-dark mt-5">My Recipes</h2>
        <div className="row gy-4">
          {recipes.map((recipe) => (
            <div className="col-12 col-sm-6 col-md-4 col-lg-3" key={recipe.id}>
              <div
                className="card shadow-sm border-0"
                style={{ cursor: "pointer", borderRadius: "15px" }}
              >
                <img
                  className="card-img-top"
                  src={
                    recipe.image
                      ? `data:image/jpeg;base64,${recipe.image}`
                      : "https://via.placeholder.com/200"
                  }
                  alt={recipe.title}
                  style={{
                    height: "180px",
                    objectFit: "cover",
                    borderTopLeftRadius: "15px",
                    borderTopRightRadius: "15px",
                  }}
                  onClick={() => navigate(`/viewRecipe/${recipe.id}`)}
                />
                <div
                  className="card-body"
                  onClick={() => navigate(`/viewRecipe/${recipe.id}`)}
                >
                  <h5 className="card-title text-truncate">{recipe.title}</h5>
                  <p className="card-text text-muted">by {user.name}</p>
                  <div className="row">
                    <div className="col-6">
                      {/* {recipe.isEnabled === false ? (
                        <span className="badge rounded-pill bg-success ms-2">
                          Active
                        </span>
                      ) : (
                        <span className="badge rounded-pill bg-danger ms-2">
                          InActive
                        </span>
                      )} */}

                      {recipe.enabled === false && (
                        <span className="badge rounded-pill bg-danger ms-2">
                          InActive
                        </span>
                      )}
                    </div>
                    <div className="col-6 text-end">
                      <span>
                        <i class="bi bi-eye-fill"></i>
                        &nbsp;{recipe.viewCount}
                      </span>
                    </div>
                  </div>
                </div>
                <div className="card-footer bg-white border-top-0 d-flex">
                  <button
                    className="btn btn-sm btn-danger me-2"
                    onClick={() => deleteRecipe(recipe.id)}
                  >
                    <i className="bi bi-trash"></i> Delete
                  </button>
                  <button
                    className="btn btn-sm btn-primary ms-auto"
                    onClick={() => handleEdit(recipe.id)}
                  >
                    <i className="bi bi-pencil-square"></i> Edit
                  </button>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </>
  );
}

export default chechAuth(Profile);
