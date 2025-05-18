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
import {jwtDecode} from "jwt-decode";

export default function Page() {
    useEffect(() => {
        // toast.error("You are not authenticated. Please login to continue.");
        Cookies.remove("access-token");
        Cookies.remove("refresh-token");
    }, []);

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
            // Kiểm tra role từ token và chuyển hướng phù hợp
            try {
                const token = Cookies.get("access-token");
                const decoded = jwtDecode(token);
                const isAdmin = decoded.authorities && Array.isArray(decoded.authorities) &&
                    decoded.authorities.includes('ADMIN');

                if (isAdmin) {
                    console.log("dang redirect sang admin")
                    await router.push("/admin");
                } else {
                    await router.push("/home");
                }
            } catch (error) {
                console.error("Error decoding token:", error);
                await router.push("/home");
            }
        }
    }

    return (
        <div className="flex min-h-[100dvh] flex-col items-center justify-center bg-background px-4 py-12 sm:px-6 lg:px-8">
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
        </div>
    );
}
