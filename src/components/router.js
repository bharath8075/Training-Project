import { createBrowserRouter } from "react-router-dom";
import App from "../App";
import Login from "../auth/Login";
import ListingPage from "./ListingPage";
import ViewRecipe from "./ViewRecipe";
import AddRecipe from "./AddRecipe";
import Profile from "./Profile";
import PasswordSet from "../auth/PasswordSet";
import Signup from "../auth/Signup";
import EditRecipe from "./EditRecipe";

const router = createBrowserRouter([
  { path: "/", element: <App /> },
  { path: "/signup", element: <Signup /> },
  { path: "/login", element: <Login /> },
  { path: "/listingPage", element: <ListingPage /> },
  { path: "/viewRecipe/:id", element: <ViewRecipe /> },
  { path: "/addRecipe", element: <AddRecipe /> },
  { path: "/profile", element: <Profile /> },
  { path: "/changePassword", element: <PasswordSet /> },
  { path: "/editRecipe/:id", element: <EditRecipe /> },
]);

export default router;
