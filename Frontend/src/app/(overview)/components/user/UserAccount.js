"use client";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger
} from "@/components/ui/dropdown-menu";
import {Button} from "@/components/ui/button";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import Link from "next/link";
import {LogOutIcon, RectangleEllipsis, UserIcon} from "lucide-react";
import {getAvatarFallback} from "@/lib/utils";

function UserAccount({userInfo}) {
    if (!userInfo) {
        return null;
    }
    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <Button variant="ghost" size="icon" className="rounded-full">
                    <Avatar className="h-8 w-8">
                        <AvatarImage src={userInfo.avatar}/>
                        <AvatarFallback>{getAvatarFallback(userInfo.username)}</AvatarFallback>
                    </Avatar>
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
                <DropdownMenuLabel>My Account</DropdownMenuLabel>
                <DropdownMenuSeparator/>
                <DropdownMenuItem>
                    <Link href="/home" className="flex items-center gap-2" prefetch={false}>
                        <UserIcon className="h-4 w-4"/>
                        <span>Profile</span>
                    </Link>
                </DropdownMenuItem>
                <DropdownMenuItem>
                    <Link href="/change_password" className="flex items-center gap-2" prefetch={false}>
                        <RectangleEllipsis className="h-4 w-4"/>
                        <span>Password</span>
                    </Link>
                </DropdownMenuItem>
                <DropdownMenuSeparator/>
                <DropdownMenuItem>
                    <Link href="/logout" className="flex items-center gap-2" prefetch={false}>
                        <LogOutIcon className="h-4 w-4"/>
                        <span>Logout</span>
                    </Link>
                </DropdownMenuItem>
            </DropdownMenuContent>
        </DropdownMenu>
    )

}

export default UserAccount;