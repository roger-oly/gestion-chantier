import api from "./api";


interface LoginRequest {
  email: string;
  motDePasse: string;
}


export const login = async (
  data: LoginRequest
) => {

  const response = await api.post(
    "/auth/login",
    data
  );

  return response.data;
};