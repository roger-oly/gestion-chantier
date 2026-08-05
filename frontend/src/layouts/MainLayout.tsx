import { Outlet } from "react-router-dom";

import Header from "../components/layout/Header";
import Sidebar from "../components/layout/Sidebar";
import Footer from "../components/layout/Footer";
import {
  Box,
} from "@mui/material";


export default function MainLayout() {

  return (

    <Box

      sx={{

        minHeight: "100vh",

        display: "flex",

        flexDirection: "column"

      }}

    >


      <Header />


      <Box

        sx={{

          display: "flex",

          flex: 1

        }}

      >


        <Sidebar />


        <Box

          component="main"

          sx={{

            flexGrow: 1,

            p: 3

          }}

        >

          <Outlet />

        </Box>


      </Box>


      <Footer />


    </Box>

  );

}