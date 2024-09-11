"use client";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {Input} from "@/components/ui/input";
import {Textarea} from "@/components/ui/textarea";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {Button} from "@/components/ui/button";
import {getProvince} from "@/lib/utils";
import {useForm} from "react-hook-form";
import {Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger} from "@/components/ui/dialog";
import {FilePen, XIcon} from "lucide-react";
import {useState} from "react";
import Image from "next/image";
import {ScrollArea} from "@/components/ui/scroll-area";
import {editProfile} from "@/lib/action";
import {toast} from "sonner";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import {useRouter} from "next/navigation";

function ProfileForm({userProfile}) {
    const provinces = getProvince();
    const [currentAvatar, setCurrentAvatar] = useState(userProfile.avatar)
    const [currentBackground, setCurrentBackground] = useState(userProfile.backgroundImage)
    const [avatar, setAvatar] = useState([]);
    const [background, setBackground] = useState([]);
    const [isDeleteAvatar, setIsDeleteAvatar] = useState(false);
    const [isDeleteBackground, setIsDeleteBackground] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const router = useRouter();

    const form = useForm({
        defaultValues: {
            firstName: userProfile.firstName,
            lastName: userProfile.lastName,
            bio: userProfile.bio,
            location: userProfile.location,
            gender: userProfile.gender,
            work: userProfile.work,
            education: userProfile.education,
            dateOfBirth: userProfile.dateOfBirth,
            avatar: userProfile.avatar,
            background: userProfile.backgroundImage,
            visibility: userProfile.visibility
        }
    })

    async function onSubmit(values) {
        const formData = new FormData();
        formData.append("bio", values.bio);
        formData.append("dateOfBirth", values.dateOfBirth);
        formData.append("education", values.education);
        formData.append("firstName", values.firstName);
        formData.append("gender", values.gender)
        formData.append("lastName", values.lastName);
        formData.append("location", values.location);
        formData.append("visibility", values.visibility);
        formData.append("work", values.work);
        formData.append("is_delete_avt", isDeleteAvatar);
        formData.append("is_delete_background", isDeleteBackground);

        avatar.forEach((file) => {
            formData.append("avatar", file);
        });

        background.forEach((file) => {
            formData.append("background", file);
        })

        setIsLoading(true);
        const result = await editProfile(formData);
        setIsLoading(false);
        if (result.isSuccessful) {
            toast.success("Profile updated successfully");
            router.push('/home')
        } else {
            console.log(result.message);
            toast.error("Failed to update profile");
        }
    }

    const handleUploadAvatar = (e) => {
        setAvatar(Array.from(e.target.files));
    }

    const handleRemoveAvatar = () => {
        if (currentAvatar) {
            setCurrentAvatar(null)
            setIsDeleteAvatar(true);
        }
        setAvatar([]);
    }

    const handleUploadBackground = (e) => {
        setBackground(Array.from(e.target.files));
    }

    const handleRemoveBackground = () => {
        if (currentBackground) {
            setCurrentBackground(null)
            setIsDeleteBackground(true);
        }
        setBackground([]);
    }

    return (
        <Dialog>
            <DialogTrigger asChild>
                <Button variant="outline" size="sm">
                    <FilePen className="w-4 h-4 mr-2"/>
                    Edit
                </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[650px]">
                <DialogHeader>
                    <DialogTitle>Change Information</DialogTitle>
                </DialogHeader>
                <ScrollArea className="max-h-[650px]">
                    <div className="mx-auto grid gap-6">
                        <div className="w-full max-w-2xl mx-auto">
                            {isLoading ? <Spinner/> : (
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
                                                                    <Input
                                                                        placeholder="Enter your first name" {...field}
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
                                                        name="lastName"
                                                        render={({field}) => (
                                                            <FormItem>
                                                                <FormLabel>Last Name</FormLabel>
                                                                <FormControl>
                                                                    <Input placeholder="Enter your last name" {...field}
                                                                           required/>
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
                                                    name="bio"
                                                    render={({field}) => (
                                                        <FormItem>
                                                            <FormLabel>Bio</FormLabel>
                                                            <FormControl>
                                                                <Textarea id="bio" placeholder="Tell us about yourself"
                                                                          className="min-h-[100px]" {...field}/>
                                                            </FormControl>
                                                            <FormMessage/>
                                                        </FormItem>
                                                    )}
                                                />
                                            </div>
                                            <div className="space-y-2">
                                                <FormField
                                                    control={form.control}
                                                    name="visibility"
                                                    render={({field}) => (
                                                        <FormItem>
                                                            <FormLabel>Visibility</FormLabel>
                                                            <FormControl>
                                                                <Select id="visibility" {...field}
                                                                        onValueChange={field.onChange}
                                                                        defaultValue={field.value}>
                                                                    <SelectTrigger>
                                                                        <SelectValue
                                                                            placeholder="Select your location"/>
                                                                    </SelectTrigger>
                                                                    <SelectContent>
                                                                        <SelectItem value="PUBLIC">Public</SelectItem>
                                                                        <SelectItem value="PRIVATE">Private</SelectItem>
                                                                        <SelectItem value="FRIEND">Friend</SelectItem>
                                                                    </SelectContent>
                                                                </Select>
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
                                                                    <Select id="location" {...field}
                                                                            onValueChange={field.onChange}
                                                                            defaultValue={field.value}>
                                                                        <SelectTrigger>
                                                                            <SelectValue
                                                                                placeholder="Select your location"/>
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
                                                                            <SelectValue
                                                                                placeholder="Select your gender"/>
                                                                        </SelectTrigger>
                                                                        <SelectContent>
                                                                            <SelectItem value="MALE">Male</SelectItem>
                                                                            <SelectItem
                                                                                value="FEMALE">Female</SelectItem>
                                                                            <SelectItem
                                                                                value="OTHERS">Others</SelectItem>
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
                                                                    <Input
                                                                        placeholder="Enter your education" {...field}/>
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
                                            <div className="grid grid-cols-2 gap-6">
                                                <div className="space-y-2">
                                                    <FormField
                                                        control={form.control}
                                                        name="avatar"
                                                        render={({field}) => (
                                                            <FormItem>
                                                                <FormLabel>Avatar</FormLabel>
                                                                <div
                                                                    className="border-2 border-dashed border-muted rounded-md p-4 flex flex-col items-center justify-center gap-2 hover:bg-muted/5 transition-colors"
                                                                >
                                                                    <FormControl>
                                                                        <Input type="file" accept="image/*"
                                                                               onChange={handleUploadAvatar}/>
                                                                    </FormControl>
                                                                    <div className="flex justify-center">
                                                                        {(avatar.length !== 0 || currentAvatar) && (
                                                                            <div className="relative">
                                                                                <Image
                                                                                    src={avatar.length !== 0 ? URL.createObjectURL(avatar[0]) : currentAvatar}
                                                                                    alt={`Preview ${avatar}`}
                                                                                    width={200}
                                                                                    height={200}/>
                                                                                <button
                                                                                    type="button"
                                                                                    className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1"
                                                                                    onClick={handleRemoveAvatar}
                                                                                >
                                                                                    <XIcon className="h-4 w-4"/>
                                                                                </button>
                                                                            </div>
                                                                        )}
                                                                    </div>
                                                                </div>
                                                            </FormItem>
                                                        )}
                                                    />
                                                </div>
                                                <div className="space-y-2">
                                                    <FormField
                                                        control={form.control}
                                                        name="background"
                                                        render={({field}) => (
                                                            <FormItem>
                                                                <FormLabel>Background</FormLabel>
                                                                <div
                                                                    className="border-2 border-dashed border-muted rounded-md p-4 flex flex-col items-center justify-center gap-2 hover:bg-muted/5 transition-colors"
                                                                >
                                                                    <FormControl>
                                                                        <Input type="file" accept="image/*"
                                                                               onChange={handleUploadBackground}/>
                                                                    </FormControl>
                                                                    <div className="flex justify-center">
                                                                        {(background.length !== 0 || currentBackground) && (
                                                                            <div className="relative">
                                                                                <Image
                                                                                    src={background.length !== 0 ? URL.createObjectURL(background[0]) : currentBackground}
                                                                                    alt={`Preview ${background}`}
                                                                                    width={200}
                                                                                    height={200}/>
                                                                                <button
                                                                                    type="button"
                                                                                    className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1"
                                                                                    onClick={handleRemoveBackground}
                                                                                >
                                                                                    <XIcon className="h-4 w-4"/>
                                                                                </button>
                                                                            </div>
                                                                        )}
                                                                    </div>
                                                                </div>
                                                            </FormItem>
                                                        )}
                                                    />
                                                </div>
                                            </div>
                                        </div>
                                        <div className="mt-8 flex justify-end">
                                            <Button type="submit">
                                                Change
                                            </Button>
                                        </div>
                                    </form>
                                </Form>
                            )}
                        </div>
                    </div>
                </ScrollArea>
            </DialogContent>
        </Dialog>
    )
}

export default ProfileForm;