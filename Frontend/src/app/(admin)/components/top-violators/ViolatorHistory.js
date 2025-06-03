"use client";

import { useState } from "react";
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
import { Search, User, AlertCircle } from "lucide-react";
import { getUserProblematicComments } from "@/lib/data";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { getAvatarFallback } from "@/lib/utils";
import { format } from "date-fns";

export function ViolatorHistory() {
    const [userId, setUserId] = useState("");
    const [violatorData, setViolatorData] = useState(null);
    const [comments, setComments] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const fetchViolatorHistory = async (e) => {
        e.preventDefault();
        if (!userId) return;

        setIsLoading(true);
        try {
            const response = await getUserProblematicComments(userId);

            if (response.isSuccessful && response.data) {
                if (response.data.content.length > 0) {
                    const firstComment = response.data.content[0];
                    setViolatorData({
                        userId: parseInt(userId),
                        username: firstComment.username,
                        avatar: firstComment.userAvatar,
                        commentCount: response.data.totalElements
                    });
                    setComments(response.data.content);
                } else {
                    // User exists but has no comments
                    setViolatorData({
                        userId: parseInt(userId),
                        username: "User #" + userId,
                        avatar: null,
                        commentCount: 0
                    });
                    setComments([]);
                }
            } else {
                console.error("Failed to fetch problematic comments:", response.message);
                setViolatorData(null);
                setComments([]);
            }
        } catch (error) {
            console.error("Error fetching violator history:", error);
            setViolatorData(null);
            setComments([]);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="space-y-6">
            <Card>
                <CardHeader>
                    <CardTitle>Violation History</CardTitle>
                    <CardDescription>
                        View the history of problematic comments for a specific user
                    </CardDescription>
                </CardHeader>
                <CardContent className="space-y-6">
                    <form onSubmit={fetchViolatorHistory} className="flex items-end gap-4">
                        <div className="grid w-full max-w-sm items-center gap-1.5">
                            <Label htmlFor="user-id">User ID</Label>
                            <Input
                                type="text"
                                id="user-id"
                                placeholder="Enter user ID"
                                value={userId}
                                onChange={(e) => setUserId(e.target.value)}
                            />
                        </div>
                        <Button type="submit" disabled={isLoading || !userId}>
                            <Search className="mr-2 h-4 w-4" />
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
                                    {violatorData.commentCount} problematic comments
                                </p>
                            </div>
                        </div>
                    )}

                    {comments.length > 0 ? (
                        <div className="rounded-md border">
                            <Table>
                                <TableHeader>
                                    <TableRow>
                                        <TableHead>Date</TableHead>
                                        <TableHead>Content</TableHead>
                                        <TableHead className="text-right">Probability</TableHead>
                                    </TableRow>
                                </TableHeader>
                                <TableBody>
                                    {comments.map((comment) => (
                                        <TableRow key={comment.id}>
                                            <TableCell className="whitespace-nowrap">
                                                {format(new Date(comment.createdAt), 'dd/MM/yyyy HH:mm')}
                                            </TableCell>
                                            <TableCell>{comment.content}</TableCell>
                                            <TableCell className="text-right">
                                                <div className="inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-semibold bg-red-100 text-red-800">
                                                    {(comment.spamProbability * 100).toFixed(1)}%
                                                </div>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </div>
                    ) : violatorData ? (
                        <div className="flex flex-col items-center justify-center p-8 border rounded-lg">
                            <AlertCircle className="h-12 w-12 text-muted-foreground mb-4" />
                            <p className="text-center text-muted-foreground">No problematic comments found</p>
                        </div>
                    ) : userId && !isLoading ? (
                        <div className="flex flex-col items-center justify-center p-8 border rounded-lg">
                            <User className="h-12 w-12 text-muted-foreground mb-4" />
                            <p className="text-center text-muted-foreground">No user found with that ID</p>
                        </div>
                    ) : null}
                </CardContent>
            </Card>
        </div>
    );
}