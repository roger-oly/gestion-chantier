import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Badge } from "@mui/material";

import { useAuth } from "../../contexts/AuthContext";
import { getNotificationsNonLues } from "../../services/notification.service";


export default function Sidebar() {

  const { user } = useAuth();

  const [nombreNotifications, setNombreNotifications] =
    useState(0);


  useEffect(() => {
if (!user) {
  setNombreNotifications(0);
  return;
}

const idUtilisateur = user.idUtilisateur;

async function loadNotificationsNonLues() {

  try {

    const notifications =
      await getNotificationsNonLues(
        idUtilisateur
      );

    setNombreNotifications(
      notifications.length
    );

  } catch (error) {

    console.error(
      "Erreur chargement notifications non lues :",
      error
    );

    setNombreNotifications(0);

  }

}


    loadNotificationsNonLues();


    const interval = setInterval(
      loadNotificationsNonLues,
      30000
    );


    return () => {
      clearInterval(interval);
    };

  }, [user]);


  return (
    <aside>

      <nav>

        <ul>

          <li>
            <Link to="/dashboard">
              Dashboard
            </Link>
          </li>


          <li>
            <Link to="/chantiers">
              Chantiers
            </Link>
          </li>


          <li>
            <Link to="/utilisateurs">
              Utilisateurs
            </Link>
          </li>


          <li>
            <Link to="/notifications">

              Notifications

              {nombreNotifications > 0 && (
                <Badge
                  badgeContent={nombreNotifications}
                  color="error"
                  sx={{ ml: 2 }}
                />
              )}

            </Link>
          </li>


        </ul>

      </nav>

    </aside>
  );
}