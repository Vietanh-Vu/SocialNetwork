import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar"
import {Button} from "@/components/ui/button"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import {LogOut, Mountain, Settings, User} from "lucide-react"
import Link from "next/link";

export function AdminHeader() {
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
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button variant="ghost" className="relative h-8 w-8 rounded-full">
                                <Avatar className="h-8 w-8">
                                    <AvatarImage src="/placeholder-user.jpg" alt="@admin"/>
                                    <AvatarFallback>AD</AvatarFallback>
                                </Avatar>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent className="w-56" align="end" forceMount>
                            <DropdownMenuLabel className="font-normal">
                                <div className="flex flex-col space-y-1">
                                    <p className="text-sm font-medium leading-none">Admin User</p>
                                    <p className="text-xs leading-none text-muted-foreground">admin@example.com</p>
                                </div>
                            </DropdownMenuLabel>
                            <DropdownMenuSeparator/>
                            <DropdownMenuItem>
                                <User className="mr-2 h-4 w-4"/>
                                <span>Profile</span>
                            </DropdownMenuItem>
                            <DropdownMenuItem>
                                <Settings className="mr-2 h-4 w-4"/>
                                <span>Settings</span>
                            </DropdownMenuItem>
                            <DropdownMenuSeparator/>
                            <DropdownMenuItem>
                                <Link href="/logout" className="flex items-center gap-2" prefetch={false}>
                                    <LogOut className="mr-2 h-4 w-4"/>
                                    <span>Log out</span>
                                </Link>
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>
            </div>
        </header>
    )
}
