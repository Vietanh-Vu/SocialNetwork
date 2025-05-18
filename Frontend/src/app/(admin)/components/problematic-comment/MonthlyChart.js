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

// Mock data for monthly chart
const monthlyData = [
    { month: "2023-06", year: 2023, count: 487 },
    { month: "2023-07", year: 2023, count: 520 },
    { month: "2023-08", year: 2023, count: 435 },
    { month: "2023-09", year: 2023, count: 562 },
    { month: "2023-10", year: 2023, count: 498 },
    { month: "2023-11", year: 2023, count: 534 },
    { month: "2023-12", year: 2023, count: 612 },
    { month: "2024-01", year: 2024, count: 587 },
    { month: "2024-02", year: 2024, count: 543 },
    { month: "2024-03", year: 2024, count: 501 },
    { month: "2024-04", year: 2024, count: 575 },
    { month: "2024-05", year: 2024, count: 587 },
]

export function MonthlyChart() {
    return (
        <Card>
            <CardHeader>
                <CardTitle>Monthly Problematic Comments</CardTitle>
                <CardDescription>Number of problematic comments over the past 12 months</CardDescription>
            </CardHeader>
            <CardContent>
                <div className="h-[300px]">
                    <Chart
                        data={monthlyData}
                        dataKey="month"
                        categories={["count"]}
                        colors={["#8b5cf6"]}
                        valueFormatter={(value) => `${value} comments`}
                        startEndOnly={false}
                    >
                        <ChartContainer className="h-[300px]">
                            <ChartTooltip>
                                <ChartTooltipContent />
                            </ChartTooltip>
                            <ChartGrid horizontal vertical />
                            <ChartXAxis
                                tickCount={12}
                                tickFormat={(value) => {
                                    const date = new Date(value)
                                    return date.toLocaleDateString("en-US", { month: "short", year: "2-digit" })
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
