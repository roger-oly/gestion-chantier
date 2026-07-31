import type { ReactNode } from "react";
import { Box } from "@mui/material";


interface AuthLayoutProps {
  children: ReactNode;
}


export default function AuthLayout({
  children,
}: AuthLayoutProps) {

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "#f5f5f5",
      }}
    >
      {children}
    </Box>
  );
}