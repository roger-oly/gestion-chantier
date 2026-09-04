import { useEffect, useState } from "react";

import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  CircularProgress,
  Divider,
  Stack,
  Typography,
} from "@mui/material";

import { useAuth } from "../../contexts/AuthContext";

import {
  getNotificationsByUtilisateur,
  marquerNotificationCommeLue,
  deleteNotification,
} from "../../services/notification.service";

import type { Notification } from "../../types/notification";


export default function Notifications() {

  const { user } = useAuth();

  const [notifications, setNotifications] =
    useState<Notification[]>([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState<string | null>(null);


  useEffect(() => {

    async function loadNotifications() {

      if (!user) {
        return;
      }

      try {

        setLoading(true);
        setError(null);

        const data =
          await getNotificationsByUtilisateur(
            user.idUtilisateur
          );

        setNotifications(data);

      } catch (error) {

        console.error(
          "Erreur chargement notifications :",
          error
        );

        setError(
          "Impossible de charger les notifications."
        );

      } finally {

        setLoading(false);

      }

    }


    loadNotifications();

  }, [user]);


  async function handleMarkAsRead(
    idNotification: number
  ) {

    try {

      const notification =
        await marquerNotificationCommeLue(
          idNotification
        );

      setNotifications((current) =>
        current.map((item) =>
          item.idNotification === idNotification
            ? notification
            : item
        )
      );

    } catch (error) {

      console.error(
        "Erreur lors du marquage de la notification :",
        error
      );

    }

  }


  async function handleDelete(
    idNotification: number
  ) {

    try {

      await deleteNotification(
        idNotification
      );

      setNotifications((current) =>
        current.filter(
          (item) =>
            item.idNotification !== idNotification
        )
      );

    } catch (error) {

      console.error(
        "Erreur suppression notification :",
        error
      );

    }

  }


  if (!user) {

    return (
      <Typography color="error">
        Aucun utilisateur connecté.
      </Typography>
    );

  }


  if (loading) {

    return (
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          py: 5,
        }}
      >
        <CircularProgress />
      </Box>
    );

  }


  if (error) {

    return (
      <Alert severity="error">
        {error}
      </Alert>
    );

  }


  return (

    <Box>

      <Typography
        variant="h4"
        gutterBottom
      >
        Notifications
      </Typography>


      <Divider sx={{ mb: 3 }} />


      {notifications.length === 0 ? (

        <Alert severity="info">
          Aucune notification.
        </Alert>

      ) : (

        <Stack spacing={2}>

          {notifications.map((notification) => (

            <Card
              key={notification.idNotification}
              sx={{
                backgroundColor:
                  notification.lu
                    ? "inherit"
                    : "action.hover",
              }}
            >

              <CardContent>

                <Box
                  sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "flex-start",
                    gap: 2,
                  }}
                >

                  <Box>

                    <Typography
                      variant="h6"
                      fontWeight={
                        notification.lu
                          ? "normal"
                          : "bold"
                      }
                    >
                      {notification.titre}
                    </Typography>

                    <Typography
                      variant="body2"
                      color="text.secondary"
                      sx={{ mt: 0.5 }}
                    >
                      {notification.type}
                    </Typography>

                  </Box>


                  {!notification.lu && (

                    <Typography
                      variant="caption"
                      fontWeight="bold"
                    >
                      Non lue
                    </Typography>

                  )}

                </Box>


                <Typography
                  sx={{ mt: 2 }}
                >
                  {notification.message}
                </Typography>


                <Typography
                  variant="caption"
                  color="text.secondary"
                  display="block"
                  sx={{ mt: 2 }}
                >
                  {new Date(
                    notification.dateCreation
                  ).toLocaleString("fr-FR")}
                </Typography>


                <Stack
                  direction="row"
                  spacing={1}
                  sx={{ mt: 2 }}
                >

                  {!notification.lu && (

                    <Button
                      size="small"
                      variant="outlined"
                      onClick={() =>
                        handleMarkAsRead(
                          notification.idNotification
                        )
                      }
                    >
                      Marquer comme lue
                    </Button>

                  )}


                  <Button
                    size="small"
                    color="error"
                    onClick={() =>
                      handleDelete(
                        notification.idNotification
                      )
                    }
                  >
                    Supprimer
                  </Button>

                </Stack>

              </CardContent>

            </Card>

          ))}

        </Stack>

      )}

    </Box>

  );

}