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
import CreateDocument from "../pages/documents/Create";

import Incidents from "../pages/incidents";
import CreateIncident from "../pages/incidents/Create";
import ShowIncident from "../pages/incidents/Show";
import EditIncident from "../pages/incidents/Edit";

import Livraisons from "../pages/livraisons";
import CreateLivraison from "../pages/livraisons/Create";
import ShowLivraison from "../pages/livraisons/Show";
import LivraisonDetail from "../pages/livraisons/Detail";


import Utilisateurs from "../pages/utilisateurs";
import CreateUtilisateur from "../pages/utilisateurs/Create";

import EditUtilisateur from "../pages/utilisateurs/Edit";

import Notifications from "../pages/notifications";

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
  path="/documents/nouveau"
  element={
    <ProtectedRoute>
      <CreateDocument />
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
  path="/incidents/nouveau"
  element={
    <ProtectedRoute>
      <CreateIncident />
    </ProtectedRoute>
  }
/>

<Route
  path="/incidents/:id"
  element={<ShowIncident />}
/>


<Route
  path="/incidents/:id/modifier"
  element={<EditIncident />}
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
  path="/livraisons/nouveau"
  element={
    <ProtectedRoute>
      <CreateLivraison />
    </ProtectedRoute>
  }
/>

<Route
  path="/livraisons/:id"
  element={
    <ProtectedRoute>
      <ShowLivraison />
    </ProtectedRoute>
  }
/>

<Route
  path="/chantiers/:id/livraisons/nouveau"
  element={<CreateLivraison />}
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
  path="/utilisateurs/:id/modifier"
  element={
    <ProtectedRoute>
      <EditUtilisateur />
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

<Route
  path="/utilisateurs/nouveau"
  element={
    <ProtectedRoute>
      <CreateUtilisateur />
    </ProtectedRoute>
  }
/>

<Route
  path="/notifications"
  element={<Notifications />}
/>


      </Route>


    </Routes>

  );

}