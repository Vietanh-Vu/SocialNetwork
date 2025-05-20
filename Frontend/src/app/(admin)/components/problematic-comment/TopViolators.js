"use client";

import {useState, useEffect} from "react";
import {Button} from "@/components/ui/button";
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {Search} from "lucide-react";
import {getTopViolators} from "@/lib/data";
import {getAvatarFallback} from "@/lib/utils";

export function TopViolators() {
    const [topCount, setTopCount] = useState(5);
    const [displayedViolators, setDisplayedViolators] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    // Fetch initial data
    useEffect(() => {
        fetchViolators(topCount).then((r) => {
        });
    }, [topCount]);

    const fetchViolators = async (limit) => {
        setIsLoading(true);
        try {
            const response = await getTopViolators(limit);
            if (
                response.isSuccessful &&
                response.data &&
                response.data.data.violators
            ) {
                setDisplayedViolators(response.data.data.violators);
            } else {
                console.error("Failed to fetch top violators:", response.message);
            }
        } catch (error) {
            console.error("Error fetching top violators:", error);
        } finally {
            setIsLoading(false);
        }
    };

    const handleSearch = (e) => {
        e.preventDefault();
        fetchViolators(topCount).then((r) => {
        });
    };

    return (
        <Card>
            <CardHeader>
                <CardTitle>Top Violators</CardTitle>
                <CardDescription>
                    Users with the highest number of problematic comments
                </CardDescription>
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
                            max={100}
                            value={topCount}
                            onChange={(e) =>
                                setTopCount(Number.parseInt(e.target.value) || 5)
                            }
                        />
                    </div>
                    <Button type="submit" disabled={isLoading}>
                        <Search className="mr-2 h-4 w-4"/>
                        {isLoading ? "Loading..." : "Get Top Violators"}
                    </Button>
                </form>

                <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
                    {displayedViolators.map((violator) => (
                        <div
                            key={violator.userId}
                            className="flex items-center gap-4 rounded-lg border p-4"
                        >
                            <Avatar className="h-12 w-12">
                                <AvatarImage
                                    src={violator.avatar || "/placeholder.svg"}
                                    alt={violator.username}
                                />
                                <AvatarFallback>
                                    {getAvatarFallback(violator.username)}
                                </AvatarFallback>
                            </Avatar>
                            <div className="space-y-1">
                                <p className="font-medium leading-none">{violator.username}</p>
                                <p className="text-sm text-muted-foreground">
                                    User ID: {violator.userId}
                                </p>
                                <p className="text-sm font-semibold text-destructive">
                                    {violator.commentCount} problematic comments
                                </p>
                            </div>
                        </div>
                    ))}
                </div>
            </CardContent>
        </Card>
    );
}
