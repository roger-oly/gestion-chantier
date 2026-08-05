import { Routes, Route } from "react-router-dom";

import Login from "../pages/login";
import Dashboard from "../pages/dashboard";

import Chantiers from "../pages/chantiers";
import ChantierDetail from "../pages/chantiers/Detail";

import Taches from "../pages/taches";
import Documents from "../pages/documents";
import Incidents from "../pages/incidents";
import Livraisons from "../pages/livraisons";
import Utilisateurs from "../pages/utilisateurs";
import Profil from "../pages/profil";


import ProtectedRoute from "../components/auth/ProtectedRoute";

import AuthLayout from "../layouts/AuthLayout";
import MainLayout from "../layouts/MainLayout";


export default function AppRoutes() {


  return (

    <Routes>


      {/* Partie publique */}

      <Route

        path="/login"

        element={

          <AuthLayout>

            <Login />

          </AuthLayout>

        }

      />



      {/* Partie privée */}

      <Route

        element={

          <ProtectedRoute>

            <MainLayout />

          </ProtectedRoute>

        }

      >


        <Route
          path="/dashboard"
          element={<Dashboard />}
        />


        <Route
          path="/chantiers"
          element={<Chantiers />}
        />


        <Route
          path="/chantiers/:id"
          element={<ChantierDetail />}
        />


        <Route
          path="/taches"
          element={<Taches />}
        />


        <Route
          path="/documents"
          element={<Documents />}
        />


        <Route
          path="/incidents"
          element={<Incidents />}
        />


        <Route
          path="/livraisons"
          element={<Livraisons />}
        />


        <Route
          path="/utilisateurs"
          element={<Utilisateurs />}
        />


        <Route
          path="/profil"
          element={<Profil />}
        />


      </Route>


    </Routes>

  );

}