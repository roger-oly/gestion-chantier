import { Routes, Route } from "react-router-dom";

import Login from "../pages/login";
import Dashboard from "../pages/dashboard";
import AuthLayout from "../layouts/AuthLayout";


export default function AppRoutes() {

  return (
    <Routes>

      <Route
        path="/login"
        element={
        <AuthLayout>
          <Login />
        </AuthLayout>
        }
      />


      <Route
        path="/dashboard"
        element={<Dashboard />}
      />


    </Routes>
  );
}