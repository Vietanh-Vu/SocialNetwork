import {Mountain} from "lucide-react";
import ModeToggle from "@/app/(overview)/components/layout/ModeToggle";
import UserAccount from "@/app/(overview)/components/user/UserAccount";
import {getUserProfile} from "@/lib/data";

export async function AdminHeader() {
    const {_, data: userInfo} = await getUserProfile();

    return (
        <header className="border-b bg-background z-10">
            <div className="flex h-16 items-center px-6">
                <div className="flex items-center gap-2 font-semibold">
                    <Mountain/>
                    {/*<div*/}
                    {/*    className="h-8 w-8 rounded-full bg-primary flex items-center justify-center text-primary-foreground">*/}
                    {/*</div>*/}
                    <div>Admin Dashboard</div>
                </div>
                <div className="ml-auto flex items-center gap-4">
                    <div className="flex justify-between gap-4">
                        <ModeToggle/>
                        <UserAccount userInfo={userInfo}/>
                    </div>
                </div>
            </div>
        </header>
    );
}
