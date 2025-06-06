"use client";

import {useCallback, useState} from "react";
import {Card, CardContent, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {AlertCircle, Search, User} from "lucide-react";
import {getUserProblematicComments, getUserWeeklyViolationStats} from "@/lib/data";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {cn, getAvatarFallback} from "@/lib/utils";
import {CartesianGrid, Line, LineChart, Tooltip, XAxis, YAxis} from 'recharts';
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import {
    Pagination,
    PaginationContent,
    PaginationEllipsis,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
} from "@/components/ui/pagination";

import {ChartContainer} from "@/components/ui/chart"

const chartConfig = {
    violations: {
        label: "Violations",
        color: "hsl(var(--foreground))",
    }
}

export function ViolatorAnalysis() {
    const [userId, setUserId] = useState("");
    const [violatorData, setViolatorData] = useState(null);
    const [comments, setComments] = useState([]);
    const [weeklyStats, setWeeklyStats] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    // Pagination state
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [totalElements, setTotalElements] = useState(0);
    const pageSize = 10;

    // Format date để hiển thị trong chart
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString("en-US", {month: 'short', day: 'numeric'});
    };

    const fetchViolatorData = useCallback(async (currentPage = 1) => {
        if (!userId) return;

        setIsLoading(true);
        try {
            // Fetch comments và weekly stats song song
            const [commentsResponse, statsResponse] = await Promise.all([
                getUserProblematicComments(userId, currentPage, pageSize),
                getUserWeeklyViolationStats(userId)
            ]);

            // Xử lý dữ liệu comments
            if (commentsResponse.isSuccessful && commentsResponse.data) {
                // Set pagination data
                setTotalPages(commentsResponse.data.totalPages || 1);
                setTotalElements(commentsResponse.data.totalElements || 0);

                if (commentsResponse.data.content.length > 0) {
                    const firstComment = commentsResponse.data.content[0];
                    setViolatorData({
                        userId: parseInt(userId),
                        username: firstComment.username,
                        avatar: firstComment.userAvatar,
                        commentCount: commentsResponse.data.totalElements
                    });
                    setComments(commentsResponse.data.content);
                } else {
                    setViolatorData({
                        userId: parseInt(userId),
                        username: "User #" + userId,
                        avatar: null,
                        commentCount: 0
                    });
                    setComments([]);
                }
            } else {
                console.error("Failed to fetch problematic comments:", commentsResponse.message);
                setViolatorData(null);
                setComments([]);
            }

            // Xử lý dữ liệu weekly stats - chỉ fetch một lần khi search user mới
            if (currentPage === 1) {
                if (statsResponse.isSuccessful && statsResponse.data) {
                    const formattedWeeklyData = statsResponse.data.weeklyStats.map(item => ({
                        week: `${formatDate(item.startDate)} - ${formatDate(item.endDate)}`,
                        weekShort: formatDate(item.startDate),
                        violations: item.count,
                        startDate: item.startDate,
                        endDate: item.endDate
                    }));
                    setWeeklyStats(formattedWeeklyData);
                } else {
                    console.error("Failed to fetch weekly stats:", statsResponse.message);
                    setWeeklyStats([]);
                }
            }

        } catch (error) {
            console.error("Error fetching violator data:", error);
            setViolatorData(null);
            setComments([]);
            setWeeklyStats([]);
        } finally {
            setIsLoading(false);
        }
    }, [userId, pageSize]);

    const handleSearch = async (e) => {
        e.preventDefault();
        if (!userId) return;
        setPage(1);
        await fetchViolatorData(1);
    };

    const handlePageChange = async (newPage) => {
        setPage(newPage);
        await fetchViolatorData(newPage);
    };

    return (
        <div className="space-y-6">
            <Card>
                <CardHeader>
                    <CardTitle>Violator Analysis</CardTitle>
                    <CardDescription>
                        {'View a specific user\'s violation history and statistics'}
                    </CardDescription>
                </CardHeader>
                <CardContent className="space-y-6">
                    <form onSubmit={handleSearch} className="flex items-end gap-4">
                        <div className="grid w-full max-w-sm items-center gap-1.5">
                            <Label htmlFor="user-id">User ID</Label>
                            <Input
                                type="text"
                                id="user-id"
                                placeholder="Input User ID"
                                value={userId}
                                onChange={(e) => setUserId(e.target.value)}
                            />
                        </div>
                        <Button type="submit" disabled={isLoading || !userId}>
                            <Search className="mr-2 h-4 w-4"/>
                            {isLoading ? "Loading..." : "Search"}
                        </Button>
                    </form>

                    {/*{violatorData && (*/}
                    {/*    <div className="flex items-center gap-4 p-4 border rounded-lg">*/}
                    {/*        <Avatar className="h-12 w-12">*/}
                    {/*            <AvatarImage*/}
                    {/*                src={violatorData.avatar || "/placeholder.svg"}*/}
                    {/*                alt={violatorData.username}*/}
                    {/*            />*/}
                    {/*            <AvatarFallback>*/}
                    {/*                {getAvatarFallback(violatorData.username)}*/}
                    {/*            </AvatarFallback>*/}
                    {/*        </Avatar>*/}
                    {/*        <div>*/}
                    {/*            <h3 className="text-lg font-semibold">{violatorData.username}</h3>*/}
                    {/*            <p className="text-sm text-muted-foreground">*/}
                    {/*                User ID: {violatorData.userId}*/}
                    {/*            </p>*/}
                    {/*            <p className="text-sm font-medium text-destructive">*/}
                    {/*                {violatorData.commentCount} problematic comments*/}
                    {/*            </p>*/}
                    {/*        </div>*/}
                    {/*    </div>*/}
                    {/*)}*/}

                    {/* Biểu đồ thống kê tuần */}
                    {weeklyStats.length > 0 && (
                        <Card>
                            <CardHeader>
                                <CardTitle>Weekly Violation Statistics</CardTitle>
                                <CardDescription>
                                    Frequency of violating comments in the last 7 weeks
                                </CardDescription>
                            </CardHeader>
                            <CardContent>
                                    <ChartContainer config={chartConfig}>
                                        <LineChart data={weeklyStats}>
                                            <CartesianGrid strokeDasharray="3 3"/>
                                            <XAxis dataKey="weekShort"
                                                   tickLine={false}
                                                   axisLine={false}
                                                   tickMargin={8}
                                            />
                                            <YAxis tickLine={false}
                                                   axisLine={false}
                                                   tickMargin={8}
                                                   tickFormatter={(value) => value >= 1000 ? `${(value / 1000).toFixed(0)}k` : value}
                                            />
                                            <Tooltip
                                                content={({active, payload}) => {
                                                    if (active && payload && payload.length) {
                                                        return (
                                                            <div
                                                                className="rounded-lg border bg-background p-2 shadow-sm">
                                                                <div className="grid grid-cols-1 gap-2">
                                                                    <div className="flex flex-col">
                                                                        <span
                                                                            className="text-[0.70rem] uppercase text-muted-foreground">
                                                                            Week
                                                                        </span>
                                                                        <span className="font-bold text-foreground">
                                                                            {payload[0].payload.week}
                                                                        </span>
                                                                    </div>
                                                                    <div className="flex flex-col">
                                                                        <span
                                                                            className="text-[0.70rem] uppercase text-muted-foreground">
                                                                            Violate
                                                                        </span>
                                                                        <span className="font-bold text-foreground">
                                                                            {payload[0].value} comment
                                                                        </span>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        );
                                                    }
                                                    return null;
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
                            </CardContent>
                        </Card>
                    )}

                    {/* Bảng danh sách comment vi phạm */}
                    {violatorData && (
                        <Card>
                            <CardHeader>
                                <CardTitle>History of Violation Comments</CardTitle>
                                <CardDescription>
                                    List of negative comments sorted from newest
                                </CardDescription>
                            </CardHeader>
                            <CardContent>
                                {/* Hiển thị tổng số phần tử */}
                                {totalElements > 0 && (
                                    <div
                                        className="text-sm text-muted-foreground flex justify-between items-center mb-4">
                                        <span>Total: <strong>{totalElements.toLocaleString()}</strong> comments found</span>
                                        <span>Page {page} of {totalPages}</span>
                                    </div>
                                )}

                                <div className="rounded-md border">
                                    <Table>
                                        <TableHeader>
                                            <TableRow>
                                                <TableHead>User</TableHead>
                                                <TableHead>Content</TableHead>
                                                <TableHead>Probability</TableHead>
                                                <TableHead>Created At</TableHead>
                                            </TableRow>
                                        </TableHeader>
                                        <TableBody>
                                            {isLoading ? (
                                                <TableRow>
                                                    <TableCell colSpan={4} className="h-24 text-center">
                                                        <div className="flex justify-center items-center">
                                                            <span>Loading...</span>
                                                        </div>
                                                    </TableCell>
                                                </TableRow>
                                            ) : comments.length === 0 ? (
                                                <TableRow>
                                                    <TableCell colSpan={4} className="h-24 text-center">
                                                        <div className="flex flex-col items-center justify-center">
                                                            <AlertCircle
                                                                className="h-12 w-12 text-muted-foreground mb-4"/>
                                                            <p className="text-center text-muted-foreground">
                                                                No offending comments found
                                                            </p>
                                                        </div>
                                                    </TableCell>
                                                </TableRow>
                                            ) : (
                                                comments.map((comment) => (
                                                    <TableRow key={comment.id}>
                                                        <TableCell>
                                                            <div className="flex items-center gap-2">
                                                                <Avatar className="h-8 w-8">
                                                                    <AvatarImage
                                                                        src={comment.userAvatar}
                                                                        alt={comment.username}
                                                                    />
                                                                    <AvatarFallback>
                                                                        {getAvatarFallback(comment.username)}
                                                                    </AvatarFallback>
                                                                </Avatar>
                                                                <div>
                                                                    <div
                                                                        className="font-medium">{comment.username}</div>
                                                                    <div className="text-xs text-muted-foreground">
                                                                        ID: {comment.userId}
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </TableCell>
                                                        <TableCell className="max-w-[300px]">
                                                            <div className="truncate" title={comment.content}>
                                                                {comment.content}
                                                            </div>
                                                        </TableCell>
                                                        <TableCell>
                                                            <div className="flex items-center gap-2">
                                                                <div
                                                                    className={cn(
                                                                        "h-2 w-16 rounded-full",
                                                                        comment.spamProbability > 0.9
                                                                            ? "bg-destructive"
                                                                            : comment.spamProbability > 0.8
                                                                                ? "bg-orange-500"
                                                                                : "bg-yellow-500",
                                                                    )}
                                                                >
                                                                    <div
                                                                        className="h-full rounded-full bg-primary"
                                                                        style={{
                                                                            width: `${comment.spamProbability * 100}%`,
                                                                        }}
                                                                    />
                                                                </div>
                                                                <span className="text-xs font-medium min-w-[40px]">
                                                                    {(comment.spamProbability * 100).toFixed(1)}%
                                                                </span>
                                                            </div>
                                                        </TableCell>
                                                        <TableCell className="text-sm">
                                                            {new Date(comment.createdAt).toLocaleString()}
                                                        </TableCell>
                                                    </TableRow>
                                                ))
                                            )}
                                        </TableBody>
                                    </Table>
                                </div>

                                {/* Pagination */}
                                {totalPages > 1 && (
                                    <div className="mt-4">
                                        <Pagination>
                                            <PaginationContent>
                                                <PaginationItem>
                                                    <PaginationPrevious
                                                        onClick={() => page > 1 && handlePageChange(page - 1)}
                                                        className={page <= 1 ? "pointer-events-none opacity-50" : "cursor-pointer"}
                                                    />
                                                </PaginationItem>

                                                {[...Array(Math.min(5, totalPages))].map((_, i) => {
                                                    // Hiển thị tối đa 5 trang, ưu tiên trang hiện tại ở giữa
                                                    let pageNum;
                                                    if (totalPages <= 5) {
                                                        pageNum = i + 1;
                                                    } else if (page <= 3) {
                                                        pageNum = i + 1;
                                                    } else if (page >= totalPages - 2) {
                                                        pageNum = totalPages - 4 + i;
                                                    } else {
                                                        pageNum = page - 2 + i;
                                                    }

                                                    return (
                                                        <PaginationItem key={i}>
                                                            <PaginationLink
                                                                onClick={() => handlePageChange(pageNum)}
                                                                isActive={pageNum === page}
                                                                className="cursor-pointer"
                                                            >
                                                                {pageNum}
                                                            </PaginationLink>
                                                        </PaginationItem>
                                                    );
                                                })}

                                                {totalPages > 5 && page < totalPages - 2 && (
                                                    <PaginationItem>
                                                        <PaginationEllipsis/>
                                                    </PaginationItem>
                                                )}

                                                <PaginationItem>
                                                    <PaginationNext
                                                        onClick={() => page < totalPages && handlePageChange(page + 1)}
                                                        className={page >= totalPages ? "pointer-events-none opacity-50" : "cursor-pointer"}
                                                    />
                                                </PaginationItem>
                                            </PaginationContent>
                                        </Pagination>
                                    </div>
                                )}
                            </CardContent>
                        </Card>
                    )}

                    {/* Empty state khi chưa search */}
                    {!violatorData && userId && !isLoading && (
                        <div className="flex flex-col items-center justify-center p-8 border rounded-lg">
                            <User className="h-12 w-12 text-muted-foreground mb-4"/>
                            <p className="text-center text-muted-foreground">No user found with this ID</p>
                        </div>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}