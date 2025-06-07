import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ViolatorAnalysis } from "@/app/(admin)/components/top-violators/ViolatorAnalysis";
import { TopViolators } from "@/app/(admin)/components/top-violators/TopViolators";
import AppealsManagement from "@/app/(admin)/components/top-violators/AppealsManagement";

export default function TopViolatorsPage() {
    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold tracking-tight">Top Violators</h1>

            <Tabs defaultValue="list">
                <TabsList className="grid w-full grid-cols-3">
                    <TabsTrigger value="list">Top Violators</TabsTrigger>
                    <TabsTrigger value="analysis">Violation Analysis</TabsTrigger>
                    <TabsTrigger value="appeals">Appeals Management</TabsTrigger>
                </TabsList>

                <TabsContent value="list" className="mt-4">
                    <TopViolators />
                </TabsContent>

                <TabsContent value="analysis" className="mt-4">
                    <ViolatorAnalysis />
                </TabsContent>

                <TabsContent value="appeals" className="mt-4">
                    <AppealsManagement />
                </TabsContent>
            </Tabs>
        </div>
    )
}