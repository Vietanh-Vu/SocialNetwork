"use client";

import { useState, useEffect } from "react";
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {
    Table,
    TableHeader,
    TableRow,
    TableHead,
    TableBody,
    TableCell
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import {
    Search,
    UserX,
    Filter
} from "lucide-react";
import { getTopViolators, banUser } from "@/lib/data";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { getAvatarFallback } from "@/lib/utils";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuCheckboxItem,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import {toast} from "sonner";

export function TopViolators() {
    const [topCount, setTopCount] = useState(10);
    const [violators, setViolators] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [showOnlyBanned, setShowOnlyBanned] = useState(false);
    const [includeBanned, setIncludeBanned] = useState(true);

    // Fetch initial data
    useEffect(() => {
        fetchViolators(topCount, includeBanned, showOnlyBanned).then(() => {});
    }, [topCount, includeBanned, showOnlyBanned]);

    const fetchViolators = async (limit, includeBanned, onlyBanned) => {
        setIsLoading(true);
        try {
            const response = await getTopViolators(limit, includeBanned, onlyBanned);
            if (
                response.isSuccessful &&
                response.data &&
                response.data.data.violators
            ) {
                const violatorsWithActions = response.data.data.violators.map(v => {
                    console.log(`User ${v.username} (${v.userId}): banned = ${v.banned}, type: ${typeof v.banned}`);
                    return {
                        ...v,
                        banApplied: Boolean(v.banned),
                        pendingBanChange: false
                    };
                });
                setViolators(violatorsWithActions);
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
        fetchViolators(topCount, includeBanned, showOnlyBanned).then(() => {});
    };

    const toggleBan = (userId) => {
        setViolators(prev =>
            prev.map(v =>
                v.userId === userId
                    ? {
                        ...v,
                        banApplied: !v.banApplied,
                        pendingBanChange: true
                    }
                    : v
            )
        );
    };

    const applyBan = async (userId, shouldBan) => {
        try {
            const response = await banUser(userId, shouldBan);
            if (response.isSuccessful) {
                setViolators(prev =>
                    prev.map(v =>
                        v.userId === userId
                            ? {
                                ...v,
                                pendingBanChange: false,
                                banned: shouldBan,
                                banApplied: shouldBan
                            }
                            : v
                    )
                );
                toast.success(`User ${shouldBan ? "banned" : "unbanned"} successfully`);
            } else {
                console.error("Failed to ban user:", response.message);
                // Revert the change
                setViolators(prev =>
                    prev.map(v =>
                        v.userId === userId
                            ? {
                                ...v,
                                banApplied: v.banned,
                                pendingBanChange: false
                            }
                            : v
                    )
                );
            }
        } catch (error) {
            console.error("Error banning user:", error);
            // Revert the change
            setViolators(prev =>
                prev.map(v =>
                    v.userId === userId
                        ? {
                            ...v,
                            banApplied: v.banned,
                            pendingBanChange: false
                        }
                        : v
                )
            );
        }
    };

    const saveChanges = async () => {
        const pendingChanges = violators.filter(v => v.pendingBanChange);

        for (const violator of pendingChanges) {
            await applyBan(violator.userId, violator.banApplied);
        }

        // Refresh the list
        await fetchViolators(topCount, includeBanned, showOnlyBanned);
    };

    const hasPendingChanges = violators.some(v => v.pendingBanChange);

    return (
        <div className="space-y-6">
            <Card>
                <CardHeader>
                    <CardTitle>Top Violators</CardTitle>
                    <CardDescription>
                        Users with the highest number of problematic comments
                    </CardDescription>
                </CardHeader>
                <CardContent className="space-y-6">
                    <div className="flex justify-between items-end">
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
                                        setTopCount(Number.parseInt(e.target.value) || 10)
                                    }
                                />
                            </div>
                            <Button type="submit" disabled={isLoading}>
                                <Search className="mr-2 h-4 w-4" />
                                {isLoading ? "Loading..." : "Get Violators"}
                            </Button>
                        </form>

                        <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                                <Button variant="outline" className="ml-2">
                                    <Filter className="mr-2 h-4 w-4" />
                                    Filter
                                </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="end">
                                <DropdownMenuCheckboxItem
                                    checked={includeBanned}
                                    onCheckedChange={setIncludeBanned}
                                >
                                    Include Banned Users
                                </DropdownMenuCheckboxItem>
                                <DropdownMenuCheckboxItem
                                    checked={showOnlyBanned}
                                    onCheckedChange={setShowOnlyBanned}
                                >
                                    Show Only Banned Users
                                </DropdownMenuCheckboxItem>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    </div>

                    {hasPendingChanges && (
                        <div className="flex justify-end">
                            <Button
                                onClick={saveChanges}
                                variant="default"
                                className="flex items-center gap-2"
                            >
                                Save Changes
                            </Button>
                        </div>
                    )}

                    <div className="rounded-md border">
                        <Table>
                            <TableHeader>
                                <TableRow>
                                    <TableHead>User</TableHead>
                                    <TableHead>Violations</TableHead>
                                    <TableHead className="text-right">Ban Status</TableHead>
                                </TableRow>
                            </TableHeader>
                            <TableBody>
                                {violators.map((violator) => (
                                    <TableRow key={violator.userId}>
                                        <TableCell>
                                            <div className="flex items-center gap-2">
                                                <Avatar className="h-8 w-8">
                                                    <AvatarImage
                                                        src={violator.avatar || "/placeholder.svg"}
                                                        alt={violator.username}
                                                    />
                                                    <AvatarFallback>
                                                        {getAvatarFallback(violator.username)}
                                                    </AvatarFallback>
                                                </Avatar>
                                                <div>
                                                    <div className="font-medium">{violator.username}</div>
                                                    <div className="text-xs text-muted-foreground">ID: {violator.userId}</div>
                                                </div>
                                            </div>
                                        </TableCell>
                                        <TableCell>
                                            <div className="text-sm font-semibold text-destructive">
                                                {violator.commentCount} comments
                                            </div>
                                        </TableCell>
                                        <TableCell className="text-right">
                                            <div className="flex items-center justify-end">
                                                <Switch
                                                    checked={violator.banApplied}
                                                    onCheckedChange={() => toggleBan(violator.userId)}
                                                />
                                                {violator.banApplied && (
                                                    <UserX className="ml-2 h-4 w-4 text-red-500" />
                                                )}
                                                {violator.pendingBanChange && (
                                                    <span className="ml-2 text-xs text-amber-500">Pending</span>
                                                )}
                                            </div>
                                        </TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}