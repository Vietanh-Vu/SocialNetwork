import Link from "next/link";
import {Contact, HomeIcon, Mountain, NewspaperIcon} from "lucide-react";
import ModeToggle from "@/app/(overview)/components/layout/ModeToggle";
import UserAccount from "@/app/(overview)/components/user/UserAccount";
import {getUserProfile} from "@/lib/data";

async function Header() {
    const {_, data: userInfo} = await getUserProfile();

    return (
        <div className="flex flex-col bg-background">
            <header className="sticky top-0 z-40 border-b bg-background">
                <div className="container flex h-14 items-center justify-between px-4 sm:px-6 md:px-8 gap-4">
                    <Link href="/home"><Mountain/></Link>
                    <nav className="flex items-center gap-4">
                        <Link href="/home" className="flex items-center gap-2" prefetch={false}>
                            <HomeIcon className="h-5 w-5"/>
                            <span className="hidden sm:inline">Home</span>
                        </Link>
                        <Link href="/newsfeed" className="flex items-center gap-2" prefetch={false}>
                            <NewspaperIcon className="h-5 w-5"/>
                            <span className="hidden sm:inline">Newsfeed</span>
                        </Link>
                        <Link href="/friend" className="flex items-center gap-2" prefetch={false}>
                            <Contact className="h-5 w-5"/>
                            <span className="hidden sm:inline">Friend</span>
                        </Link>
                    </nav>
                    <div className="flex justify-between gap-4">
                        <ModeToggle/>
                        <UserAccount userInfo={userInfo}/>
                    </div>
                </div>
            </header>
        </div>
    )
}

export default Header;