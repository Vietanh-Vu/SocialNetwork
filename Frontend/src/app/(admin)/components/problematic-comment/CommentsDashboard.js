"use client"

import { useEffect, useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { getDashboardData } from "@/lib/data"
import { Loader2 } from "lucide-react"
import {toast} from "sonner";
import {getAvatarFallback} from "@/lib/utils";

export function CommentsDashboard() {
    const [dashboardData, setDashboardData] = useState(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await getDashboardData()
                if (response.isSuccessful) {
                    setDashboardData(response.data.data)
                } else {
                    setError(response.message || "Error fetching data")
                    toast.error(response.message || "Error fetching data")
                }
            } catch (err) {
                setError("Error fetching data")
                toast.error("Error fetching data")
                console.error(err)
            } finally {
                setLoading(false)
            }
        }

        fetchData().then(r => {})
    }, [])

    if (loading) {
        return (
            <div className="flex justify-center items-center h-40">
               Loading...
            </div>
        )
    }

    if (error || !dashboardData) {
        return (
            <div className="text-center p-4 text-destructive">
                {error || "Không thể tải dữ liệu"}
            </div>
        )
    }

    return (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
            <Card>
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                    <CardTitle className="text-sm font-medium">Today</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="text-2xl font-bold">{dashboardData.todayCount.toLocaleString()}</div>
                    <p className="text-xs text-muted-foreground">Problematic comments today</p>
                </CardContent>
            </Card>

            <Card>
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                    <CardTitle className="text-sm font-medium">This Week</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="text-2xl font-bold">{dashboardData.weeklyCount.toLocaleString()}</div>
                    <p className="text-xs text-muted-foreground">Problematic comments this week</p>
                </CardContent>
            </Card>

            <Card>
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                    <CardTitle className="text-sm font-medium">This Month</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="text-2xl font-bold">{dashboardData.monthlyCount.toLocaleString()}</div>
                    <p className="text-xs text-muted-foreground">Problematic comments this month</p>
                </CardContent>
            </Card>

            <Card>
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                    <CardTitle className="text-sm font-medium">Total</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="text-2xl font-bold">{dashboardData.totalCount.toLocaleString()}</div>
                    <p className="text-xs text-muted-foreground">Total problematic comments</p>
                </CardContent>
            </Card>

            <Card className="md:col-span-2 lg:col-span-4">
                <CardHeader>
                    <CardTitle>Top Violators</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="grid gap-4 md:grid-cols-3 lg:grid-cols-5">
                        {dashboardData.topViolators.map((violator) => (
                            <div key={violator.userId} className="flex items-center gap-4 rounded-lg border p-4">
                                <Avatar>
                                    <AvatarImage src={violator.avatar || "/placeholder.svg"} alt={violator.username} />
                                    <AvatarFallback>{getAvatarFallback(violator.username)}</AvatarFallback>
                                </Avatar>
                                <div className="space-y-1">
                                    <p className="text-sm font-medium leading-none">{violator.username}</p>
                                    <p className="text-sm text-muted-foreground">User ID: {violator.userId}</p>
                                    <p className="text-sm font-semibold text-destructive">
                                        {violator.commentCount.toLocaleString()} problematic comments
                                    </p>
                                </div>
                            </div>
                        ))}
                    </div>
                </CardContent>
            </Card>
        </div>
    )
}