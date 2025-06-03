import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ViolatorHistory } from "@/app/(admin)/components/top-violators/ViolatorHistory";
import { ViolatorStats } from "@/app/(admin)/components/top-violators/ViolatorStats";
import {TopViolators} from "@/app/(admin)/components/top-violators/TopViolators";

export default function TopViolatorsPage() {
    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold tracking-tight">Top Violators</h1>

            <Tabs defaultValue="list">
                <TabsList className="grid w-full grid-cols-3">
                    <TabsTrigger value="list">Top Violators</TabsTrigger>
                    <TabsTrigger value="history">Violation History</TabsTrigger>
                    <TabsTrigger value="stats">Violation Statistics</TabsTrigger>
                </TabsList>

                <TabsContent value="list" className="mt-4">
                    <TopViolators />
                </TabsContent>

                <TabsContent value="history" className="mt-4">
                    <ViolatorHistory />
                </TabsContent>

                <TabsContent value="stats" className="mt-4">
                    <ViolatorStats />
                </TabsContent>
            </Tabs>
        </div>
    )
}