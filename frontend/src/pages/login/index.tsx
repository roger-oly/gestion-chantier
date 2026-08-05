import { login } from "../../services/auth.service";
import { useForm } from "react-hook-form";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";

interface LoginForm {
  email: string;
  motDePasse: string;
}
import {
  Box,
  Button,
  TextField,
  Typography,
  Paper,
} from "@mui/material";


export default function Login() {
  const {
  register,
  handleSubmit,
  formState: {errors},
} = useForm<LoginForm>();

    const navigate = useNavigate();
    
    const { loginUser } = useAuth();

    const onSubmit = async (data: LoginForm) => {

    try {

    const response = await login(data);

    loginUser(response);

    console.log("Connexion réussie :", response);

    navigate("/dashboard");

  } catch (error) {

    console.log("Erreur de connexion :", error);

  }

};

  return (

    <Paper
      elevation={3}
      sx={{
        padding: 4,
        width: 400,
      }}
    >

      <Box
      component="form"
      onSubmit={handleSubmit(onSubmit)}
      >

        <Typography
          variant="h5"
          align="center"
          gutterBottom
        >
          Gestion et suivi de chantier
        </Typography>


        <Typography
          variant="body2"
          align="center"
          sx={{ mb: 3 }}
        >
          Connexion à votre espace
        </Typography>


        <TextField
          label="Email"
          fullWidth
          margin="normal"
          {...register("email", {
          required: "Email obligatoire",
          pattern: {
          value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
          message: "Format email invalide",
          },
          })}

          error={!!errors.email}
          helperText={errors.email?.message}
        />


        <TextField
          label="Mot de passe"
          type="password"
          fullWidth
          margin="normal"
          {...register("motDePasse", {
          required: "Mot de passe obligatoire",
          })}
          error={!!errors.motDePasse}
          helperText={errors.motDePasse?.message}
        />


        <Button
          type="submit"
          variant="contained"
          fullWidth
          sx={{ mt: 3 }}
        >
          Se connecter
        </Button>


      </Box>

    </Paper>

  );
}