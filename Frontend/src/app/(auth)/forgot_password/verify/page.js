import {verifyChangePassword} from "@/lib/action";
import Link from "next/link";
import {CircleCheck, CircleX} from "lucide-react";
import {ResetPasswordForm} from "@/app/(overview)/components/auth/ResetPasswordForm";

export default async function Page({searchParams}) {
    const res = await verifyChangePassword(searchParams.token);

    return (
        <div
            className="flex min-h-[100dvh] flex-col items-center justify-center bg-background px-4 py-12 sm:px-6 lg:px-8">
            <div className="mx-auto max-w-md text-center">
                <div className="space-y-4">
                    {res.isSuccessful ? <CircleCheck className="mx-auto h-12 w-12 text-green-500"/> :
                        <CircleX className="mx-auto h-12 w-12 text-red-500"/>}

                    <h1 className="text-3xl font-bold tracking-tight text-foreground">Verification {res.isSuccessful ? 'Successful' : 'Failed'}</h1>
                    <p className="text-muted-foreground">
                        {res.isSuccessful ? "Your request has been successfully verified. You can now set up new password" : "Your request verification failed. Please try again."}
                    </p>
                    {!res.isSuccessful ? (
                        <Link
                            href="/forgot_password"
                            className="inline-flex items-center rounded-md bg-primary px-4 py-2 text-sm font-medium text-primary-foreground shadow-sm transition-colors hover:bg-primary/90 focus:outline-none focus:ring-2 focus:ring-primary focus:ring-offset-2"
                            prefetch={false}
                        >
                            Try Again
                        </Link>
                    ) : (
                        <ResetPasswordForm token={searchParams.token}/>
                    )}
                </div>
            </div>
        </div>
    )
}