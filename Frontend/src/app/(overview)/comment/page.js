import {ScrollArea} from "@/components/ui/scroll-area";
import {Textarea} from "@/components/ui/textarea";
import React from "react";
import CloseDialogPage from "@/app/(overview)/components/ultils/CloseDialogPage";
import {getCommentInPost} from "@/lib/data";
import SomethingWentWrong from "@/app/(overview)/components/ultils/SomethingWentWrong";
import CommentList from "@/app/(overview)/components/comment/CommentList";
import {Dialog, DialogContent, DialogTrigger} from "@/components/ui/dialog";
import {Button} from "@/components/ui/button";
import CommentForm from "@/app/(overview)/components/comment/CommentForm";

export const revalidate = 1;

export default async function Page({searchParams}) {
    const postId = searchParams.postId
    const page = searchParams?.page;
    const result = await getCommentInPost(page, postId);
    let pageMeta = null;
    let comments = null;

    if (result?.isSuccessful) {
        pageMeta = result.data.pageMeta;
        comments = result.data.data;
    }

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
            <div className="bg-background p-6 rounded-lg shadow-lg w-full max-w-3xl flex flex-col h-[80vh]">
                <h2 className="text-xl font-bold mb-4">Comments</h2>

                {!result?.isSuccessful ? <SomethingWentWrong/> : (
                    <ScrollArea className="flex-grow">
                        {!result.isSuccessful ? <SomethingWentWrong/> :
                            <CommentList initialComments={comments} initialPageMeta={pageMeta}/>
                        }
                    </ScrollArea>
                )}
                <div className="mt-6">
                    <div className="flex justify-end mt-2 gap-4">
                        <Dialog>
                            <DialogTrigger asChild>
                                <Button>Leave a Comment</Button>
                            </DialogTrigger>
                            <DialogContent>
                                <CommentForm postId={postId}/>
                            </DialogContent>
                        </Dialog>
                        <CloseDialogPage>Button</CloseDialogPage>
                    </div>
                </div>
            </div>
        </div>

    )
}

