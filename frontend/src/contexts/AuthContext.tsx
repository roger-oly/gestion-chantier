import {
  createContext,
  useContext,
  useState,
  type ReactNode,
} from "react";

import {
  getUser,
  removeUser,
  saveUser,
} from "../services/storage.service";

import type { AuthUser } from "../types/auth";

interface AuthContextType {
  user: AuthUser | null;
  loginUser: (userData: AuthUser) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | null>(null);

interface AuthProviderProps {
  children: ReactNode;
}

export function AuthProvider({
  children,
}: AuthProviderProps) {

  const [user, setUser] = useState<AuthUser | null>(
    getUser()
  );

  console.log(
    "Utilisateur connecté :",
    user
  );

  function loginUser(userData: AuthUser) {

    saveUser(userData);

    setUser(userData);
  }

  function logout() {

    removeUser();

    setUser(null);
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        loginUser,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {

  const context = useContext(AuthContext);

  if (!context) {
    throw new Error(
      "useAuth doit être utilisé dans AuthProvider"
    );
  }

  return context;
}