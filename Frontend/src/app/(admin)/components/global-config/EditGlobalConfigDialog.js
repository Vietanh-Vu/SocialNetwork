"use client"

import { useEffect, useState } from "react"
import { Button } from "@/components/ui/button"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { updateConfig } from "@/lib/action"
import { toast } from "sonner"
import Spinner from "@/app/(admin)/ultils/Spinner"

export function EditGlobalConfigDialog({ config, open, onOpenChange }) {
    const [formData, setFormData] = useState({
        name: "",
        code: "",
        desc: ""
    })
    const [loading, setLoading] = useState(false)

    useEffect(() => {
        if (config) {
            setFormData({
                name: config.name,
                code: config.code,
                desc: config.desc
            })
        }
    }, [config])

    const handleChange = (e) => {
        const { name, value } = e.target
        setFormData((prev) => ({ ...prev, [name]: value }))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setLoading(true)

        try {
            const response = await updateConfig(formData)

            if (response.isSuccessful) {
                toast.success("Configuration updated successfully")
                onOpenChange(false)
                // refresh the config list
                window.location.reload()
            } else {
                toast.error(response.message || "Failed to update configuration")
            }
        } catch (error) {
            toast.error("An error occurred while updating the configuration")
        } finally {
            setLoading(false)
        }
    }

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="sm:max-w-[425px]">
                <form onSubmit={handleSubmit}>
                    <DialogHeader>
                        <DialogTitle>Edit Global Configuration</DialogTitle>
                        <DialogDescription>Update the global configuration settings.</DialogDescription>
                    </DialogHeader>
                    <div className="grid gap-4 py-4">
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="edit-name" className="text-right">
                                Name
                            </Label>
                            <Input
                                id="edit-name"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                className="col-span-3"
                                required
                            />
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="edit-code" className="text-right">
                                Code
                            </Label>
                            <Input
                                id="edit-code"
                                name="code"
                                value={formData.code}
                                onChange={handleChange}
                                className="col-span-3 font-mono"
                                disabled
                            />
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="edit-desc" className="text-right">
                                Desc
                            </Label>
                            <Textarea
                                id="edit-desc"
                                name="desc"
                                value={formData.desc}
                                onChange={handleChange}
                                className="col-span-3"
                                required
                            />
                        </div>
                    </div>
                    <DialogFooter>
                        <Button type="submit" disabled={loading}>
                            Save changes
                            {/*{loading ? <><Spinner size="sm" /> Saving...</> : "Save changes"}*/}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    )
}