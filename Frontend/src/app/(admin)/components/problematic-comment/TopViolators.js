"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Search } from "lucide-react"

// Mock data
const allViolators = [
    { user_id: 1, username: "toxic_user1", comment_count: 45, avatar: "/placeholder-user.jpg" },
    { user_id: 2, username: "spammer42", comment_count: 38, avatar: "/placeholder-user.jpg" },
    { user_id: 3, username: "bad_actor99", comment_count: 27, avatar: "/placeholder-user.jpg" },
    { user_id: 4, username: "angry_user", comment_count: 24, avatar: "/placeholder-user.jpg" },
    { user_id: 5, username: "bot_account", comment_count: 22, avatar: "/placeholder-user.jpg" },
    { user_id: 6, username: "troll_master", comment_count: 19, avatar: "/placeholder-user.jpg" },
    { user_id: 7, username: "spam_king", comment_count: 17, avatar: "/placeholder-user.jpg" },
    { user_id: 8, username: "rule_breaker", comment_count: 15, avatar: "/placeholder-user.jpg" },
    { user_id: 9, username: "fake_profile", comment_count: 14, avatar: "/placeholder-user.jpg" },
    { user_id: 10, username: "content_violator", comment_count: 12, avatar: "/placeholder-user.jpg" },
]

export function TopViolators() {
    const [topCount, setTopCount] = useState(5)
    const [displayedViolators, setDisplayedViolators] = useState(allViolators.slice(0, 5))

    const handleSearch = (e) => {
        e.preventDefault()
        setDisplayedViolators(allViolators.slice(0, topCount))
    }

    return (
        <Card>
            <CardHeader>
                <CardTitle>Top Violators</CardTitle>
                <CardDescription>Users with the highest number of problematic comments</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
                <form onSubmit={handleSearch} className="flex items-end gap-4">
                    <div className="grid w-full max-w-sm items-center gap-1.5">
                        <Label htmlFor="top-count">Number of Top Violators</Label>
                        <Input
                            type="number"
                            id="top-count"
                            placeholder="Enter number"
                            min={1}
                            max={allViolators.length}
                            value={topCount}
                            onChange={(e) => setTopCount(Number.parseInt(e.target.value) || 5)}
                        />
                    </div>
                    <Button type="submit">
                        <Search className="mr-2 h-4 w-4" />
                        Get Top Violators
                    </Button>
                </form>

                <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                    {displayedViolators.map((violator) => (
                        <div key={violator.user_id} className="flex items-center gap-4 rounded-lg border p-4">
                            <Avatar className="h-12 w-12">
                                <AvatarImage src={violator.avatar || "/placeholder.svg"} alt={violator.username} />
                                <AvatarFallback>{violator.username.substring(0, 2).toUpperCase()}</AvatarFallback>
                            </Avatar>
                            <div className="space-y-1">
                                <p className="font-medium leading-none">{violator.username}</p>
                                <p className="text-sm text-muted-foreground">User ID: {violator.user_id}</p>
                                <p className="text-sm font-semibold text-destructive">{violator.comment_count} problematic comments</p>
                            </div>
                        </div>
                    ))}
                </div>
            </CardContent>
        </Card>
    )
}
