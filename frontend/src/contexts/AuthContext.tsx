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


interface AuthContextType {
  user: any;
  loginUser: (userData: any) => void;
  logout: () => void;
}


const AuthContext = createContext<AuthContextType | null>(null);


interface AuthProviderProps {
  children: ReactNode;
}


export function AuthProvider({
  children,
}: AuthProviderProps) {

  const [user, setUser] = useState(
    getUser()
  );


  function loginUser(userData: any) {

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