"use client"

import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
} from "@/components/ui/alert-dialog"
import { useState } from "react"
import { deleteConfig } from "@/lib/action"
import { toast } from "sonner"
import Spinner from "@/app/(admin)/ultils/Spinner"

export function DeleteGlobalConfigDialog({ config, open, onOpenChange }) {
    const [isDeleting, setIsDeleting] = useState(false)

    const handleDelete = async (e) => {
        e.preventDefault()
        setIsDeleting(true)

        try {
            const response = await deleteConfig(config.code)

            if (response.isSuccessful) {
                toast.success(`Config ${config.code} deleted successfully.`)
                onOpenChange(false)
                // Reload trang để cập nhật dữ liệu
                window.location.reload()
            } else {
                toast.error(response.message || "Failed to delete configuration")
            }
        } catch (error) {
            toast.error(error || "Failed to delete configuration")
        } finally {
            setIsDeleting(false)
        }
    }

    return (
        <AlertDialog open={open} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Are you sure want to delete this config?</AlertDialogTitle>
                    <AlertDialogDescription>
                        This will permanently delete the configuration <span className="font-semibold">{config.name}</span> (
                        {config.code}). This action cannot be undone.
                    </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogCancel disabled={isDeleting}>Cancel</AlertDialogCancel>
                    <AlertDialogAction
                        onClick={handleDelete}
                        className="bg-destructive text-destructive-foreground"
                        disabled={isDeleting}
                    >
                        Delete
                    </AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    )
}