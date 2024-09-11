import Header from "@/app/(overview)/components/layout/Header";
import Sidebar from "@/app/(overview)/components/layout/Sidebar";
import {Suspense} from "react";
import Spinner from "@/app/(overview)/components/ultils/Spinner";

export default function Layout({children}) {
    return (
        <div className="flex min-h-screen flex-col bg-background">
            <Header/>
            <main className="container flex-1 grid grid-cols-[300px_1fr] gap-8 px-4 sm:px-6 md:px-8 py-8">
                <div className="flex flex-col gap-6">
                    <Sidebar/>
                </div>
                <div className="flex flex-col gap-6">
                    <Suspense fallback={<Spinner/>}>
                        {children}
                    </Suspense>
                </div>
            </main>
        </div>
    )
}