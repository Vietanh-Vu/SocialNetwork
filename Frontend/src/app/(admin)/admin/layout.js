import {AdminHeader} from "@/app/(admin)/components/AdminHeader";
import {AdminSidebar} from "@/app/(admin)/components/AdminSidebar";

export default function AdminLayout({ children }) {
    return (
        <div className="min-h-screen flex flex-col">
            <AdminHeader />
            <div className="flex flex-1">
                <div className="w-2/12">
                    <AdminSidebar />
                </div>
                <main className="w-10/12 p-6 overflow-auto">{children}</main>
            </div>
        </div>
    )
}
