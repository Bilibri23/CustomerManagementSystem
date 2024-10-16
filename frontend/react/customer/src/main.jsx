import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import {ChakraProvider} from "@chakra-ui/react";
import './index.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import  {createStandaloneToast} from "@chakra-ui/toast";
import Login from "./components/login/Login.jsx";
import AuthProvider from "./components/context/AuthContext.jsx";
import Signup from "./components/signup/Signup.jsx";
import ProtectedRoute from "./components/shared/ProtectedRoute.jsx";


const {ToastContainer, toast} = createStandaloneToast()


const router = createBrowserRouter([
    // defining  how many routes(2) login and dashboard routes I  will use
    {
        path: "/", // default route such that when u hit the url it the first page u see
        element:<Login/>
    },
    {
        path: "/signup",
        element: <Signup/>
    },
    {
        path:"dashboard",
        element: <ProtectedRoute> <App/></ProtectedRoute>
    }

])
createRoot(document.getElementById('root')).render(
  <StrictMode>
      <ChakraProvider>
          <AuthProvider>
              <RouterProvider router={router}/>
          </AuthProvider>

          <ToastContainer/>
      </ChakraProvider>

  </StrictMode>,
)
