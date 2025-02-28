import { useNavigate } from "react-router-dom";

function Navbar() {
  const navigate = useNavigate();
  const pointer = { cursor: "pointer" };

  function logout() {
    localStorage.removeItem("authToken");
    navigate("/login");
  }

  return (
    <nav className="navbar navbar-expand-md custom-navbar">
      <div className="container-fluid">
        {/* Brand */}
        <a
          className="navbar-brand fw-bold text-white"
          style={{ fontSize: "1.5rem", cursor: "pointer" }}
          onClick={() => navigate("/listingPage")}
        >
          RecipeBook
        </a>

        {/* Toggler */}
        <button
          className="navbar-toggler border-0"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span className="navbar-toggler-icon custom-toggler-icon"></span>
        </button>

        {/* Navbar Items */}
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav ms-auto">
            <li className="nav-item pointer-cursor">
              <a
                style={pointer}
                className="nav-link custom-link mx-2"
                onClick={() => navigate("/listingPage")}
              >
                <i className="bi bi-house-door me-1"></i>Home
              </a>
            </li>
            <li className="nav-item pointer-cursor">
              <a
                style={pointer}
                className="nav-link custom-link mx-2"
                onClick={() => navigate("/profile")}
              >
                <i className="bi bi-person-circle me-1"></i>Profile
              </a>
            </li>
            <li className="nav-item pointer-cursor">
              <a className="nav-link custom-link mx-2" style={pointer}>
                <i className="bi bi-info-circle me-1"></i>About Us
              </a>
            </li>
            <li className="nav-item pointer-cursor">
              <a
                className="nav-link custom-link-danger mx-2"
                style={pointer}
                onClick={logout}
              >
                <i className="bi bi-box-arrow-right me-1"></i>Logout
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
