import type { ReactNode } from "react";

import { BrowserRouter } from "react-router-dom";

import {
  QueryClient,
  QueryClientProvider,
} from "@tanstack/react-query";

import { ThemeProvider } from "@mui/material/styles";

import { theme } from "../theme/theme";

import { AuthProvider } from "../contexts/AuthContext";


const queryClient = new QueryClient();


interface ProvidersProps {
  children: ReactNode;
}


function Providers({ children }: ProvidersProps) {

  return (
    <QueryClientProvider client={queryClient}>

      <ThemeProvider theme={theme}>

        <BrowserRouter>

          <AuthProvider>
            {children}
          </AuthProvider>

        </BrowserRouter>

      </ThemeProvider>

    </QueryClientProvider>
  );
}


export default Providers;