"use client"
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog"
import {Button} from "@/components/ui/button"
import {Textarea} from "@/components/ui/textarea"
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select"
import SearchFriend from "@/app/(overview)/components/ultils/SearchFriend";
import {Input} from "@/components/ui/input";
import {useState} from "react";
import Image from "next/image";
import {Carousel, CarouselContent, CarouselItem} from "@/components/ui/carousel";
import {toast} from "sonner";
import {ScrollArea} from "@/components/ui/scroll-area";
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import {getAvatarFallback} from "@/lib/utils";
import {XIcon} from "lucide-react";
import {Card} from "@/components/ui/card";
import {useForm} from "react-hook-form";
import {Form, FormControl, FormField, FormItem, FormLabel} from "@/components/ui/form";
import {createPost} from "@/lib/action";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import {useRouter} from "next/navigation";

function PostForm() {
    const [selectedImages, setSelectedImages] = useState([]);
    const [imagesPreviews, setImagesPreviews] = useState([]);
    // const [selectedTags, setSelectedTags] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const router = useRouter()

    const handleImageUpload = (event) => {
        const files = Array.from(event.target.files);
        if (files.length + selectedImages.length > 4) {
            toast.warning(`You can only upload up to 4 images. ${4 - selectedImages.length} more can be added.`);
        }
        const newFiles = files.slice(0, 4 - selectedImages.length);
        setSelectedImages([...selectedImages, ...newFiles]);
        const newPreviews = newFiles.map((file) => URL.createObjectURL(file));
        setImagesPreviews([...imagesPreviews, ...newPreviews]);
    };

    const handleRemoveImage = (index) => {
        const newSelectedImages = [...selectedImages];
        const newImagesPreviews = [...imagesPreviews];
        newSelectedImages.splice(index, 1);
        newImagesPreviews.splice(index, 1);
        setSelectedImages(newSelectedImages);
        setImagesPreviews(newImagesPreviews);
    };

    const handleUserClick = (user) => {
        if (!selectedTags.some(tag => tag.id === user.id)) {
            setSelectedTags([...selectedTags, user]);
        }
    };

    const handleRemoveTag = (removeUser) => {
        setSelectedTags(selectedTags.filter((user) => user.id !== removeUser.id));
    };

    const form = useForm()

    const onSubmit = async (values) => {
        const formData = new FormData()
        formData.append("content", values.content)
        formData.append("visibility", values.visibility)
        selectedImages.forEach((photo) => {
            formData.append("photoLists", photo)
        })
        // formData.append("tagUsers", selectedTags.map(user => user.id).join(","))

        setIsLoading(true)
        const result = await createPost(formData)

        if (!result.isSuccessful) {
            console.log(result.message)
            toast.error("Failed to create post")
        } else {
            toast.success("Post created successfully")
            router.refresh()
        }
        setIsLoading(false)
    }

    return (
        <Dialog>
            <DialogTrigger asChild>
                <Button variant="outline">Create Post</Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[650px]">
                <DialogHeader>
                    <DialogTitle>Create Post</DialogTitle>
                    <DialogDescription>Share your thoughts, photos, and tags with your audience.</DialogDescription>
                </DialogHeader>
                <ScrollArea className="grid gap-4 max-h-[650px]">
                    {isLoading ? <Spinner/> : (
                        <Form {...form}>
                            <form onSubmit={form.handleSubmit(onSubmit)} className="grid gap-4 max-h-[650px] pr-2">
                                <FormField
                                    control={form.control}
                                    name="content"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel>Content</FormLabel>
                                            <FormControl>
                                                <Textarea {...field} id="content" placeholder="What's on your mind?"
                                                          className="min-h-[100px]"/>
                                            </FormControl>
                                        </FormItem>
                                    )}
                                />
                                <FormField
                                    control={form.control}
                                    name="visibility"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel>Visibility</FormLabel>
                                            <FormControl>
                                                <Select id="visibility" defaultValue={field.value}
                                                        onValueChange={field.onChange}>
                                                    <SelectTrigger>
                                                        <SelectValue placeholder="Select visibility"/>
                                                    </SelectTrigger>
                                                    <SelectContent>
                                                        <SelectItem value="PUBLIC">Public</SelectItem>
                                                        <SelectItem value="PRIVATE">Private</SelectItem>
                                                        <SelectItem value="FRIEND">Friend</SelectItem>
                                                    </SelectContent>
                                                </Select>
                                            </FormControl>
                                        </FormItem>
                                    )}
                                />
                                <FormField
                                    control={form.control}
                                    name="photos"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel>Photos ({selectedImages.length}/4)</FormLabel>
                                            <div
                                                className="border-2 border-dashed border-muted rounded-md p-4 flex flex-col items-center justify-center gap-2 hover:bg-muted/5 transition-colors"
                                            >
                                                <FormControl>
                                                    <Input {...field} type="file" accept="image/*" multiple
                                                           onChange={handleImageUpload}
                                                           disabled={selectedImages.length >= 4}/>
                                                </FormControl>
                                                <div className="flex justify-center">
                                                    <Carousel className="w-full max-w-xs">
                                                        <CarouselContent>
                                                            {imagesPreviews.map((preview, index) => (
                                                                <CarouselItem key={index} className="relative">
                                                                    <Image src={preview} alt={`Preview ${index}`}
                                                                           width={200}
                                                                           height={200}/>
                                                                    <button
                                                                        type="button"
                                                                        className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1"
                                                                        onClick={() => handleRemoveImage(index)}
                                                                    >
                                                                        <XIcon className="h-4 w-4"/>
                                                                    </button>
                                                                </CarouselItem>
                                                            ))}
                                                        </CarouselContent>
                                                    </Carousel>
                                                </div>
                                            </div>
                                        </FormItem>
                                    )}
                                />
                                {/*<FormField*/}
                                {/*    control={form.control}*/}
                                {/*    name="tags"*/}
                                {/*    render={({field}) => (*/}
                                {/*        <FormItem>*/}
                                {/*            <FormLabel>Tag</FormLabel>*/}
                                {/*            <FormControl>*/}
                                {/*                <ScrollArea className="max-h-24 flex flex-wrap gap-2 mt-2 mb-2">*/}
                                {/*                    <div className="flex flex-wrap gap-2 max-h-24">*/}
                                {/*                        {selectedTags.map((user) => (*/}
                                {/*                            <Card*/}
                                {/*                                key={user.id}*/}
                                {/*                                className="px-2 py-1 rounded-md flex items-center gap-2"*/}
                                {/*                            >*/}
                                {/*                                <Avatar>*/}
                                {/*                                    <AvatarImage src={user.avatar} alt={user.username}/>*/}
                                {/*                                    <AvatarFallback>{getAvatarFallback(user.username)}</AvatarFallback>*/}
                                {/*                                </Avatar>*/}
                                {/*                                <span>{user.username}</span>*/}
                                {/*                                <button*/}
                                {/*                                    type="button"*/}
                                {/*                                    className="text-red-500 hover:text-red-600 transition-colors"*/}
                                {/*                                    onClick={() => handleRemoveTag(user)}*/}
                                {/*                                >*/}
                                {/*                                    <XIcon className="h-4 w-4"/>*/}
                                {/*                                </button>*/}
                                {/*                            </Card>*/}
                                {/*                        ))}*/}
                                {/*                    </div>*/}
                                {/*                </ScrollArea>*/}
                                {/*            </FormControl>*/}
                                {/*        </FormItem>*/}
                                {/*    )}*/}
                                {/*/>*/}
                                {/*<SearchTag onUserClick={handleUserClick}/>*/}
                                <div className="flex justify-end gap-2">
                                    <Button type="submit">Create Post</Button>
                                </div>
                            </form>
                        </Form>
                    )}
                </ScrollArea>
            </DialogContent>
        </Dialog>
    )
}

export default PostForm;