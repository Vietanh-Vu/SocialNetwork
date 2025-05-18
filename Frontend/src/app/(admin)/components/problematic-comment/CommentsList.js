"use client"

import { PaginationEllipsis } from "@/components/ui/pagination"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import {
    Pagination,
    PaginationContent,
    PaginationItem,
    PaginationLink,
    PaginationNext,
    PaginationPrevious,
} from "@/components/ui/pagination"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Label } from "@/components/ui/label"
import { CalendarIcon, Download } from "lucide-react"
import { Slider } from "@/components/ui/slider"
import { format } from "date-fns"
import { cn } from "@/lib/utils"
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"

// Mock data
const mockComments = [
    {
        id: 1,
        user_id: 101,
        username: "toxic_user1",
        user_avatar: "/placeholder-user.jpg",
        content: "This is a problematic comment with offensive language.",
        spam_probability: 0.92,
        created_at: "2023-05-15T10:30:00Z",
    },
    {
        id: 2,
        user_id: 102,
        username: "spammer42",
        user_avatar: "/placeholder-user.jpg",
        content: "Buy my products at example.com! Best deals!",
        spam_probability: 0.88,
        created_at: "2023-05-16T14:20:00Z",
    },
    {
        id: 3,
        user_id: 103,
        username: "bad_actor99",
        user_avatar: "/placeholder-user.jpg",
        content: "Click this suspicious link to get free stuff.",
        spam_probability: 0.95,
        created_at: "2023-05-17T09:15:00Z",
    },
    {
        id: 4,
        user_id: 104,
        username: "angry_user",
        user_avatar: "/placeholder-user.jpg",
        content: "This platform is terrible and everyone who works here is incompetent!",
        spam_probability: 0.75,
        created_at: "2023-05-18T16:45:00Z",
    },
    {
        id: 5,
        user_id: 105,
        username: "bot_account",
        user_avatar: "/placeholder-user.jpg",
        content: "Automated message with suspicious links and offers.",
        spam_probability: 0.98,
        created_at: "2023-05-19T11:10:00Z",
    },
]

export function CommentsList() {
    const [page, setPage] = useState(1)
    const [probabilityRange, setProbabilityRange] = useState([0.7, 1.0])
    const [dateRange, setDateRange] = useState({
        from: undefined,
        to: undefined,
    })

    const handleExport = () => {
        // Implement export functionality
        console.log("Exporting data with filters:", {
            minProbability: probabilityRange[0],
            maxProbability: probabilityRange[1],
            dateRange,
        })
    }

    return (
        <Card>
            <CardHeader>
                <CardTitle>Problematic Comments</CardTitle>
                <CardDescription>Filter and view problematic comments based on probability and date range.</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
                <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-4">
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
                                    <CalendarIcon className="mr-2 h-4 w-4" />
                                    {dateRange.from ? format(dateRange.from, "PPP") : <span>Pick a date</span>}
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent className="w-auto p-0">
                                <Calendar
                                    mode="single"
                                    selected={dateRange.from}
                                    onSelect={(date) => setDateRange((prev) => ({ ...prev, from: date }))}
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
                                    <CalendarIcon className="mr-2 h-4 w-4" />
                                    {dateRange.to ? format(dateRange.to, "PPP") : <span>Pick a date</span>}
                                </Button>
                            </PopoverTrigger>
                            <PopoverContent className="w-auto p-0">
                                <Calendar
                                    mode="single"
                                    selected={dateRange.to}
                                    onSelect={(date) => setDateRange((prev) => ({ ...prev, to: date }))}
                                    initialFocus
                                />
                            </PopoverContent>
                        </Popover>
                    </div>

                    <div className="flex items-end">
                        <Button onClick={handleExport} className="w-full">
                            <Download className="mr-2 h-4 w-4" />
                            Export Data
                        </Button>
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
                            {mockComments.map((comment) => (
                                <TableRow key={comment.id}>
                                    <TableCell>
                                        <div className="flex items-center gap-2">
                                            <Avatar className="h-8 w-8">
                                                <AvatarImage src={comment.user_avatar || "/placeholder.svg"} alt={comment.username} />
                                                <AvatarFallback>{comment.username.substring(0, 2).toUpperCase()}</AvatarFallback>
                                            </Avatar>
                                            <div>
                                                <div className="font-medium">{comment.username}</div>
                                                <div className="text-xs text-muted-foreground">ID: {comment.user_id}</div>
                                            </div>
                                        </div>
                                    </TableCell>
                                    <TableCell className="max-w-[300px] truncate">{comment.content}</TableCell>
                                    <TableCell>
                                        <div className="flex items-center gap-2">
                                            <div
                                                className={cn(
                                                    "h-2 w-full rounded-full",
                                                    comment.spam_probability > 0.9
                                                        ? "bg-destructive"
                                                        : comment.spam_probability > 0.8
                                                            ? "bg-orange-500"
                                                            : "bg-yellow-500",
                                                )}
                                            >
                                                <div
                                                    className="h-full rounded-full bg-primary"
                                                    style={{
                                                        width: `${comment.spam_probability * 100}%`,
                                                    }}
                                                />
                                            </div>
                                            <span className="text-xs font-medium">{(comment.spam_probability * 100).toFixed(0)}%</span>
                                        </div>
                                    </TableCell>
                                    <TableCell className="text-sm">{new Date(comment.created_at).toLocaleString()}</TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </div>
            </CardContent>
            <CardFooter>
                <Pagination>
                    <PaginationContent>
                        <PaginationItem>
                            <PaginationPrevious href="#" />
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink href="#" isActive>
                                1
                            </PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink href="#">2</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationLink href="#">3</PaginationLink>
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationEllipsis />
                        </PaginationItem>
                        <PaginationItem>
                            <PaginationNext href="#" />
                        </PaginationItem>
                    </PaginationContent>
                </Pagination>
            </CardFooter>
        </Card>
    )
}
