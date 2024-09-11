import Link from "next/link";

export default function Page({children}) {
    return (
        <div className="w-full max-w-[800px] mx-auto py-8">
            <nav className="flex border-b justify-around">
                <Link href="/friend/list"
                      className="px-4 py-2 text-sm font-medium transition-colors hover:bg-muted hover:text-foreground focus:bg-muted focus:text-foreground focus:outline-none">
                    Friends
                </Link>
                <Link href="/friend/request"
                      className="px-4 py-2 text-sm font-medium transition-colors hover:bg-muted hover:text-foreground focus:bg-muted focus:text-foreground focus:outline-none">
                    Friend Requests
                </Link>
                <Link href="/friend/received"
                      className="px-4 py-2 text-sm font-medium transition-colors hover:bg-muted hover:text-foreground focus:bg-muted focus:text-foreground focus:outline-none">
                    Received Requests
                </Link>
                <Link href="/friend/block"
                      className="px-4 py-2 text-sm font-medium transition-colors hover:bg-muted hover:text-foreground focus:bg-muted focus:text-foreground focus:outline-none">
                    Blocked
                </Link>
                <Link href="/friend/search"
                      className="px-4 py-2 text-sm font-medium transition-colors hover:bg-muted hover:text-foreground focus:bg-muted focus:text-foreground focus:outline-none">
                    Search
                </Link>
            </nav>
            {children}
        </div>
    )
}