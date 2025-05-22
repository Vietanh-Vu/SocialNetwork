import {NextResponse} from "next/server";
import {refreshAccessToken} from "@/lib/action";
import {cookies} from "next/headers";
import Cookies from "js-cookie";
import {jwtDecode} from "jwt-decode";

export async function middleware(request) {
    const accessToken = request.cookies.get("access-token")?.value;
    const refreshToken = request.cookies.get("refresh-token")?.value;
    const callbackUrl = request.nextUrl.pathname;
    const url = request.nextUrl.clone();

    // Kiểm tra xem route hiện tại có yêu cầu xác thực không
    if (isPublicRoute(request.nextUrl.pathname)) {
        return NextResponse.next();
    }

    // Nếu không có access token và có refresh token, gọi API để làm mới access token
    if (!accessToken && refreshToken) {
        const result = await refreshAccessToken(refreshToken);
        if (result.isSuccessful) {
            const response = NextResponse.next();
            // Set cookie cho access token mới
            response.cookies.set("access-token", result.data.accessToken, {
                maxAge: parseInt(process.env.NEXT_PUBLIC_ACCESS_TOKEN_EXPIRY),
            });
            // Set cookie cho refresh token mới
            response.cookies.set("refresh-token", result.data.refreshToken, {
                maxAge: parseInt(process.env.NEXT_PUBLIC_ACCESS_REFRESH_EXPIRY),
            });
            return response;
        }
    }

    // Kiểm tra route dành cho admin
    if (isAdminRoute(request.nextUrl.pathname)) {
        if (!accessToken) {
            url.pathname = "/login";
            return NextResponse.redirect(url);
        }

        try {
            const decoded = jwtDecode(accessToken);
            const isAdmin = decoded.authorities && Array.isArray(decoded.authorities) &&
                decoded.authorities.includes('ADMIN');

            if (!isAdmin) {
                // Nếu không phải admin, chuyển hướng về trang chính
                url.pathname = "/home";
                return NextResponse.redirect(url);
            }
            // url.pathname = "/admin";
            // return NextResponse.redirect(url);
        } catch (error) {
            console.log(error)
            url.pathname = "/login";
            return NextResponse.redirect(url);
        }
    }

    if (!accessToken || !refreshToken) {
        // return NextResponse.redirect(new URL(`/login?callbackUrl=${callbackUrl}`, request.url));
        return NextResponse.redirect(new URL(`/login`, request.url));
    }

    // Kiểm tra nếu đã đăng nhập và có token hợp lệ
    if (accessToken && request.nextUrl.pathname === "/login") {
        try {
            const decoded = jwtDecode(accessToken);
            const isAdmin = decoded.authorities && Array.isArray(decoded.authorities) &&
                decoded.authorities.includes('ADMIN');

            // Chuyển hướng dựa trên role
            if (isAdmin) {
                url.pathname = "/admin";
                return NextResponse.redirect(url);
            } else {
                url.pathname = "/home";
                return NextResponse.redirect(url);
            }
        } catch (error) {
            // Token không hợp lệ, để người dùng tiếp tục vào trang login
            return NextResponse.next();
        }
    }

    return NextResponse.next();
}

function isPublicRoute(pathname) {
    const publicRoutes = ["/login", "/register", "/reset-password", "/register/verify", "/forgot_password", "/forgot_password/verify"];
    return publicRoutes.includes(pathname);
}

function isAdminRoute(pathname) {
    return pathname.startsWith('/admin');
}

export const config = {
    matcher: [
        /*
         * Match all request paths except for the ones starting with:
         * - api (API routes)
         * - _next/static (static files)
         * - _next/image (image optimization files)
         * - favicon.ico (favicon file)
         */
        "/((?!api|_next/static|_next/image|favicon.ico).*)",
    ],
};