import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export const chechAuth = (Component) => {
  function Wrapper(props) {
    var token = localStorage.getItem("authToken");
    let navigate = useNavigate();

    useEffect(() => {
      if (!token) {
        navigate("/login");
      }
    }, [token]);
    return <Component {...props} />;
  }
  return Wrapper;
};

export default chechAuth;
