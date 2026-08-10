import type { AuthUser } from "../types/auth";

export const saveUser = (user: AuthUser) => {

  localStorage.setItem(
    "user",
    JSON.stringify(user)
  );

};

export const getUser = (): AuthUser | null => {

  const user = localStorage.getItem("user");

  if (!user) {
    return null;
  }

  return JSON.parse(user);

};

export const removeUser = () => {

  localStorage.removeItem("user");

};