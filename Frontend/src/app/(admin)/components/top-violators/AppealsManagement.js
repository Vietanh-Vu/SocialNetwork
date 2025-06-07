"use client";
import {useCallback, useEffect, useState} from "react";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {Badge} from "@/components/ui/badge";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {Textarea} from "@/components/ui/textarea";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle
} from "@/components/ui/dialog";
import {Separator} from "@/components/ui/separator";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {toast} from "sonner";
import {format} from "date-fns";
import {vi} from "date-fns/locale";
import {CheckCircle, Clock, Filter, MessageSquare, XCircle} from "lucide-react";
import {getAvatarFallback} from "@/lib/utils";
import {getAdminAppeals, processAppeal} from "@/lib/data";

export default function AppealsManagement(callback, deps) {
    const [appeals, setAppeals] = useState([]);
    const [loading, setLoading] = useState(false);
    const [statusFilter, setStatusFilter] = useState("all");
    const [selectedAppeal, setSelectedAppeal] = useState(null);
    const [processingAppeal, setProcessingAppeal] = useState(false);
    const [adminResponse, setAdminResponse] = useState("");
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    useEffect(() => {
        fetchAppeals().then(r => {
        });
    }, [statusFilter, currentPage]);

    const fetchAppeals = useCallback(async () => {
        setLoading(true);
        try {
            const statuses = statusFilter === "all" ? null : [statusFilter.toUpperCase()];
            const result = await getAdminAppeals(currentPage, 10, statuses);

            if (result.isSuccessful) {
                setAppeals(result.data.data || []);
                setTotalPages(result.data.pageMeta?.totalPages || 1);
            } else {
                toast.error(result.message || "Unable to load appeal list");
            }
        } catch (error) {
            toast.error("An error occurred while loading data.");
        } finally {
            setLoading(false);
        }
    });

    const handleProcessAppeal = async (approved) => {
        if (!adminResponse.trim()) {
            toast.error("Please enter your response.");
            return;
        }

        setProcessingAppeal(true);
        try {
            const result = await processAppeal(selectedAppeal.appealId, approved, adminResponse);

            if (result.isSuccessful) {
                toast.success(`Appeal ${approved ? "approved" : "rejected"} successfully`);
                setSelectedAppeal(null);
                setAdminResponse("");
                await fetchAppeals(); // Refresh data
            } else {
                toast.error(result.message || "Unable to process appeal");
            }
        } catch (error) {
            toast.error("An error occurred while processing the appeal.");
        } finally {
            setProcessingAppeal(false);
        }
    };

    const getStatusBadge = (status) => {
        switch (status) {
            case "PENDING":
                return <Badge variant="outline" className="text-yellow-600 border-yellow-600"><Clock
                    className="w-3 h-3 mr-1"/>Pending</Badge>;
            case "APPROVED":
                return <Badge variant="outline" className="text-green-600 border-green-600"><CheckCircle
                    className="w-3 h-3 mr-1"/>Approved</Badge>;
            case "REJECTED":
                return <Badge variant="outline" className="text-red-600 border-red-600"><XCircle
                    className="w-3 h-3 mr-1"/>Rejected</Badge>;
            default:
                return <Badge variant="outline">{status}</Badge>;
        }
    };

    const openProcessDialog = (appeal) => {
        setSelectedAppeal(appeal);
        setAdminResponse("");
    };

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                    <MessageSquare className="w-6 h-6"/>
                    <h2 className="text-2xl font-bold">Appeal Management</h2>
                </div>

                <div className="flex items-center gap-2">
                    <Filter className="w-4 h-4"/>
                    <Select value={statusFilter} onValueChange={setStatusFilter}>
                        <SelectTrigger className="w-40">
                            <SelectValue placeholder="Filter by status"/>
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="all">All</SelectItem>
                            <SelectItem value="pending">Pending</SelectItem>
                            <SelectItem value="approved">Approved</SelectItem>
                            <SelectItem value="rejected">Rejected</SelectItem>
                        </SelectContent>
                    </Select>
                </div>
            </div>

            <Card>
                <CardHeader>
                    <CardTitle>List Appeal</CardTitle>
                    <CardDescription>
                        View and handle appeals from users
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    {loading ? (
                        <div className="text-center py-8">Loading...</div>
                    ) : appeals.length === 0 ? (
                        <div className="text-center py-8 text-gray-500">
                            No appeals
                        </div>
                    ) : (
                        <div className="space-y-4">
                            {appeals.map((appeal) => (
                                <div key={appeal.appealId} className="border rounded-lg p-4 space-y-3">
                                    <div className="flex justify-between items-start">
                                        <div className="flex items-center gap-3">
                                            <Avatar className="h-10 w-10">
                                                <AvatarImage src={appeal.userAvatar}/>
                                                <AvatarFallback>
                                                    {getAvatarFallback(appeal.username)}
                                                </AvatarFallback>
                                            </Avatar>
                                            <div>
                                                <div className="flex items-center gap-2">
                                                    <span className="font-medium">{appeal.username}</span>
                                                    <span className="text-sm text-gray-500">#Id: {appeal.userId}</span>
                                                </div>
                                                <div className="flex items-center gap-2 mt-1">
                                                    {getStatusBadge(appeal.status)}
                                                    <span className="text-xs text-gray-500">
                                                        {format(new Date(appeal.createdAt), "dd/MM/yyyy HH:mm", {locale: vi})}
                                                    </span>
                                                </div>
                                            </div>
                                        </div>

                                        {appeal.status === "PENDING" && (
                                            <Button
                                                variant="outline"
                                                size="sm"
                                                onClick={() => openProcessDialog(appeal)}
                                            >
                                                Process Appeal
                                            </Button>
                                        )}
                                    </div>

                                    <div
                                        className="bg-gray-50 dark:bg-gray-800 p-3 rounded border dark:border-gray-700">
                                        <p className="text-sm font-medium text-gray-600 dark:text-gray-300 mb-1">Reason
                                            for appeal:</p>
                                        <p className="text-gray-700 dark:text-gray-200">{appeal.reason}</p>
                                    </div>

                                    {appeal.adminResponse && (
                                        <>
                                            <Separator/>
                                            <div
                                                className="bg-gray-50 dark:bg-gray-800 p-3 rounded border dark:border-gray-700">
                                                <p className="text-sm font-medium text-gray-600 dark:text-gray-300 mb-1">Response
                                                    from Admin:</p>
                                                <p className="text-gray-700 dark:text-gray-200">{appeal.adminResponse}</p>
                                                <div
                                                    className="flex justify-between text-xs text-gray-500 dark:text-gray-400 mt-2">
                                                    <span>Created: {format(new Date(appeal.createdAt), "dd/MM/yyyy HH:mm", {locale: vi})}</span>
                                                    {appeal.resolvedAt && (
                                                        <span>Resolved: {format(new Date(appeal.resolvedAt), "dd/MM/yyyy HH:mm", {locale: vi})}</span>
                                                    )}
                                                </div>
                                            </div>
                                        </>
                                    )}
                                </div>
                            ))}
                        </div>
                    )}

                    {/* Pagination */}
                    {totalPages > 1 && (
                        <div className="flex justify-center gap-2 mt-6">
                            <Button
                                variant="outline"
                                size="sm"
                                disabled={currentPage === 1}
                                onClick={() => setCurrentPage(currentPage - 1)}
                            >
                                Before
                            </Button>
                            <span className="flex items-center px-3 text-sm">
                                Page {currentPage} / {totalPages}
                            </span>
                            <Button
                                variant="outline"
                                size="sm"
                                disabled={currentPage === totalPages}
                                onClick={() => setCurrentPage(currentPage + 1)}
                            >
                                After
                            </Button>
                        </div>
                    )}
                </CardContent>
            </Card>

            {/* Process Appeal Dialog */}
            <Dialog open={!!selectedAppeal} onOpenChange={() => setSelectedAppeal(null)}>
                <DialogContent className="max-w-2xl">
                    <DialogHeader>
                        <DialogTitle>Handle appeal #{selectedAppeal?.appealId} for userId
                            #{selectedAppeal?.userId}</DialogTitle>
                        <DialogDescription>
                            From user: {selectedAppeal?.username}
                        </DialogDescription>
                    </DialogHeader>

                    {selectedAppeal && (
                        <div className="space-y-4">
                            <div className="bg-gray-50 p-3 rounded">
                                <p className="text-sm font-medium text-gray-600 mb-1">Reason:</p>
                                <p className="text-gray-700">{selectedAppeal.reason}</p>
                            </div>

                            <div>
                                <label className="text-sm font-medium text-gray-600 mb-2 block">
                                    Admin Response:
                                </label>
                                <Textarea
                                    placeholder="Enter response..."
                                    value={adminResponse}
                                    onChange={(e) => setAdminResponse(e.target.value)}
                                    rows={4}
                                />
                            </div>
                        </div>
                    )}

                    <DialogFooter className="gap-2">
                        <Button
                            variant="outline"
                            onClick={() => setSelectedAppeal(null)}
                            disabled={processingAppeal}
                        >
                            Cancel
                        </Button>
                        <Button
                            variant="destructive"
                            onClick={() => handleProcessAppeal(false)}
                            disabled={processingAppeal || !adminResponse.trim()}
                        >
                            {processingAppeal ? "Sending..." : "Reject"}
                        </Button>
                        <Button
                            onClick={() => handleProcessAppeal(true)}
                            disabled={processingAppeal || !adminResponse.trim()}
                        >
                            {processingAppeal ? "Sending..." : "Approve"}
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    );
}