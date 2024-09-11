"use client";

import {Input} from "@/components/ui/input"
import {Textarea} from "@/components/ui/textarea"
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select"
import {Button} from "@/components/ui/button"
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {registerFormSchema} from "@/lib/validate";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import Link from "next/link";
import {register} from "@/lib/action";
import {toast} from "sonner";
import {useRouter} from "next/navigation";
import {getProvince} from "@/lib/utils";


export default function Page() {
    const provinces = getProvince();

    const form = useForm({
        resolver: zodResolver(registerFormSchema),
        defaultValues: {
            email: "",
            firstName: "",
            lastName: "",
            bio: "",
            location: "",
            gender: "",
            work: "",
            education: "",
            dateOfBirth: "",
        }
    })

    async function onSubmit(values) {
        const res = await register(values);
        if (!res.isSuccessful) {
            toast.error(res.message)
        } else {
            toast.success(res.message);
        }
    }

    return (
        <div className="mx-auto grid gap-6">
            <div className="grid gap-2 text-center">
                <h1 className="text-3xl font-bold">Create an Account</h1>
                <p className="text-balance text-muted-foreground">
                    Fill out the form below to register for your new account.
                </p>
            </div>
            <div className="w-full max-w-2xl mx-auto">
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)}>
                        <div className="space-y-6">
                            <div className="grid grid-cols-2 gap-6">
                                <div className="space-y-2">
                                    <FormField
                                        control={form.control}
                                        name="firstName"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>First Name</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="Enter your first name" {...field} required/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <div className="space-y-2">
                                    <FormField
                                        control={form.control}
                                        name="lastName"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Last Name</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="Enter your last name" {...field} required/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                            </div>
                            <div className="space-y-2">
                                <FormField
                                    control={form.control}
                                    name="email"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel>Email</FormLabel>
                                            <FormControl>
                                                <Input placeholder="Enter your email" {...field} required/>
                                            </FormControl>
                                            <FormMessage/>
                                        </FormItem>
                                    )}
                                />
                            </div>
                            <div className="space-y-2">
                                <FormField
                                    control={form.control}
                                    name="password"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel>Password</FormLabel>
                                            <FormControl>
                                                <Input placeholder="Create a password" {...field} required/>
                                            </FormControl>
                                            <FormMessage/>
                                        </FormItem>
                                    )}
                                />
                            </div>
                            <div className="space-y-2">
                                <FormField
                                    control={form.control}
                                    name="bio"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel>Bio</FormLabel>
                                            <FormControl>
                                                <Textarea id="bio" placeholder="Tell us about yourself"
                                                          className="min-h-[75px]" {...field}/>
                                            </FormControl>
                                            <FormMessage/>
                                        </FormItem>
                                    )}
                                />
                            </div>
                            <div className="grid grid-cols-2 gap-6">
                                <div className="space-y-2">
                                    <FormField
                                        control={form.control}
                                        name="location"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Location</FormLabel>
                                                <FormControl>
                                                    <Select id="location" {...field} onValueChange={field.onChange}
                                                            defaultValue={field.value}>
                                                        <SelectTrigger>
                                                            <SelectValue placeholder="Select your location"/>
                                                        </SelectTrigger>
                                                        <SelectContent>
                                                            {provinces.map((province) => (
                                                                <SelectItem key={province.province_id}
                                                                            value={province.province_name}>
                                                                    {province.province_name}
                                                                </SelectItem>
                                                            ))}
                                                        </SelectContent>
                                                    </Select>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <div className="space-y-2">
                                    <FormField
                                        control={form.control}
                                        name="gender"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Gender</FormLabel>
                                                <FormControl>
                                                    <Select id="gender" onValueChange={field.onChange}
                                                            defaultValue={field.value}>
                                                        <SelectTrigger>
                                                            <SelectValue placeholder="Select your gender"/>
                                                        </SelectTrigger>
                                                        <SelectContent>
                                                            <SelectItem value="MALE">Male</SelectItem>
                                                            <SelectItem value="FEMALE">Female</SelectItem>
                                                            <SelectItem value="OTHERS">Others</SelectItem>
                                                        </SelectContent>
                                                    </Select>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                            </div>
                            <div className="grid grid-cols-2 gap-6">
                                <div className="space-y-2">
                                    <FormField
                                        control={form.control}
                                        name="work"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Work</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="Enter your work" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                                <div className="space-y-2">
                                    <FormField
                                        control={form.control}
                                        name="education"
                                        render={({field}) => (
                                            <FormItem>
                                                <FormLabel>Education</FormLabel>
                                                <FormControl>
                                                    <Input placeholder="Enter your education" {...field}/>
                                                </FormControl>
                                                <FormMessage/>
                                            </FormItem>
                                        )}
                                    />
                                </div>
                            </div>
                            <div className="space-y-2">
                                <FormField
                                    control={form.control}
                                    name="dateOfBirth"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel>Date of Birth</FormLabel>
                                            <FormControl>
                                                <Input type="date" {...field} required/>
                                            </FormControl>
                                            <FormMessage/>
                                        </FormItem>
                                    )}
                                />
                            </div>
                        </div>
                        <div className="mt-8">
                            <Button type="submit" className="w-full">
                                Create Account
                            </Button>
                        </div>
                    </form>
                </Form>
                <div className="mt-4 text-center text-sm">
                    Already have an account?{" "}
                    <Link href="/login" className="underline">
                        Login
                    </Link>
                </div>
            </div>
        </div>
    )
}
