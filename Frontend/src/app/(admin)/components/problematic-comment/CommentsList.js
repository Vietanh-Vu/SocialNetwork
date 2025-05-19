"use client"

import {
    Pagination,
    PaginationContent,
    PaginationEllipsis,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious
} from "@/components/ui/pagination"

import {useCallback, useEffect, useState} from "react"
import {Button} from "@/components/ui/button"
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card"
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table"
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar"
import {Label} from "@/components/ui/label"
import {CalendarIcon, Download, Loader2} from "lucide-react"
import {Slider} from "@/components/ui/slider"
import {format} from "date-fns"
import {cn, getAvatarFallback} from "@/lib/utils"
import {Calendar} from "@/components/ui/calendar"
import {Popover, PopoverContent, PopoverTrigger} from "@/components/ui/popover"
import {toast} from "sonner";
import {exportData, getProblematicComments} from "@/lib/data";

export function CommentsList() {
    const [page, setPage] = useState(1)
    const [totalPages, setTotalPages] = useState(1)
    const [probabilityRange, setProbabilityRange] = useState([0.7, 1.0])
    const [dateRange, setDateRange] = useState({
        from: new Date(new Date().getFullYear(), new Date().getMonth(), 1), // Ngày đầu tiên của tháng hiện tại
        to: new Date(), // Ngày hiện tại
    })
    const [isExporting, setIsExporting] = useState(false)
    const [isLoading, setIsLoading] = useState(true)
    const [comments, setComments] = useState([])

    const loadComments = useCallback(async () => {
        setIsLoading(true);
        try {
            const startDate = dateRange.from ? format(dateRange.from, "yyyy-MM-dd") : undefined;
            const endDate = dateRange.to ? format(dateRange.to, "yyyy-MM-dd") : undefined;

            const result = await getProblematicComments(
                page,
                probabilityRange[0],
                probabilityRange[1],
                startDate,
                endDate
            );

            if (result.isSuccessful) {
                // Cập nhật để sử dụng cấu trúc dữ liệu mới
                setComments(result.data.data || []);
                setTotalPages(result.data.pageMeta.totalPages || 1);
            } else {
                toast.error("Failed to load comments");
                console.error("Error loading comments:", result.message);
            }
        } catch (error) {
            toast.error("Error loading comments");
            console.error("Error loading comments:", error);
        } finally {
            setIsLoading(false);
        }
    }, [page, probabilityRange, dateRange.from, dateRange.to]);

// Tải dữ liệu khi component mount hoặc khi các điều kiện lọc thay đổi
    useEffect(() => {
        loadComments();
    }, [loadComments]);

    const handleExport = async () => {
        setIsExporting(true);
        try {
            const minProb = probabilityRange[0];
            const maxProb = probabilityRange[1];
            const startDate = dateRange.from ? format(dateRange.from, "yyyy-MM-dd") : undefined;
            const endDate = dateRange.to ? format(dateRange.to, "yyyy-MM-dd") : undefined;

            const result = await exportData(minProb, maxProb, startDate, endDate);

            if (result.isSuccessful) {
                // Tạo blob từ arraybuffer
                const blob = new Blob([result.data.arrayBuffer], {type: result.data.type});

                // Tạo URL đối tượng và tải xuống
                const url = window.URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = url;
                a.download = result.data.filename;
                document.body.appendChild(a);
                a.click();
                document.body.removeChild(a);
                window.URL.revokeObjectURL(url);

                toast.success("Export successful");
            } else {
                toast.error("Export failed");
                console.error("Export error:", result.message);
            }
        } catch (error) {
            toast.error("Export error");
            console.error("Export error:", error);
        } finally {
            setIsExporting(false);
        }
    };

    const handlePageChange = (newPage) => {
        setPage(newPage);
    };

    return (
        <Card>
            <CardHeader>
                <CardTitle>Problematic Comments</CardTitle>
                <CardDescription>Filter and view problematic comments based on probability and date
                    range.</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
                <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
                    <div className="space-y-2">
                        <Label>Probability Range</Label>
                        <div className="pt-4">
                            <Slider
                                defaultValue={[0.7, 1.0]}
                                max={1}
                                min={0}
                                step={0.01}
                                value={probabilityRange}
                                onValueChange={setProbabilityRange}
                            />
                        </div>
                        <div className="flex justify-between text-xs text-muted-foreground">
                            <span>{probabilityRange[0].toFixed(2)}</span>
                            <span>{probabilityRange[1].toFixed(2)}</span>
                        </div>
                    </div>

                    <div className="space-y-2">
                        <Label>Start Date</Label>
                        <Popover>
                            <PopoverTrigger asChild>
                                <Button
                                    variant={"outline"}
                                    className={cn(
                                        "w-full justify-start text-left font-normal",
                                        !dateRange.from && "text-muted-foreground",
                                    )}
                                >
                                    <CalendarIcon className="mr-2 h-4 w-4"/>
                                    {dateRange.from ? format(dateRange.from, "PPP") : <span>Pick a date</span>}
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent className="w-auto p-0">
                                <Calendar
                                    mode="single"
                                    selected={dateRange.from}
                                    onSelect={(date) => setDateRange((prev) => ({...prev, from: date}))}
                                    initialFocus
                                />
                            </PopoverContent>
                        </Popover>
                    </div>

                    <div className="space-y-2">
                        <Label>End Date</Label>
                        <Popover>
                            <PopoverTrigger asChild>
                                <Button
                                    variant={"outline"}
                                    className={cn("w-full justify-start text-left font-normal", !dateRange.to && "text-muted-foreground")}
                                >
                                    <CalendarIcon className="mr-2 h-4 w-4"/>
                                    {dateRange.to ? format(dateRange.to, "PPP") : <span>Pick a date</span>}
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent className="w-auto p-0">
                                <Calendar
                                    mode="single"
                                    selected={dateRange.to}
                                    onSelect={(date) => setDateRange((prev) => ({...prev, to: date}))}
                                    initialFocus
                                />
                            </PopoverContent>
                        </Popover>
                    </div>
                </div>

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
                                        No comments found matching the criteria
                                    </TableCell>
                                </TableRow>
                            ) : (
                                comments.map((comment) => (
                                    <TableRow key={comment.id}>
                                        <TableCell>
                                            <div className="flex items-center gap-2">
                                                <Avatar className="h-8 w-8">
                                                    <AvatarImage src={comment.userAvatar} alt={comment.username}/>
                                                    <AvatarFallback>{getAvatarFallback(comment.username)}</AvatarFallback>
                                                </Avatar>
                                                <div>
                                                    <div className="font-medium">{comment.username}</div>
                                                    <div
                                                        className="text-xs text-muted-foreground">ID: {comment.userId}</div>
                                                </div>
                                            </div>
                                        </TableCell>
                                        <TableCell className="max-w-[300px] truncate">{comment.content}</TableCell>
                                        <TableCell>
                                            <div className="flex items-center gap-2">
                                                <div
                                                    className={cn(
                                                        "h-2 w-full rounded-full",
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
                                                <span
                                                    className="text-xs font-medium">{(comment.spamProbability * 100).toFixed(0)}%</span>
                                            </div>
                                        </TableCell>
                                        <TableCell
                                            className="text-sm">{new Date(comment.createdAt).toLocaleString()}</TableCell>
                                    </TableRow>
                                ))
                            )}
                        </TableBody>
                    </Table>
                </div>
            </CardContent>
            <CardFooter className="flex flex-col gap-4">
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

                <Button onClick={handleExport} className="w-full" disabled={isExporting}>
                    {isExporting ? (
                        <span className="flex items-center">
                            <Loader2 className="mr-2 h-4 w-4 animate-spin"/>
                            <span>Đang xuất...</span>
                        </span>
                    ) : (
                        <span className="flex items-center">
                            <Download className="mr-2 h-4 w-4"/>
                            Export data with probability in range [{probabilityRange[0].toFixed(2)} - {probabilityRange[1].toFixed(2)}]
                        </span>
                    )}
                </Button>
            </CardFooter>
        </Card>
    )
}