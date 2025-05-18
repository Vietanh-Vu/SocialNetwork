"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"

// Mock data
const dashboardData = {
    today_count: 24,
    weekly_count: 156,
    monthly_count: 587,
    total_count: 12453,
    top_violators: [
        { user_id: 1, username: "toxic_user1", comment_count: 45, avatar: "/placeholder-user.jpg" },
        { user_id: 2, username: "spammer42", comment_count: 38, avatar: "/placeholder-user.jpg" },
        { user_id: 3, username: "bad_actor99", comment_count: 27, avatar: "/placeholder-user.jpg" },
    ],
}

export function CommentsDashboard() {
    return (
        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
            <Card>
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                    <CardTitle className="text-sm font-medium">Today</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="text-2xl font-bold">{dashboardData.today_count}</div>
                    <p className="text-xs text-muted-foreground">Problematic comments today</p>
                </CardContent>
            </Card>

            <Card>
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                    <CardTitle className="text-sm font-medium">This Week</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="text-2xl font-bold">{dashboardData.weekly_count}</div>
                    <p className="text-xs text-muted-foreground">Problematic comments this week</p>
                </CardContent>
            </Card>

            <Card>
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                    <CardTitle className="text-sm font-medium">This Month</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="text-2xl font-bold">{dashboardData.monthly_count}</div>
                    <p className="text-xs text-muted-foreground">Problematic comments this month</p>
                </CardContent>
            </Card>

            <Card>
                <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                    <CardTitle className="text-sm font-medium">Total</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="text-2xl font-bold">{dashboardData.total_count}</div>
                    <p className="text-xs text-muted-foreground">Total problematic comments</p>
                </CardContent>
            </Card>

            <Card className="md:col-span-2 lg:col-span-4">
                <CardHeader>
                    <CardTitle>Top Violators</CardTitle>
                </CardHeader>
                <CardContent>
                    <div className="grid gap-4 md:grid-cols-3">
                        {dashboardData.top_violators.map((violator) => (
                            <div key={violator.user_id} className="flex items-center gap-4 rounded-lg border p-4">
                                <Avatar>
                                    <AvatarImage src={violator.avatar || "/placeholder.svg"} alt={violator.username} />
                                    <AvatarFallback>{violator.username.substring(0, 2).toUpperCase()}</AvatarFallback>
                                </Avatar>
                                <div className="space-y-1">
                                    <p className="text-sm font-medium leading-none">{violator.username}</p>
                                    <p className="text-sm text-muted-foreground">User ID: {violator.user_id}</p>
                                    <p className="text-sm font-semibold text-destructive">
                                        {violator.comment_count} problematic comments
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
