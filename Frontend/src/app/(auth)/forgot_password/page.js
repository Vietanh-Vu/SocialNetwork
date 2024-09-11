"use client";
import {Label} from "@/components/ui/label"
import {Input} from "@/components/ui/input"
import {Button} from "@/components/ui/button"
import Link from "next/link"
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {forgotPasswordFormSchema} from "@/lib/validate";
import {forgotPassword} from "@/lib/action";
import {toast} from "sonner";
import {useRouter} from "next/navigation";

export default function Page() {
    const form = useForm({
        resolver: zodResolver(forgotPasswordFormSchema),
        defaultValues: {
            email: "",
        },
    })

    async function onSubmit(values) {
        const result = await forgotPassword(values.email);
        if (!result.isSuccessful) {
            toast.error(result.message)
        } else {
            toast.success("Reset password link sent to your email");
        }
    }

    return (
        <div className="mx-auto max-w-md space-y-6 py-12">
            <div className="space-y-2 text-center">
                <h1 className="text-3xl font-bold">Forgot Password</h1>
                <p className="text-muted-foreground">
                    Enter your email address and we&apos;ll send you a link to reset your password.
                </p>
            </div>
            <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                    <FormField
                        control={form.control}
                        name="email"
                        render={({field}) => (
                            <FormItem>
                                <FormLabel>Email</FormLabel>
                                <FormControl>
                                    <Input placeholder="Enter your email" {...field} require/>
                                </FormControl>
                                <FormMessage/>
                            </FormItem>
                        )}
                    />
                    <Button type="submit" className="w-full">
                        Reset Password
                    </Button>
                </form>
            </Form>
            <div className="text-center text-sm text-muted-foreground">
                <Link href="/login" className="underline underline-offset-4" prefetch={false}>
                    Back to login
                </Link>
            </div>
        </div>
    )
}