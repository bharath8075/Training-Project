import logo from "./logo.svg";
import "./App.css";
import Signup from "./auth/Signup";
import Navbar from "./components/Navbar";
import Pizza from "./Image/Pizza.jpg";
import Biriyani from "./Image/Biriyani3.webp";
import Samosa from "./Image/Somosa.png";
import { useNavigate } from "react-router-dom";

function App() {
  let navigate = useNavigate();

  return (
    <>
      <Navbar />
      <div className="container my-5">
        <div className="row align-items-center">
          {/* Left Section */}
          <div className="col-lg-6 mb-4">
            <h1 className="display-4">RecipeBook</h1>
            <p className="lead">
              Explore and share amazing recipes from around the world. Whether
              you're a professional chef or a home cook, RecipeBook is your
              go-to platform for all things delicious.
            </p>
            <div className="d-flex">
              <button
                className="btn btn-primary me-3"
                onClick={() => navigate("/signup")}
              >
                Signup
              </button>
              <button
                className="btn btn-outline-secondary"
                onClick={() => navigate("/login")}
              >
                Login
              </button>
            </div>
          </div>

          {/* Right Section (Carousel) */}
          <div className="col-lg-6">
            <div
              id="recipeCarousel"
              className="carousel slide overflow-hidden"
              data-bs-ride="carousel"
            >
              <div className="carousel-inner">
                <div className="carousel-item active">
                  <img
                    src={Pizza}
                    className="d-block w-100"
                    alt="Delicious Pizza"
                  />
                </div>
                <div className="carousel-item">
                  <img
                    src={Samosa}
                    className="d-block w-100"
                    alt="Crispy Samosa"
                  />
                </div>
                <div className="carousel-item">
                  <img
                    src={Biriyani}
                    className="d-block w-100"
                    alt="Tasty Biriyani"
                  />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default App;
