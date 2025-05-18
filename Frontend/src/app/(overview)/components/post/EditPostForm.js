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
import {createPost, editPost} from "@/lib/action";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import {useRouter} from "next/navigation";

function EditPostForm({postInfo}) {
    const {
        id,
        avatar,
        username,
        userId,
        content,
        visibility,
        createdAt,
        updatedAt,
        photoResponses,
        numberOfComments,
        numberOfReacts,
        tagUsers,
        isReacted
    } = postInfo;

    const [currentImages, setCurrentImages] = useState(photoResponses?.map(photo => photo.url));
    const [selectedImagesUpload, setSelectedImagesUpload] = useState([]);
    const [imagesUploadPreviews, setImagesUploadPreviews] = useState([]);
    // const [selectedTags, setSelectedTags] = useState(tagUsers);
    const [isLoading, setIsLoading] = useState(false);
    const router = useRouter()

    const handleImageUpload = (event) => {
        const files = Array.from(event.target.files);
        if (files.length + selectedImagesUpload.length + (currentImages?.length || 0) > 4) {
            toast.warning(`Post can only have 4 images`);
            return;
        }
        const newFiles = files.slice(0, 4 - selectedImagesUpload.length - (currentImages?.length || 0));
        setSelectedImagesUpload([...selectedImagesUpload, ...newFiles]);
        const newPreviews = newFiles.map((file) => URL.createObjectURL(file));
        setImagesUploadPreviews([...imagesUploadPreviews, ...newPreviews]);
    };

    const handleRemoveImageUpload = (index) => {
        const newSelectedImages = [...selectedImagesUpload];
        const newImagesPreviews = [...imagesUploadPreviews];
        newSelectedImages.splice(index, 1);
        newImagesPreviews.splice(index, 1);
        setSelectedImagesUpload(newSelectedImages);
        setImagesUploadPreviews(newImagesPreviews);
    };

    const handleRemoveCurrentImages = (index) => {
        const newCurrentImages = [...currentImages];
        newCurrentImages.splice(index, 1);
        setCurrentImages(newCurrentImages);
    }

    const handleUserClick = (user) => {
        if (!selectedTags.some(tag => tag.id === user.id)) {
            setSelectedTags([...selectedTags, user]);
        }
    };

    const handleRemoveTag = (removeUser) => {
        setSelectedTags(selectedTags.filter((user) => user.id !== removeUser.id));
    };

    const form = useForm({
        defaultValues: {
            content: content,
            visibility: visibility
        }
    })

    const onSubmit = async (values) => {
        const formData = new FormData()
        formData.append("id", id)
        formData.append("content", values.content)
        formData.append("visibility", values.visibility)
        selectedImagesUpload.forEach(image => {
            formData.append("photoLists", image)
        })
        // formData.append("tagUsers", selectedTags.map(user => user.id).join(","))
        if (currentImages) {
            formData.append("photoListString", currentImages.join(","))
        }

        setIsLoading(true)
        const result = await editPost(formData)

        if (!result.isSuccessful) {
            console.log(result.message)
            toast.error("Failed to edit post")
        } else {
            toast.success("Post edit successfully")
            router.refresh()
        }
        setIsLoading(false)
    }

    return (
        <div className="sm:max-w-[650px]">
            <DialogHeader className="mb-2">
                <DialogTitle>Edit Post</DialogTitle>
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
                                name="currentImages"
                                render={({field}) => (
                                    <FormItem>
                                        <FormLabel>Current images</FormLabel>
                                        <div
                                            className="border-2 border-dashed border-muted rounded-md p-4 flex flex-col items-center justify-center gap-2 hover:bg-muted/5 transition-colors"
                                        >
                                            {/*<FormControl>*/}
                                            {/*    <Input {...field} type="file" accept="image/*" multiple*/}
                                            {/*           onChange={handleImageUpload}*/}
                                            {/*           disabled={selectedImagesUpload.length + currentImages.length >= 4}/>*/}
                                            {/*</FormControl>*/}
                                            <div className="flex justify-center">
                                                <Carousel className="w-full max-w-xs">
                                                    <CarouselContent>
                                                        {currentImages?.map((preview, index) => (
                                                            <CarouselItem key={index} className="relative">
                                                                <Image src={preview} alt={`Preview ${index}`}
                                                                       width={200}
                                                                       height={200}/>
                                                                <button
                                                                    type="button"
                                                                    className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1"
                                                                    onClick={() => handleRemoveCurrentImages(index)}
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
                            <FormField
                                control={form.control}
                                name="photos"
                                render={({field}) => (
                                    <FormItem>
                                        <FormLabel>Photos ({selectedImagesUpload.length}/
                                            {currentImages ? 4 - currentImages.length : 4})</FormLabel>
                                        <div
                                            className="border-2 border-dashed border-muted rounded-md p-4 flex flex-col items-center justify-center gap-2 hover:bg-muted/5 transition-colors"
                                        >
                                            <FormControl>
                                                <Input
                                                    {...field}
                                                    type="file"
                                                    accept="image/*"
                                                    multiple
                                                    onChange={handleImageUpload}
                                                    disabled={(currentImages?.length || 0) + selectedImagesUpload.length >= 4}
                                                />
                                            </FormControl>
                                            <div className="flex justify-center">
                                                <Carousel className="w-full max-w-xs">
                                                    <CarouselContent>
                                                        {imagesUploadPreviews.map((preview, index) => (
                                                            <CarouselItem key={index} className="relative">
                                                                <Image src={preview} alt={`Preview ${index}`}
                                                                       width={200}
                                                                       height={200}/>
                                                                <button
                                                                    type="button"
                                                                    className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1"
                                                                    onClick={() => handleRemoveImageUpload(index)}
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
                                <Button type="submit">Edit Post</Button>
                            </div>
                        </form>
                    </Form>
                )}
            </ScrollArea>
        </div>
    )
}

export default EditPostForm;