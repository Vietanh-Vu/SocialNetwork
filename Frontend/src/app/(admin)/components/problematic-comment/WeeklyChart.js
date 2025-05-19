"use client"

import { useState, useEffect } from "react"
import { TrendingUp, TrendingDown } from "lucide-react"
import { CartesianGrid, Line, LineChart, XAxis, YAxis, Tooltip, ResponsiveContainer } from "recharts"

import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"

import {
    ChartContainer,
    ChartTooltip,
    ChartTooltipContent,
} from "@/components/ui/chart"

import { getWeeklyStats } from "@/lib/data"

const chartConfig = {
    violations: {
        label: "Violations",
        color: "hsl(var(--foreground))",
    }
}

// Format date to display as "MMM DD"
const formatDate = (dateString) => {
    const date = new Date(dateString)
    return date.toLocaleDateString("en-US", { month: 'short', day: 'numeric' })
}

export function WeeklyChart() {
    const [chartData, setChartData] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [trend, setTrend] = useState({ percentage: 0, isUp: true })

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true)
            try {
                const response = await getWeeklyStats()
                if (response.isSuccessful && response.data) {
                    // Convert API data to chart format
                    const formattedData = response.data.data.weeklyStats.map(item => ({
                        week: `${formatDate(item.startDate)} - ${formatDate(item.endDate)}`,
                        weekShort: formatDate(item.startDate),
                        violations: item.count,
                        startDate: item.startDate,
                        endDate: item.endDate
                    }))

                    setChartData(formattedData)

                    // Calculate trend
                    if (formattedData.length >= 2) {
                        const lastWeek = formattedData[formattedData.length - 1].violations
                        const previousWeek = formattedData[formattedData.length - 2].violations
                        const diff = lastWeek - previousWeek
                        const percentage = previousWeek !== 0
                            ? Math.abs((diff / previousWeek) * 100).toFixed(1)
                            : 0

                        setTrend({
                            percentage,
                            isUp: diff > 0
                        })
                    }
                }
            } catch (error) {
                console.error("Error loading chart data:", error)
            } finally {
                setIsLoading(false)
            }
        }

        fetchData()
    }, [])

    return (
        <Card>
            <CardHeader>
                <CardTitle>Weekly Violations Chart</CardTitle>
                <CardDescription>Problematic comments statistics (7 weeks)</CardDescription>
            </CardHeader>
            <CardContent>
                {isLoading ? (
                    <div className="flex justify-center items-center h-64">
                        <p>Loading data...</p>
                    </div>
                ) : (
                    <ChartContainer config={chartConfig}>
                        <LineChart
                            accessibilityLayer
                            data={chartData}
                            margin={{ left: 12, right: 12, top: 12, bottom: 12 }}
                        >
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis
                                dataKey="weekShort"
                                tickLine={false}
                                axisLine={false}
                                tickMargin={8}
                            />
                            <YAxis
                                tickLine={false}
                                axisLine={false}
                                tickMargin={8}
                                tickFormatter={(value) => value >= 1000 ? `${(value / 1000).toFixed(0)}k` : value}
                            />
                            <ChartTooltip
                                content={({ active, payload }) => {
                                    if (active && payload && payload.length) {
                                        return (
                                            <div className="rounded-lg border bg-background p-2 shadow-sm">
                                                <div className="grid grid-cols-2 gap-2">
                                                    <div className="flex flex-col">
                                                        <span className="text-[0.70rem] uppercase text-muted-foreground">
                                                            Week
                                                        </span>
                                                        <span className="font-bold text-foreground">
                                                            {payload[0].payload.week}
                                                        </span>
                                                    </div>
                                                    <div className="flex flex-col">
                                                        <span className="text-[0.70rem] uppercase text-muted-foreground">
                                                            Violations
                                                        </span>
                                                        <span className="font-bold text-foreground">
                                                            {payload[0].value.toLocaleString()}
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        )
                                    }
                                    return null
                                }}
                            />
                            <Line
                                dataKey="violations"
                                type="monotone"
                                stroke="var(--color-violations)"
                                strokeWidth={2}
                                dot={{
                                    stroke: 'var(--color-violations)',
                                    strokeWidth: 2,
                                    fill: 'hsl(var(--background))',
                                    r: 4
                                }}
                                activeDot={{
                                    stroke: 'var(--color-violations)',
                                    strokeWidth: 2,
                                    fill: 'hsl(var(--background))',
                                    r: 6
                                }}
                            />
                        </LineChart>
                    </ChartContainer>
                )}
            </CardContent>
            <CardFooter className="flex-col items-start gap-2 text-sm">
                <div className="flex gap-2 font-medium leading-none">
                    {trend.isUp ? (
                        <>
                            <span className="text-destructive">Increased by {trend.percentage}% from previous week</span>
                            <TrendingUp className="h-4 w-4 text-destructive" />
                        </>
                    ) : (
                        <>
                            <span className="text-green-500">Decreased by {trend.percentage}% from previous week</span>
                            <TrendingDown className="h-4 w-4 text-green-500" />
                        </>
                    )}
                </div>
                <div className="leading-none text-muted-foreground">
                    Showing weekly problematic comments count
                </div>
            </CardFooter>
        </Card>
    )
}