"use client"

import {createContext, useContext, useEffect, useState} from "react";
import {jwtDecode} from "jwt-decode";
import Cookies from "js-cookie";
import axios from "axios";

const AuthContext = createContext();

function AuthProvider({children}) {
    const [currentUserId, setCurrentUserId] = useState(null);
    const [userInfo, setUserInfo] = useState(null);
    const [loading, setLoading] = useState(true);

    async function getCurrentUser(target_user_id, token) {
        try {
            const response = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/profile`, {
                params: {target_user_id},
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            return {
                isAllowed: true,
                data: response.data.result.data
            };
        } catch (error) {
            return {
                isAllowed: false,
                message: error.response?.data?.message || "An error occurred"
            };
        }
    }

    useEffect(() => {
        async function initializeAuth() {
            const token = await Cookies.get("access-token");
            if (token) {
                try {
                    const decodedToken = jwtDecode(token);
                    setCurrentUserId(decodedToken.sub);
                    const res = await getCurrentUser(decodedToken.sub, token);
                    setUserInfo(res.data);
                    // console.log("User authContext", res.data);
                } catch (error) {
                    console.error("Failed to decode token or fetch user profile", error);
                } finally {
                    setLoading(false);
                }
            } else {
                setLoading(false);
            }
        }

        initializeAuth().then(r => {
        });
    }, []);

    return (
        <AuthContext.Provider value={{currentUserId, userInfo, loading}}>
            {children}
        </AuthContext.Provider>
    )
}

function useAuth() {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error("useAuth must be used within a AuthProvider");
    }
    return context;
}

export {AuthProvider, useAuth};