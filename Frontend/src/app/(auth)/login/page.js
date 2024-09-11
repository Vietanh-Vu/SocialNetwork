"use client";

import Link from "next/link";

import {Button} from "@/components/ui/button";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage,} from "@/components/ui/form";
import {Input} from "@/components/ui/input";
import {loginFormSchema} from "@/lib/validate";
import {zodResolver} from "@hookform/resolvers/zod";
import {useForm} from "react-hook-form";
import {login} from "@/lib/action";
import {toast} from "sonner";
import {useRouter} from "next/navigation";
import {useEffect} from "react";
import Cookies from "js-cookie";
import process from "next/dist/build/webpack/loaders/resolve-url-loader/lib/postcss";

export default function Page() {
    useEffect(() => {
        // toast.error("You are not authenticated. Please login to continue.");
        Cookies.remove("access-token");
        Cookies.remove("refresh-token");
    }, []);

    console.log("api url: ", process.env.NEXT_PUBLIC_API_URL);
    console.log("access token expiry: ", process.env.NEXT_PUBLIC_ACCESS_TOKEN_EXPIRY);
    console.log("refresh token expiry: ", process.env.NEXT_PUBLIC_ACCESS_REFRESH_EXPIRY);
    console.log("verify token expiry: ", process.env.NEXT_PUBLIC_ACCESS_VERIFY_EXPIRY);

    const router = useRouter()
    const form = useForm({
        resolver: zodResolver(loginFormSchema),
        defaultValues: {
            email: "",
            password: "",
        },
    });

    async function onSubmit(values) {
        const res = await login(values);
        if (!res.isSuccessful) {
            toast.error("Login failed. Please check your email and password again");
        } else {
            toast.success("Login successful");
            await router.push("/home");
        }
    }

    return (
        <>
            <div className="mx-auto grid w-[350px] gap-6">
                <div className="grid gap-2 text-center">
                    <h1 className="text-3xl font-bold">Login</h1>
                    <p className="text-balance text-muted-foreground">
                        Enter your email below to login to your account
                    </p>
                </div>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)} className="grid gap-4">
                        <FormField
                            control={form.control}
                            name="email"
                            render={({field}) => (
                                <FormItem>
                                    <FormLabel>Email</FormLabel>
                                    <FormControl>
                                        <Input placeholder="Enter your email" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />

                        <FormField
                            control={form.control}
                            name="password"
                            render={({field}) => (
                                <FormItem>
                                    <div className="flex items-center">
                                        <FormLabel>Password</FormLabel>
                                        <Link
                                            href="/forgot_password"
                                            className="ml-auto inline-block text-sm underline"
                                        >
                                            Forgot your password?
                                        </Link>
                                    </div>
                                    <FormControl>
                                        <Input type="password" placeholder="Enter your password" {...field} />
                                    </FormControl>
                                    <FormMessage/>
                                </FormItem>
                            )}
                        />

                        <Button type="submit" className="w-full">
                            Login
                        </Button>
                    </form>
                </Form>

                <div className="mt-4 text-center text-sm">
                    Don&apos;t have an account?{" "}
                    <Link href="/register" className="underline">
                        Sign up
                    </Link>
                </div>
            </div>
        </>
    );
}
