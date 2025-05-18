"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { createConfig } from "@/lib/action"
import { toast } from "sonner"
import Spinner from "@/app/(admin)/ultils/Spinner"

export function AddGlobalConfigDialog({ children }) {
    const [open, setOpen] = useState(false)
    const [loading, setLoading] = useState(false)
    const [formData, setFormData] = useState({
        name: "",
        code: "",
        value: ""
    })

    const handleChange = (e) => {
        const { name, value } = e.target
        setFormData((prev) => ({ ...prev, [name]: value }))
    }

    const handleSubmit = async (e) => {
        e.preventDefault()
        setLoading(true)

        try {
            const response = await createConfig(formData)

            if (response.isSuccessful) {
                toast.success("Configuration created successfully")
                setOpen(false)
                setFormData({ name: "", code: "", desc: "", value: "" })
                // refresh the config list
                window.location.reload()
            } else {
                toast.error(response.message || "Failed to create configuration")
            }
        } catch (error) {
            toast.error("An error occurred while creating the configuration")
        } finally {
            setLoading(false)
        }
    }

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>{children}</DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
                <form onSubmit={handleSubmit}>
                    <DialogHeader>
                        <DialogTitle>Add Global Configuration</DialogTitle>
                        <DialogDescription>Create a new global configuration setting.</DialogDescription>
                    </DialogHeader>
                    <div className="grid gap-4 py-4">
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="name" className="text-right">
                                Name
                            </Label>
                            <Input
                                id="name"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                className="col-span-3"
                                required
                            />
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="code" className="text-right">
                                Code
                            </Label>
                            <Input
                                id="code"
                                name="code"
                                value={formData.code}
                                onChange={handleChange}
                                className="col-span-3 font-mono"
                                required
                            />
                        </div>
                        <div className="grid grid-cols-4 items-center gap-4">
                            <Label htmlFor="value" className="text-right">
                                Desc
                            </Label>
                            <Textarea
                                id="value"
                                name="value"
                                value={formData.value}
                                onChange={handleChange}
                                className="col-span-3"
                                required
                            />
                        </div>
                    </div>
                    <DialogFooter>
                        <Button type="submit" disabled={loading}>
                            Create
                            {/*{loading ? <><Spinner size="sm" /> Creating...</> : "Create"}*/}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    )
}