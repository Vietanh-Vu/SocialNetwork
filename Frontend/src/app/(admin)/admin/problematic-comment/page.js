import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs"
import {CommentsDashboard} from "@/app/(admin)/components/problematic-comment/CommentsDashboard";
import {CommentsList} from "@/app/(admin)/components/problematic-comment/CommentsList";
import {WeeklyChart} from "@/app/(admin)/components/problematic-comment/WeeklyChart";
import {MonthlyChart} from "@/app/(admin)/components/problematic-comment/MonthlyChart";
import {TopViolators} from "@/app/(admin)/components/problematic-comment/TopViolators";

export default function ProblematicCommentsPage() {
    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold tracking-tight">Problematic Comments</h1>

            <CommentsDashboard/>

            <Tabs defaultValue="comments">
                <TabsList className="grid w-full grid-cols-4">
                    <TabsTrigger value="comments">Comments List</TabsTrigger>
                    <TabsTrigger value="weekly">Weekly Trends</TabsTrigger>
                    <TabsTrigger value="monthly">Monthly Trends</TabsTrigger>
                    <TabsTrigger value="violators">Top Violators</TabsTrigger>
                </TabsList>

                <TabsContent value="comments" className="mt-4">
                    <CommentsList/>
                </TabsContent>

                <TabsContent value="weekly" className="mt-4">
                    <WeeklyChart/>
                </TabsContent>

                <TabsContent value="monthly" className="mt-4">
                    <MonthlyChart/>
                </TabsContent>

                <TabsContent value="violators" className="mt-4">
                    <TopViolators/>
                </TabsContent>
            </Tabs>
        </div>
    )
}
