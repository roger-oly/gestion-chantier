import {
  createContext,
  useContext,
  type ReactNode,
} from "react";


const AuthContext = createContext(null);


interface AuthProviderProps {
  children: ReactNode;
}


export function AuthProvider({
  children,
}: AuthProviderProps) {

  return (
    <AuthContext.Provider value={null}>
      {children}
    </AuthContext.Provider>
  );
}


export function useAuth() {
  return useContext(AuthContext);
}