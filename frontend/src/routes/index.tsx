import { Routes, Route } from "react-router-dom";


import Login from "../pages/login";
import Dashboard from "../pages/dashboard";

import Chantiers from "../pages/chantiers";
import CreateChantier from "../pages/chantiers/Create";
import ChantierDetail from "../pages/chantiers/Detail";
import EditChantier from "../pages/chantiers/Edit";

import Taches from "../pages/taches";
import CreateTache from "../pages/taches/Create";
import TacheDetail from "../pages/taches/Detail";
import EditTache from "../pages/taches/Edit";

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
          element={
            <ProtectedRoute>
          <Dashboard />
            </ProtectedRoute>
        }
        />


        <Route
          path="/chantiers"
          element={
          <ProtectedRoute>
          <Chantiers />
          </ProtectedRoute>
        }
        />


        <Route
          path="/chantiers/:id"
          element={
            <ProtectedRoute>
          <ChantierDetail />
          </ProtectedRoute>
        }
        />
        <Route
        path="/chantiers/:id/modifier"
        element={<EditChantier />}
        />

        <Route
          path="/taches"
          element={
            <ProtectedRoute>
          <Taches />
            </ProtectedRoute>
        }
        />

        <Route
  path="/taches/:id"
  element={
    <ProtectedRoute>
      <TacheDetail />
    </ProtectedRoute>
  }
/>

<Route
  path="/taches/:id/modifier"
  element={
    <ProtectedRoute>
      <EditTache />
    </ProtectedRoute>
  }
/>

        <Route
        path="/taches/nouveau"
        element={
        <ProtectedRoute>
        <CreateTache />
        </ProtectedRoute>
        }
        />


        <Route
          path="/documents"
          element={
            <ProtectedRoute>
          <Documents />
            </ProtectedRoute>
        }
        />


        <Route
          path="/incidents"
          element={
            <ProtectedRoute>
          <Incidents />
            </ProtectedRoute>
        }
        />


        <Route
          path="/livraisons"
          element={
            <ProtectedRoute>
          <Livraisons />
            </ProtectedRoute>
        }
        />


        <Route
          path="/utilisateurs"
          element={
          <ProtectedRoute>
          <Utilisateurs />
          </ProtectedRoute>
        }
        />


        <Route
          path="/profil"
          element={
          <ProtectedRoute>
          <Profil />
          </ProtectedRoute>
        }
        />

        <Route
        path="/chantiers/nouveau"
        element={
        <ProtectedRoute>
        <CreateChantier />
        </ProtectedRoute>
  }
/>


      </Route>


    </Routes>

  );

}