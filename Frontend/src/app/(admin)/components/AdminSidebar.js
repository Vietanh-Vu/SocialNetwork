"use client";

import Link from "next/link";
import {usePathname} from "next/navigation";
import {MessageSquareWarning, Settings, AlertTriangle} from "lucide-react";

export function AdminSidebar() {
    const pathname = usePathname();

    const isActive = (path) => {
        return pathname.startsWith(path);
    };

    return (
        <div className="h-full bg-background p-4 border-r">
            <div className="mb-4">
                <ul className="space-y-2">
                    <li>
                        <Link
                            href="/admin/global-config"
                            className={`flex items-center gap-2 p-2 rounded-md text-sm ${
                                isActive("/admin/global-config")
                                    ? "bg-muted text-primary"
                                    : "hover:bg-muted/50"
                            }`}
                        >
                            <Settings className="h-4 w-4"/>
                            <span>Global Config</span>
                        </Link>
                    </li>
                    <li>
                        <Link
                            href="/admin/problematic-comment"
                            className={`flex items-center gap-2 p-2 rounded-md text-sm ${
                                isActive("/admin/problematic-comment")
                                    ? "bg-muted text-primary"
                                    : "hover:bg-muted/50"
                            }`}
                        >
                            <MessageSquareWarning className="h-4 w-4"/>
                            <span>Problematic Comments</span>
                        </Link>
                    </li>
                    <li>
                        <Link
                            href="/admin/top-violators"
                            className={`flex items-center gap-2 p-2 rounded-md text-sm ${
                                isActive("/admin/top-violators")
                                    ? "bg-muted text-primary"
                                    : "hover:bg-muted/50"
                            }`}
                        >
                            <AlertTriangle className="h-4 w-4"/>
                            <span>Top Violators</span>
                        </Link>
                    </li>
                </ul>
            </div>
        </div>
    );
}