"use client";

import {useState} from "react";
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {Search, User} from "lucide-react";
import {getUserViolationStats} from "@/lib/data";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {getAvatarFallback} from "@/lib/utils";
import {BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer} from 'recharts';

export function ViolatorStats() {
    const [userId, setUserId] = useState("");
    const [violatorData, setViolatorData] = useState(null);
    const [monthlyStats, setMonthlyStats] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const fetchViolatorStats = async (e) => {
        e.preventDefault();
        if (!userId) return;

        setIsLoading(true);
        try {
            const response = await getUserViolationStats(userId);

            if (response.isSuccessful && response.data) {
                setViolatorData({
                    userId: parseInt(userId),
                    username: response.data.username || "User #" + userId,
                    avatar: response.data.avatar,
                    commentCount: response.data.totalCount
                });

                const stats = response.data.monthlyStats.map(stat => ({
                    name: `${stat.month}/${stat.year}`,
                    count: stat.count
                }));

                setMonthlyStats(stats);
            } else {
                console.error("Failed to fetch violation stats:", response.message);
                setViolatorData(null);
                setMonthlyStats([]);
            }
        } catch (error) {
            console.error("Error fetching violator stats:", error);
            setViolatorData(null);
            setMonthlyStats([]);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="space-y-6">
            <Card>
                <CardHeader>
                    <CardTitle>Violation Statistics</CardTitle>
                    <CardDescription>
                        View statistics of problematic comments for a specific user over time
                    </CardDescription>
                </CardHeader>
                <CardContent className="space-y-6">
                    <form onSubmit={fetchViolatorStats} className="flex items-end gap-4">
                        <div className="grid w-full max-w-sm items-center gap-1.5">
                            <Label htmlFor="user-id-stats">User ID</Label>
                            <Input type="text"
                                   id="user-id-stats"
                                   placeholder="Enter user ID"
                                   value={userId}
                                   onChange={(e) => setUserId(e.target.value)}
                            />
                        </div>
                        <Button type="submit" disabled={isLoading || !userId}>
                            <Search className="mr-2 h-4 w-4"/>
                            {isLoading ? "Loading..." : "Search"}
                        </Button>
                    </form>

                    {violatorData && (
                        <div className="flex items-center gap-4 p-4 border rounded-lg">
                            <Avatar className="h-12 w-12">
                                <AvatarImage
                                    src={violatorData.avatar || "/placeholder.svg"}
                                    alt={violatorData.username}
                                />
                                <AvatarFallback>
                                    {getAvatarFallback(violatorData.username)}
                                </AvatarFallback>
                            </Avatar>
                            <div>
                                <h3 className="text-lg font-semibold">{violatorData.username}</h3>
                                <p className="text-sm text-muted-foreground">
                                    User ID: {violatorData.userId}
                                </p>
                                <p className="text-sm font-medium text-destructive">
                                    {violatorData.commentCount} problematic comments total
                                </p>
                            </div>
                        </div>
                    )}

                    {monthlyStats.length > 0 ? (
                        <div className="w-full h-80 border rounded-lg p-4">
                            <ResponsiveContainer width="100%" height="100%">
                                <BarChart data={monthlyStats}>
                                    <CartesianGrid strokeDasharray="3 3"/>
                                    <XAxis dataKey="name"/>
                                    <YAxis allowDecimals={false}/>
                                    <Tooltip
                                        formatter={(value) => [`${value} comments`, 'Violations']}
                                        labelFormatter={(label) => `Period: ${label}`}
                                    />
                                    <Bar
                                        dataKey="count"
                                        name="Violations"
                                        fill="#ef4444"
                                        radius={[4, 4, 0, 0]}
                                    />
                                </BarChart>
                            </ResponsiveContainer>
                        </div>
                    ) : violatorData ? (
                        <div className="flex flex-col items-center justify-center p-8 border rounded-lg">
                            <p className="text-center text-muted-foreground">No statistics available</p>
                        </div>
                    ) : userId && !isLoading ? (
                        <div className="flex flex-col items-center justify-center p-8 border rounded-lg">
                            <User className="h-12 w-12 text-muted-foreground mb-4"/>
                            <p className="text-center text-muted-foreground">No user found with that ID</p>
                        </div>
                    ) : null}
                </CardContent>
            </Card>
        </div>
    );
}