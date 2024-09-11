'use client';

import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {resetPasswordFormSchema} from "@/lib/validate";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {toast} from "sonner";
import {useRouter} from "next/navigation";
import {resetPassword} from "@/lib/action";

export function ResetPasswordForm({token}) {
    const router = useRouter();
    const form = useForm({
        resolver: zodResolver(resetPasswordFormSchema),
        defaultValues: {
            resetPassword: "",
        },
    });

    async function onSubmit(values) {
        const res = await resetPassword(values.resetPassword, token);
        if (!res.isSuccessful) {
            toast.error(res.message);
        } else {
            toast.success("Password reset successful");
            router.push("/login");
        }
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                <FormField
                    control={form.control}
                    name="resetPassword"
                    render={({field}) => (
                        <FormItem>
                            <FormLabel>Reset password</FormLabel>
                            <FormControl>
                                <Input placeholder="Enter your reset password" {...field} />
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
    );
}