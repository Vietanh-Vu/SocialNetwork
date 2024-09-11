import {verifyRegister} from "@/lib/action";
import {redirect} from "next/navigation";
import Link from "next/link"
import {CircleCheck, CircleX} from "lucide-react";

export default async function Page({searchParams}) {
    const res = await verifyRegister(searchParams.token);

    return (
        <div
            className="flex min-h-[100dvh] flex-col items-center justify-center bg-background px-4 py-12 sm:px-6 lg:px-8">
            <div className="mx-auto max-w-md text-center">
                <div className="space-y-4">
                    {res.isSuccessful ? <CircleCheck className="mx-auto h-12 w-12 text-green-500"/> :
                        <CircleX className="mx-auto h-12 w-12 text-red-500"/>}

                    <h1 className="text-3xl font-bold tracking-tight text-foreground">Verification Successful</h1>
                    <p className="text-muted-foreground">
                        {res.isSuccessful ? "Your account has been successfully verified. You can now log in to your account." : "Your account verification failed. Please try again."}
                    </p>
                    <Link
                        href={res.isSuccessful ? "/login" : "/register"}
                        className="inline-flex items-center rounded-md bg-primary px-4 py-2 text-sm font-medium text-primary-foreground shadow-sm transition-colors hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
                        prefetch={false}
                    >
                        {res.isSuccessful ? "Go to Login" : "Go to Register"}
                    </Link>
                </div>
            </div>
        </div>
    )
}