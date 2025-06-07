"use client";
import {useEffect, useState} from "react";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {Textarea} from "@/components/ui/textarea";
import {Badge} from "@/components/ui/badge";
import {Alert, AlertDescription} from "@/components/ui/alert";
import {Separator} from "@/components/ui/separator";
import {toast} from "sonner";
import {format} from "date-fns";
import {vi} from "date-fns/locale";
import {AlertCircle, CheckCircle, Clock, MessageSquare, XCircle} from "lucide-react";
import {createAppeal, getAppealStatus, getMyAppeals} from "@/lib/data";

export default function AppealsPage() {
    const [appeals, setAppeals] = useState([]);
    const [appealStatus, setAppealStatus] = useState(null);
    const [newAppealReason, setNewAppealReason] = useState("");
    const [loading, setLoading] = useState(false);
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        fetchData().then(r => {
        });
    }, []);

    const fetchData = async () => {
        setLoading(true);
        try {
            const [appealsResult, statusResult] = await Promise.all([
                getMyAppeals(),
                getAppealStatus()
            ]);

            if (appealsResult.isSuccessful) {
                setAppeals(appealsResult.data.data || []);
            } else {
                toast.error(appealsResult.message || "Unable to load appeal list");
            }

            if (statusResult.isSuccessful) {
                setAppealStatus(statusResult.data.data);
            } else {
                toast.error(statusResult.message || "Unable to load appeal status");
            }
        } catch (error) {
            toast.error("An error occurred while loading data.");
        } finally {
            setLoading(false);
        }
    };

    const handleSubmitAppeal = async () => {
        if (!newAppealReason.trim()) {
            toast.error("Please enter reason.");
            return;
        }

        setSubmitting(true);
        try {
            const result = await createAppeal(newAppealReason);
            if (result.isSuccessful) {
                toast.success("Complaint has been sent successfully");
                setNewAppealReason("");
                await fetchData(); // Refresh data
            } else {
                toast.error(result.message || "Unable to submit complaint");
            }
        } catch (error) {
            toast.error("An error occurred while submitting the complaint.");
        } finally {
            setSubmitting(false);
        }
    };

    const getStatusBadge = (status) => {
        switch (status) {
            case "PENDING":
                return <Badge variant="outline" className="text-yellow-600 border-yellow-600"><Clock
                    className="w-3 h-3 mr-1"/>Đang chờ</Badge>;
            case "APPROVED":
                return <Badge variant="outline" className="text-green-600 border-green-600"><CheckCircle
                    className="w-3 h-3 mr-1"/>Được chấp nhận</Badge>;
            case "REJECTED":
                return <Badge variant="outline" className="text-red-600 border-red-600"><XCircle
                    className="w-3 h-3 mr-1"/>Bị từ chối</Badge>;
            default:
                return <Badge variant="outline">{status}</Badge>;
        }
    };

    if (loading) {
        return (
            <div className="container mx-auto p-6">
                <div className="text-center">Loading...</div>
            </div>
        );
    }

    return (
        <div className="container mx-auto p-6 space-y-6">
            <div className="flex items-center gap-2">
                <MessageSquare className="w-6 h-6"/>
                <h1 className="text-2xl font-bold">My appeal</h1>
            </div>

            {/* Appeal Status Alert */}
            {appealStatus && (
                <Alert
                    className={appealStatus.hasActiveBan ? "border-red-200 bg-red-50" : "border-blue-200 bg-blue-50"}>
                    <AlertCircle className="h-4 w-4"/>
                    <AlertDescription>
                        {appealStatus.hasActiveBan ? (
                            <div>
                                <p className="font-medium text-red-800">Your account is currently blocked from
                                    commenting.</p>
                                {appealStatus.hasPendingAppeal ? (
                                    <p className="text-red-700">You have a pending appeal.</p>
                                ) : appealStatus.canCreateAppeal ? (
                                    <p className="text-red-700">You can create a new appeal.</p>
                                ) : (
                                    <p className="text-red-700">You cannot create new appeal at this time.</p>
                                )}
                            </div>
                        ) : (
                            <p className="text-blue-800">Your account is not currently blocked from commenting.</p>
                        )}
                    </AlertDescription>
                </Alert>
            )}

            {/* Create New Appeal */}
            {appealStatus?.canCreateAppeal && (
                <Card>
                    <CardHeader>
                        <CardTitle>Create new appeal</CardTitle>
                        <CardDescription>
                            If you believe comment blocking is unfair, please submit a complaint with specific reasons.
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-4">
                        <Textarea
                            placeholder="Enter your reason..."
                            value={newAppealReason}
                            onChange={(e) => setNewAppealReason(e.target.value)}
                            rows={4}
                        />
                        <Button
                            onClick={handleSubmitAppeal}
                            disabled={submitting || !newAppealReason.trim()}
                        >
                            {submitting ? "Sending..." : "Submit"}
                        </Button>
                    </CardContent>
                </Card>
            )}

            {/* Appeals List */}
            <Card>
                <CardHeader>
                    <CardTitle>Appeal history</CardTitle>
                    <CardDescription>
                        List of appeals you have previously filed
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    {appeals.length === 0 ? (
                        <div className="text-center py-8 text-gray-500">
                            You have no appeals yet.
                        </div>
                    ) : (
                        <div className="space-y-4">
                            {appeals.map((appeal) => (
                                <div key={appeal.appealId} className="border rounded-lg p-4 space-y-3">
                                    <div className="flex justify-between items-start">
                                        <div className="flex-1">
                                            <div className="flex items-center gap-2 mb-2">
                                                {getStatusBadge(appeal.status)}
                                                <span
                                                    className="text-sm text-gray-500 dark:text-gray-400">#{appeal.appealId}</span>
                                            </div>
                                            <p className="text-gray-700 dark:text-gray-200 mb-2">{appeal.reason}</p>
                                        </div>
                                    </div>

                                    {appeal.adminResponse && (
                                        <>
                                            <Separator/>
                                            <div
                                                className="bg-gray-50 dark:bg-gray-800 p-3 rounded border dark:border-gray-700">
                                                <p className="text-sm font-medium text-gray-600 dark:text-gray-300 mb-1">Response
                                                    from Admin:</p>
                                                <p className="text-gray-700 dark:text-gray-200">{appeal.adminResponse}</p>
                                            </div>
                                        </>
                                    )}

                                    <div className="flex justify-between text-xs text-gray-500 dark:text-gray-400">
                                        <span>Created: {format(new Date(appeal.createdAt), "dd/MM/yyyy HH:mm", {locale: vi})}</span>
                                        {appeal.resolvedAt && (
                                            <span>Resolved: {format(new Date(appeal.resolvedAt), "dd/MM/yyyy HH:mm", {locale: vi})}</span>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </div>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}