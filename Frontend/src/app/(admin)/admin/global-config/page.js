import { Button } from "@/components/ui/button"
import { Plus } from "lucide-react"
import {AddGlobalConfigDialog} from "@/app/(admin)/components/global-config/AddGlobalConfigDialog";
import {GlobalConfigSearch} from "@/app/(admin)/components/global-config/GlobalConfigSearch";
import {GlobalConfigTable} from "@/app/(admin)/components/global-config/GlobalConfigTable";

export default function GlobalConfigPage() {
    return (
        <div className="space-y-4">
            <div className="flex items-center justify-between">
                <h1 className="text-2xl font-bold tracking-tight">Global Configuration</h1>
                <AddGlobalConfigDialog>
                    <Button>
                        <Plus className="mr-1 h-4 w-4" />
                        Add Config
                    </Button>
                </AddGlobalConfigDialog>
            </div>
            {/*<GlobalConfigSearch />*/}
            <GlobalConfigTable />
        </div>
    )
}
