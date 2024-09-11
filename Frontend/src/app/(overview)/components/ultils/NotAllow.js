import {Lock} from "lucide-react";
import Link from "next/link";

function NotAllow({message}) {
    return (
        <div
            className="flex flex-col items-center justify-center bg-background px-4 py-12 sm:px-6 lg:px-8">
            <div className="mx-auto max-w-md text-center">
                <Lock className="mx-auto h-12 w-12 text-primary"/>
                <h1 className="mt-4 text-3xl font-bold tracking-tight text-foreground sm:text-4xl">Access Denied</h1>
                <p className="mt-4 text-muted-foreground">
                    {message}
                </p>
            </div>
        </div>
    )
}

export default NotAllow;