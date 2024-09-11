"use client"
import Link from "next/link"
import {logout} from "@/lib/action";
import {useEffect} from "react";

export default function Page() {
    useEffect(() => {
        const performLogout = async () => {
            const result = await logout();
            if (result?.isSuccessful) {
                // You can optionally show a success message here
                // console.log(result.message);
            } else {
                // Handle logout failure (e.g., show an error message)
                // console.error(result.message);
            }
        };

        performLogout().then(r => {
        });
    }, []);

    return (
        <div
            className="flex min-h-[100dvh] flex-col items-center justify-center bg-background px-4 py-12 sm:px-6 lg:px-8">
            <div className="mx-auto max-w-md text-center">
                <div className="mx-auto h-12 w-12 text-green-500"/>
                <h1 className="mt-4 text-3xl font-bold tracking-tight text-foreground sm:text-4xl">Logout
                    Successful</h1>
                <p className="mt-4 text-muted-foreground">
                    You have been successfully logged out. Click the button below to return to the login page.
                </p>
                <div className="mt-6">
                    <Link
                        href="/login"
                        className="inline-flex items-center rounded-md bg-primary px-4 py-2 text-sm font-medium text-primary-foreground shadow-sm transition-colors hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
                        prefetch={false}
                    >
                        Go to Login
                    </Link>
                </div>
            </div>
        </div>
    )
}