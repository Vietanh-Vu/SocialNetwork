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
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { getTopViolators } from "@/lib/data";
import { AlertTriangle, Shield, UserX } from "lucide-react";

export function ViolatorsActions() {
    const [topCount, setTopCount] = useState(5);
    const [violators, setViolators] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [actionType, setActionType] = useState("warning");

    // Fetch initial data
    useEffect(() => {
        fetchViolators(topCount).then((r) => {});
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
                // Add action status to each violator
                const violatorsWithActions = response.data.data.violators.map(v => ({
                    ...v,
                    actionApplied: false,
                    actionType: "warning"
                }));
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
        fetchViolators(topCount).then((r) => {});
    };

    const toggleAction = (userId) => {
        setViolators(prev =>
            prev.map(v =>
                v.userId === userId
                    ? { ...v, actionApplied: !v.actionApplied }
                    : v
            )
        );
    };

    const setAction = (userId, type) => {
        setViolators(prev =>
            prev.map(v =>
                v.userId === userId
                    ? { ...v, actionType: type }
                    : v
            )
        );
    };

    const applyBulkAction = () => {
        setViolators(prev =>
            prev.map(v => ({ ...v, actionType }))
        );
    };

    const getActionIcon = (actionType) => {
        switch(actionType) {
            case "warning": return <AlertTriangle className="h-4 w-4 text-yellow-500" />;
            case "restrict": return <Shield className="h-4 w-4 text-orange-500" />;
            case "ban": return <UserX className="h-4 w-4 text-red-500" />;
            default: return <AlertTriangle className="h-4 w-4 text-yellow-500" />;
        }
    };

    return (
        <div className="space-y-6">
            <Card>
                <CardHeader>
                    <CardTitle>Manage Violators</CardTitle>
                    <CardDescription>
                        Apply actions to users who violate community guidelines
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
                            {isLoading ? "Loading..." : "Get Violators"}
                        </Button>
                    </form>

                    <div className="border rounded-lg p-4">
                        <h3 className="text-md font-medium mb-2">Bulk Actions</h3>
                        <div className="flex items-end gap-4">
                            <div className="grid w-full max-w-sm items-center gap-1.5">
                                <Label htmlFor="action-type">Action Type</Label>
                                <Select value={actionType} onValueChange={setActionType}>
                                    <SelectTrigger>
                                        <SelectValue />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectItem value="warning">Warning</SelectItem>
                                        <SelectItem value="restrict">Restrict</SelectItem>
                                        <SelectItem value="ban">Ban</SelectItem>
                                    </SelectContent>
                                </Select>
                            </div>
                            <Button onClick={applyBulkAction}>
                                Apply to All
                            </Button>
                        </div>
                    </div>

                    <Table>
                        <TableHeader>
                            <TableRow>
                                <TableHead>User</TableHead>
                                <TableHead>Violations</TableHead>
                                <TableHead>Action Type</TableHead>
                                <TableHead>Apply Action</TableHead>
                            </TableRow>
                        </TableHeader>
                        <TableBody>
                            {violators.map((violator) => (
                                <TableRow key={violator.userId}>
                                    <TableCell>
                                        <div className="font-medium">{violator.username}</div>
                                        <div className="text-sm text-muted-foreground">ID: {violator.userId}</div>
                                    </TableCell>
                                    <TableCell>
                                        <div className="text-sm font-semibold text-destructive">
                                            {violator.commentCount} comments
                                        </div>
                                    </TableCell>
                                    <TableCell>
                                        <Select
                                            value={violator.actionType}
                                            onValueChange={(value) => setAction(violator.userId, value)}
                                            disabled={!violator.actionApplied}
                                        >
                                            <SelectTrigger className="w-32">
                                                <SelectValue />
                                            </SelectTrigger>
                                            <SelectContent>
                                                <SelectItem value="warning">
                                                    <div className="flex items-center gap-2">
                                                        <AlertTriangle className="h-4 w-4 text-yellow-500" />
                                                        <span>Warning</span>
                                                    </div>
                                                </SelectItem>
                                                <SelectItem value="restrict">
                                                    <div className="flex items-center gap-2">
                                                        <Shield className="h-4 w-4 text-orange-500" />
                                                        <span>Restrict</span>
                                                    </div>
                                                </SelectItem>
                                                <SelectItem value="ban">
                                                    <div className="flex items-center gap-2">
                                                        <UserX className="h-4 w-4 text-red-500" />
                                                        <span>Ban</span>
                                                    </div>
                                                </SelectItem>
                                            </SelectContent>
                                        </Select>
                                    </TableCell>
                                    <TableCell>
                                        <div className="flex items-center">
                                            <Switch
                                                checked={violator.actionApplied}
                                                onCheckedChange={() => toggleAction(violator.userId)}
                                            />
                                            {violator.actionApplied && (
                                                <div className="ml-2">
                                                    {getActionIcon(violator.actionType)}
                                                </div>
                                            )}
                                        </div>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </CardContent>
            </Card>
        </div>
    );
}