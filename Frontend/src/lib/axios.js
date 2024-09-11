"use server";

import axios from "axios";
import {cookies} from "next/headers";
import {refreshAccessToken} from "@/lib/action";
import {NextResponse} from "next/server";
import {redirect} from "next/navigation";
import process from "next/dist/build/webpack/loaders/resolve-url-loader/lib/postcss";
import Cookies from "js-cookie";

const config = {
    baseURL: process.env.NEXT_PUBLIC_API_URL,
};

const http = axios.create(config);

const onRequest = async (config) => {
    let accessToken = cookies().get("access-token")?.value;
    const refreshToken = cookies().get("refresh-token")?.value;
    // console.log("accessToken trong axios: ", accessToken)
    // if (!accessToken && refreshToken) {
    //     const result = await refreshAccessToken(refreshToken);
    //     console.log(result)
    //     const response = NextResponse.next();
    //     if (result.isSuccessful) {
    //         accessToken = result.data.accessToken;
    //         console.log("accessToken trong axios: ", accessToken)
    //         response.cookies.set("access-token", accessToken, {
    //             maxAge: process.env.NEXT_PUBLIC_ACCESS_TOKEN_EXPIRY,
    //             httpOnly: true,
    //         });
    //
    //         // Trả về response với cookies mới set
    //         return response;
    //     } else {
    //         redirect("/login");
    //     }
    // }


    config.headers = {
        ...config.headers,
        Authorization: `Bearer ${accessToken}`,
    };
    return config;
};

const onRequestError = (error) => {
    return Promise.reject(error);
};

// Request interceptor
http.interceptors.request.use(onRequest, onRequestError);

export default http;
