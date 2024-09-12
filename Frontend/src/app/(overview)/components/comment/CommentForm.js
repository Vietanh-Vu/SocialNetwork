"use client";
import {Button} from "@/components/ui/button";
import {Label} from "@/components/ui/label";
import {Input} from "@/components/ui/input";
import {Textarea} from "@/components/ui/textarea";
import {DialogHeader, DialogTitle, DialogFooter} from "@/components/ui/dialog";
import {useState} from "react";
import {useForm} from "react-hook-form";
import {Form, FormControl, FormField, FormItem, FormLabel} from "@/components/ui/form";
import Image from "next/image";
import {XIcon} from "lucide-react";
import {createComment, editComment} from "@/lib/action";
import {toast} from "sonner";
import Spinner from "@/app/(overview)/components/ultils/Spinner";
import {useRouter} from "next/navigation";
import {ScrollArea} from "@/components/ui/scroll-area";

function CommentForm({postId, parentCommentId}) {
    const [selectedImage, setSelectedImage] = useState([]);
    const [isDelete, setIsDelete] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const router = useRouter();

    const form = useForm({
        defaultValues: {
            content: "",
            image: null
        }
    })

    const handleUploadImage = (e) => {
        setSelectedImage(Array.from(e.target.files));
    }

    const handleRemoveImage = () => {
        setSelectedImage([]);
    }

    async function onSubmit(values) {
        const formData = new FormData();
        formData.append("postId", postId);
        if (parentCommentId) {
            formData.append("parentCommentId", parentCommentId);
        }
        formData.append("content", values.content);
        selectedImage.forEach(image => {
            formData.append("image", image);
        })

        setIsLoading(true);
        const result = await createComment(formData);
        setIsLoading(false);
        if (result.isSuccessful) {
            toast.success("Create comment successfully");
            router.refresh();
        } else {
            console.log(result.message);
            if (result.message === "Your comment is considered as spam") {
                toast.error("Your comment is considered as spam");
            } else {
                toast.error("Error while create comment");
            }
        }
    }

    return (
        <>
            <DialogHeader>
                <DialogTitle>Leave a Comment</DialogTitle>
            </DialogHeader>
            <ScrollArea className="max-h-[400px]">
                {isLoading ? <Spinner/> : (
                    <Form {...form}>
                        <form onSubmit={form.handleSubmit(onSubmit)} className="grid gap-4">
                            <div className="space-y-2">
                                <FormField
                                    control={form.control}
                                    name="content"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel>Content</FormLabel>
                                            <Textarea
                                                {...field}
                                                id="comment"
                                                placeholder="Write your comment here..."
                                                className="min-h-[100px]"
                                            />
                                        </FormItem>
                                    )}
                                />
                            </div>
                            <div className="space-y-2">
                                <FormField
                                    control={form.control}
                                    name="image"
                                    render={({field}) => (
                                        <FormItem>
                                            <FormLabel>Image</FormLabel>
                                            <div
                                                className="border-2 border-dashed border-muted rounded-md p-4 flex flex-col items-center justify-center gap-2 hover:bg-muted/5 transition-colors"
                                            >
                                                <FormControl>
                                                    <Input type="file" accept="image/*"
                                                           onChange={handleUploadImage}/>
                                                </FormControl>
                                                <div className="flex justify-center">
                                                    {(selectedImage.length !== 0) && (
                                                        <div className="relative">
                                                            <Image
                                                                src={URL.createObjectURL(selectedImage[0])}
                                                                alt={`Preview`}
                                                                width={200}
                                                                height={200}/>
                                                            <button
                                                                type="button"
                                                                className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1"
                                                                onClick={handleRemoveImage}
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
                            <DialogFooter>
                                <Button type="submit">Comment</Button>
                            </DialogFooter>
                        </form>
                    </Form>
                )}
            </ScrollArea>
        </>
    );
}

export default CommentForm;