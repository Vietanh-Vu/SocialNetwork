"use client"

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import {
    Chart,
    ChartArea, ChartContainer,
    ChartGrid,
    ChartLine,
    ChartTooltip, ChartTooltipContent,
    ChartXAxis,
    ChartYAxis
} from "@/app/(admin)/components/Chart";

// Mock data for weekly chart
const weeklyData = [
    { date: "2023-05-14", count: 18 },
    { date: "2023-05-15", count: 24 },
    { date: "2023-05-16", count: 19 },
    { date: "2023-05-17", count: 32 },
    { date: "2023-05-18", count: 27 },
    { date: "2023-05-19", count: 21 },
    { date: "2023-05-20", count: 15 },
]

export function WeeklyChart() {
    return (
        <Card>
            <CardHeader>
                <CardTitle>Weekly Problematic Comments</CardTitle>
                <CardDescription>Number of problematic comments in the past week</CardDescription>
            </CardHeader>
            <CardContent>
                <div className="h-[300px]">
                    <Chart
                        data={weeklyData}
                        dataKey="date"
                        categories={["count"]}
                        colors={["#2563eb"]}
                        valueFormatter={(value) => `${value} comments`}
                        startEndOnly={false}
                    >
                        <ChartContainer className="h-[300px]">
                            <ChartTooltip>
                                <ChartTooltipContent />
                            </ChartTooltip>
                            <ChartGrid horizontal vertical />
                            <ChartXAxis
                                tickCount={7}
                                tickFormat={(value) => {
                                    const date = new Date(value)
                                    return date.toLocaleDateString("en-US", { weekday: "short", month: "short", day: "numeric" })
                                }}
                            />
                            <ChartYAxis />
                            <ChartArea />
                            <ChartLine />
                        </ChartContainer>
                    </Chart>
                </div>
            </CardContent>
        </Card>
    )
}
