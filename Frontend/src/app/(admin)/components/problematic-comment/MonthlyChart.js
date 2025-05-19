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

import { getMonthlyStats } from "@/lib/data"

const chartConfig = {
    violations: {
        label: "Violations",
        color: "hsl(var(--foreground))",
    }
}

// Function to convert month number to month name
const getMonthName = (month) => {
    const monthNames = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ]
    return monthNames[month - 1]
}

export function MonthlyChart() {
    const [chartData, setChartData] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const [trend, setTrend] = useState({ percentage: 0, isUp: true })

    useEffect(() => {
        const fetchData = async () => {
            setIsLoading(true)
            try {
                const response = await getMonthlyStats()
                if (response.isSuccessful && response.data) {
                    // Convert API data to chart format
                    const formattedData = response.data.data.monthlyStats.map(item => ({
                        month: `${getMonthName(item.month)} ${item.year}`,
                        monthShort: getMonthName(item.month).substring(0, 3),
                        violations: item.count,
                        fullData: `${getMonthName(item.month)} ${item.year}: ${item.count.toLocaleString()}`
                    }))

                    setChartData(formattedData)

                    // Calculate trend
                    if (formattedData.length >= 2) {
                        const lastMonth = formattedData[formattedData.length - 1].violations
                        const previousMonth = formattedData[formattedData.length - 2].violations
                        const diff = lastMonth - previousMonth
                        const percentage = previousMonth !== 0
                            ? Math.abs((diff / previousMonth) * 100).toFixed(1)
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
                <CardTitle>Monthly Violations Chart</CardTitle>
                <CardDescription>Problematic comments statistics (12 months)</CardDescription>
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
                                dataKey="monthShort"
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
                                                            Month
                                                        </span>
                                                        <span className="font-bold text-foreground">
                                                            {payload[0].payload.month}
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
                            <span className="text-destructive">Increased by {trend.percentage}% from previous month</span>
                            <TrendingUp className="h-4 w-4 text-destructive" />
                        </>
                    ) : (
                        <>
                            <span className="text-green-500">Decreased by {trend.percentage}% from previous month</span>
                            <TrendingDown className="h-4 w-4 text-green-500" />
                        </>
                    )}
                </div>
                <div className="leading-none text-muted-foreground">
                    Showing monthly problematic comments count over the past year
                </div>
            </CardFooter>
        </Card>
    )
}