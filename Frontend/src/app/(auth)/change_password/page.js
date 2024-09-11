"use client"
import {Label} from "@/components/ui/label"
import {Input} from "@/components/ui/input"
import {Button} from "@/components/ui/button"
import Link from "next/link"
import {useRouter} from "next/navigation";
import {useForm} from "react-hook-form";
import {changePasswordFormSchema} from "@/lib/validate";
import {zodResolver} from "@hookform/resolvers/zod";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {toast} from "sonner";
import {changePassword} from "@/lib/action";

export default function Component() {
    const router = useRouter()
    const form = useForm({
        resolver: zodResolver(changePasswordFormSchema),
        defaultValues: {
            currentPassword: "",
            newPassword: "",
            confirmPassword: "",
        },
    })

    async function onSubmit(values) {
        if (values.currentPassword === values.newPassword) {
            return toast.error("New password must be different from the current password")
        }
        if (values.newPassword !== values.confirmPassword) {
            return toast.error("New password and confirm password must match")
        }

        const result = await changePassword(values.newPassword, values.currentPassword)

        if (result.isSuccessful) {
            toast.success("Password changed successfully")
            await router.push("/login")
        } else {
            console.log("Failed to change password", result.message)
            toast.error(`Failed to change password: ${result.message}`)
        }
    }

    return (
        <div className="flex min-h-[100dvh] items-center justify-center bg-background px-4 py-12 sm:px-6 lg:px-8">
            <div className="mx-auto w-full max-w-md space-y-6">
                <div className="space-y-2 text-center">
                    <h1 className="text-3xl font-bold">Change Password</h1>
                    <p className="text-muted-foreground">Enter your current password and a new secure password.</p>
                </div>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                        <div className="space-y-2">
                            <FormField
                                control={form.control}
                                name="currentPassword"
                                render={({field}) => (
                                    <FormItem>
                                        <FormLabel>Current Password</FormLabel>
                                        <FormControl>
                                            <Input type="password" placeholder="Enter your current password" {...field}
                                                   required/>
                                        </FormControl>
                                        <FormMessage/>
                                    </FormItem>
                                )}
                            />
                        </div>
                        <div className="space-y-2">
                            <FormField
                                control={form.control}
                                name="newPassword"
                                render={({field}) => (
                                    <FormItem>
                                        <FormLabel>New Password</FormLabel>
                                        <FormControl>
                                            <Input type="password" placeholder="Enter your new password" {...field}
                                                   required/>
                                        </FormControl>
                                        <FormMessage/>
                                    </FormItem>
                                )}
                            />
                        </div>
                        <div className="space-y-2">
                            <FormField
                                control={form.control}
                                name="confirmPassword"
                                render={({field}) => (
                                    <FormItem>
                                        <FormLabel>Confirm New Password</FormLabel>
                                        <FormControl>
                                            <Input type="password" placeholder="Confirm your new password" {...field}
                                                   required/>
                                        </FormControl>
                                        <FormMessage/>
                                    </FormItem>
                                )}
                            />
                        </div>
                        <div className="flex flex-col gap-4">
                            <Button type="submit" className="flex-1">
                                Change Password
                            </Button>
                            <Link
                                href="/home"
                                className="inline-flex h-10 items-center justify-center text-sm font-medium underline transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 flex-1"
                                prefetch={false}
                            >
                                Return to Home
                            </Link>
                        </div>
                    </form>
                </Form>
            </div>
        </div>
    )
}